package com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants;

import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu.ItemMatchCondition;
import com.github.yukkuritaku.modernwarpmenu.data.skyblockconstants.menu.Menu;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkyBlockConstantsProvider implements DataProvider {

    private final List<SkyBlockConstantsFile> skyBlockConstantsFiles = Collections.synchronizedList(new LinkedList<>());
    private final PackOutput.PathProvider pathProvider;
    private final String modid;

    public SkyBlockConstantsProvider(PackOutput output, String modid) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "constants");
        this.modid = modid;
    }

    public record SkyBlockConstantsFile(SkyBlockConstants constants, String fileName) {
    }

    protected Path getPath(Identifier id) {
        return this.pathProvider.json(id);
    }

    private CompletableFuture<?> generateFeatures(CachedOutput cache) {
        CompletableFuture<?>[] completableFutures = new CompletableFuture<?>[this.skyBlockConstantsFiles.size()];
        int size = 0;
        for (var constant : this.skyBlockConstantsFiles) {
            var target = getPath(Identifier.fromNamespaceAndPath(this.modid, constant.fileName));
            completableFutures[size++] = DataProvider.saveStable(cache, SkyBlockConstants.CODEC.codec()
                    .encodeStart(JsonOps.INSTANCE, constant.constants).getOrThrow(IllegalStateException::new), target);
        }

        return CompletableFuture.allOf(completableFutures);
    }

    private void addConstants() {
        this.skyBlockConstantsFiles.add(new SkyBlockConstantsFile(new SkyBlockConstants(
                Map.of(Menu.FAST_TRAVEL,
                        List.of(
                                new ItemMatchCondition(45,
                                        "Island Browser",
                                        List.of(),
                                        "minecraft:blaze_powder",
                                        List.of(), "", List.of(), ItemMatchCondition.EMPTY_PATTERN
                                ),
                                new ItemMatchCondition(49,
                                        "Close",
                                        List.of(),
                                        "minecraft:barrier",
                                        List.of(), "", List.of(), ItemMatchCondition.EMPTY_PATTERN
                                ), new ItemMatchCondition(53,
                                        "",
                                        List.of("Paper Icons", ""),
                                        "",
                                        List.of("minecraft:map", "minecraft:filled_map", "minecraft:black_stained_glass_pane"),
                                        "", List.of(), ItemMatchCondition.EMPTY_PATTERN
                                )
                        ),
                                Menu.PORHTAL,
                                List.of(new ItemMatchCondition(31,
                                        "Close",
                                        List.of(),
                                        "minecraft:barrier",
                                        List.of(), "", List.of(), ItemMatchCondition.EMPTY_PATTERN
                                ))),
                        new WarpMessages(Collections.singletonList("Warping..."),
                                Map.of("Unknown destination! Check the Fast Travel menu to view options!", "modernwarpmenu.errors.unknownDestination",
                                        "You haven't unlocked this fast travel destination!", "modernwarpmenu.errors.notUnlocked",
                                        "Couldn't warp you! Try again later. (NO_DESTINATION_FOUND)", "modernwarpmenu.errors.noDestination",
                                        "You need to have visited this island at least once before fast traveling to it!", "modernwarpmenu.errors.notVisited",
                                        "Jerry's Workshop is only available during the Winter!", "modernwarpmenu.errors.notOpenYet")),
                        List.of(new WarpCommandVariant("warp", WarpCommandVariant.WarpCommandType.ALIAS),
                                new WarpCommandVariant("travel", WarpCommandVariant.WarpCommandType.ALIAS),
                                new WarpCommandVariant("is", WarpCommandVariant.WarpCommandType.WARP),
                                new WarpCommandVariant("hub", WarpCommandVariant.WarpCommandType.WARP),
                                new WarpCommandVariant("warpforge", WarpCommandVariant.WarpCommandType.WARP),
                                new WarpCommandVariant("savethejerrys", WarpCommandVariant.WarpCommandType.WARP),
                                new WarpCommandVariant("garry", WarpCommandVariant.WarpCommandType.WARP)
                        ),
                        ""
                ),
                "skyblock_constants"));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.addConstants();
        return CompletableFuture.allOf(generateFeatures(output));
    }

    @Override
    public String getName() {
        return "constants";
    }
}
