package com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A match condition that checks if a given SkyBlock {@code GuiChest} menu contains an item with the same
 * item name, inventory slot index, Minecraft item ID, and SkyBlock item ID as the item specified in this condition.
 */
public record ItemMatchCondition(int inventorySlot,
                                 String itemName,
                                 List<String> itemNameList,
                                 String itemId,
                                 List<String> itemIdList,
                                 String skyBlockItemId,
                                 List<String> skyBlockItemIdList,
                                 Pattern loreMatchPattern
) {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Pattern EMPTY_PATTERN = Pattern.compile("");

    public static final MapCodec<ItemMatchCondition> CODEC = RecordCodecBuilder.<ItemMatchCondition>mapCodec(instance ->
            instance.group(
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("inventory_slot").forGetter(ItemMatchCondition::inventorySlot),
                            Codec.STRING.optionalFieldOf("item_name", "").forGetter(ItemMatchCondition::itemName),
                            Codec.STRING.listOf().optionalFieldOf("item_name_list", List.of()).forGetter(ItemMatchCondition::itemNameList),
                            Codec.STRING.optionalFieldOf("item_id", "").forGetter(ItemMatchCondition::itemId),
                            Codec.STRING.listOf().optionalFieldOf("item_id_list", List.of()).forGetter(ItemMatchCondition::itemIdList),
                            Codec.STRING.optionalFieldOf("skyblock_item_id", "").forGetter(ItemMatchCondition::skyBlockItemId),
                            Codec.STRING.listOf().optionalFieldOf("skyblock_item_id_list", List.of()).forGetter(ItemMatchCondition::skyBlockItemIdList),
                            ExtraCodecs.PATTERN.optionalFieldOf("lore_match_pattern", EMPTY_PATTERN).forGetter(ItemMatchCondition::loreMatchPattern)
                    )
                    .apply(instance, ItemMatchCondition::new))
            .validate(ItemMatchCondition::validate);

    /**
     * Checks whether the given {@code IInventory} contains an item that satisfies this item match condition.
     *
     * @param container the inventory to check for a matching item
     * @return {@code true} if an item in {@code inventory} satisfies this item match condition, {@code false} otherwise
     */
    public boolean inventoryContainsMatchingItem(Container container) {
        if (container == null) {
            throw new NullPointerException("Inventory cannot be null");
        } else if (container.getContainerSize() <= 0) {
            throw new IllegalArgumentException("Cannot check for matching item in empty inventory");
        } else if (container.getContainerSize() < this.inventorySlot) {
            throw new IllegalArgumentException(
                    String.format("Inventory size (%d) is smaller than match condition slot index (%d)",
                            container.getContainerSize(), this.inventorySlot));
        }
        ItemStack stack = container.getItem(this.inventorySlot);
        if (!stack.isEmpty()) {

            boolean itemNameMatches;
            boolean minecraftItemIDMatches;
            boolean skyBlockItemIDMatches;
            boolean lorePatternMatches;
            if (!StringUtil.isNullOrEmpty(this.itemName) || !this.itemNameList.isEmpty()) {
                String itemStackName = stack.has(DataComponents.CUSTOM_NAME) ?
                        ChatFormatting.stripFormatting(stack.getHoverName().getString()).trim() : null;
                itemNameMatches = itemStackName != null
                        && (itemStackName.equals(this.itemName) || this.itemNameList.contains(itemStackName));


                if (!itemNameMatches) {
                    LOGGER.warn("Item name mismatch\nExpected {} ; Found {}",
                            this.itemName, itemStackName);
                    return false;
                }
            }
            if (!StringUtil.isNullOrEmpty(this.itemId) || !this.itemIdList.isEmpty()) {
                String stackItemId = stack.typeHolder().getRegisteredName();
                minecraftItemIDMatches = stackItemId.equals(this.itemId)
                        || this.itemIdList.contains(stackItemId);
                if (!minecraftItemIDMatches) {
                    LOGGER.warn("Minecraft Item ID mismatch\nExpected {} ; Found {}",
                            this.itemId, stackItemId);
                    return false;
                }
            }
            /*if (!stack.has(DataComponents.CUSTOM_DATA)) {
                LOGGER.info("No Custom Data, return");
                return false;
            }*/

            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

            if (!StringUtil.isNullOrEmpty(this.skyBlockItemId) || !this.skyBlockItemIdList.isEmpty()) {
                Optional<CompoundTag> extraAttributes = tag.getCompound("ExtraAttributes");
                if (extraAttributes.isEmpty()) {
                    return false;
                }
                Optional<String> skyBlockId = extraAttributes.get().getString("id");
                skyBlockItemIDMatches = skyBlockId.isPresent() &&
                        (skyBlockId.get().equals(this.skyBlockItemId) || this.skyBlockItemIdList.contains(skyBlockId.get()));
                /*String skyBlockId = extraAttributes.contains("id", Tag.TAG_STRING) ?
                        extraAttributes.getString("id") : null;
                skyBlockItemIDMatches = skyBlockId != null &&
                        (skyBlockId.equals(this.skyBlockItemId) || this.skyBlockItemIdList.contains(skyBlockId));*/

                if (!skyBlockItemIDMatches) {
                    LOGGER.warn("SkyBlock Item ID mismatch\nExpected {} ; Found {}",
                            this.skyBlockItemId, skyBlockId);
                    return false;
                }
            }

            if (this.loreMatchPattern != EMPTY_PATTERN && !Objects.equals(loreMatchPattern.pattern(), EMPTY_PATTERN.pattern())) {
                Optional<CompoundTag> display = tag.getCompound("display");
                if (display.isEmpty()){
                    return false;
                }
                Optional<ListTag> lore = display.get().getList("Lore");
                if (lore.isPresent()) {
                    if (!lore.get().isEmpty()) {
                        StringBuilder loreBuilder = new StringBuilder();
                        for (int i = 0; i < lore.get().size(); i++) {
                            loreBuilder.append(lore.get().getString(i)).append("\n");
                        }
                        loreBuilder.deleteCharAt(loreBuilder.length() - 1);
                        String loreString = loreBuilder.toString();
                        lorePatternMatches = this.loreMatchPattern.asPredicate().test(loreString);
                        if (!lorePatternMatches) {
                            LOGGER.warn("Lore did not match pattern\nItem lore: {}", lore);
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies that this match condition's properties are valid.
     * This is called for conditions that have just been deserialized.
     */
    private static DataResult<ItemMatchCondition> validate(ItemMatchCondition condition) {

        if (condition.itemName == null && condition.itemNameList.isEmpty()
                && condition.itemId == null && condition.itemIdList.isEmpty()
                && condition.skyBlockItemId == null && condition.skyBlockItemIdList.isEmpty()
                && condition.loreMatchPattern == null) {
            return DataResult.error(() -> "No item name, Minecraft item Id, SkyBlock item Id, or lore criteria specified.");
        }

        if (!StringUtil.isNullOrEmpty(condition.itemName) && !condition.itemNameList.isEmpty()) {
            return DataResult.error(() -> "item_name and item_name_list cannot both be set. Only one can be set.");
        }

        if (!StringUtil.isNullOrEmpty(condition.itemId) && !condition.itemIdList.isEmpty()) {
            return DataResult.error(() -> "item_id and item_id_list cannot both be set. Only one can be set.");
        }

        if (!StringUtil.isNullOrEmpty(condition.skyBlockItemId) && !condition.skyBlockItemIdList.isEmpty()) {
            return DataResult.error(() -> "skyblock_item_id and skyblock_item_id_list cannot both be set. Only one can be set.");
        }

        /*if (condition.loreMatchPattern != null && condition.loreMatchPattern.pattern() == EMPTY_PATTERN.pattern()) {
            return DataResult.error(() -> "Lore match pattern for item in slot " + condition.inventorySlot + " lacks a regex string.");
        }*/
        return DataResult.success(condition);
    }
}
