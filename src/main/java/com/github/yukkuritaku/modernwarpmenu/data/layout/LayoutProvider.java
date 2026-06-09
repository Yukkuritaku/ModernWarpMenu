package com.github.yukkuritaku.modernwarpmenu.data.layout;

import com.github.yukkuritaku.modernwarpmenu.ModernWarpMenu;
import com.github.yukkuritaku.modernwarpmenu.data.layout.texture.LayoutTexture;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LayoutProvider implements DataProvider {

    private final List<LayoutFile> layouts = Collections.synchronizedList(new LinkedList<>());

    private final PackOutput.PathProvider pathProvider;
    private final String modid;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public LayoutProvider(FabricPackOutput output, String modid, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "layouts");
        this.modid = modid;
        this.lookupProvider = lookupProvider;
    }

    public record LayoutFile(Layout layout, String fileName) {
    }

    protected Path getPath(Identifier id) {
        return this.pathProvider.json(id);
    }

    private CompletableFuture<?> generateLayouts(CachedOutput cache) {
        CompletableFuture<?>[] completableFutures = new CompletableFuture<?>[this.layouts.size()];
        int size = 0;
        for (var layout : this.layouts) {
            var target = getPath(Identifier.fromNamespaceAndPath(this.modid, layout.fileName));
            completableFutures[size++] = DataProvider.saveStable(cache, Layout.CODEC.codec().encodeStart(JsonOps.INSTANCE, layout.layout).getOrThrow(), target);
        }
        return CompletableFuture.allOf(completableFutures);
    }

    private void addLayouts() {
        this.layouts.add(new LayoutFile(new Layout(Layout.LayoutType.OVERWORLD, Layout.EMPTY,
                List.of(
                        new Island("Hub",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/hub.png"),
                                        1760, 1340),
                                24, 18, 0, 0.26f,
                                List.of(
                                        new Warp(20, 13, "Spawn", "hub"),
                                        new Warp(18, 22, "Museum", "museum"),
                                        new Warp(3, 11, "Crypts", "crypts"),
                                        new Warp(27, 18, "Wizard", "wizard"),
                                        new Warp(33, 26, "Sirius (DA)", "da"),
                                        new Warp(2, 23, "Ruins", "castle")
                                )
                        ),
                        new Island("Crimson Isle",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/crimson_isle.png"), 926, 656),
                                18, 1, 2, 0.25f,
                                List.of(
                                        new Warp(4, 30, "Spawn", "isle"),
                                        new Warp(22, 8, "Skull", "skull"),
                                        new Warp(27, 14, "Tomb", "smold"),
                                        new Warp(12, 12, "Wasteland", "wasteland", List.of("bingo")),
                                        new Warp(11, 2, "Dragontail", "dragontail", List.of("bingo")),
                                        new Warp(21, 24, "Scarleton", "scarleton", List.of("bingo"))
                                )),
                        new Island("Spider's Den",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/spiders_den.png"), 790, 522),
                                14, 13, 1, 0.15f,
                                List.of(
                                        new Warp(23, 23, "Spawn", "spider"),
                                        new Warp(31, 1, "Top", "top"),
                                        new Warp(6, 16, "Arachne", "arachne")
                                )),
                        new Island("The End",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/end.png"), 1400, 1320),
                                2, 2, 2, 0.20f,
                                List.of(
                                        new Warp(27, 19, "Spawn", "end"),
                                        new Warp(13, 29, "Nest", "drag"),
                                        new Warp(24, 28, "Void", "void")
                                )
                        ),
                        new Island("Gold Mine",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/gold_mine.png"), 464, 493),
                                31, 11, 1, 0.1f,
                                List.of(new Warp(8, 20, "Spawn", "gold"))
                        ),
                        new Island("Deep Caverns",
                                new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                        "textures/gui/islands/deep_caverns.png"), 397, 509),
                                38, 2, 2, 0.135f,
                                List.of(
                                        new Warp(5, 21, "Spawn", "deep"),
                                        new Warp(33, 12, "Dwarven\nMines", "dwarves"),
                                        new Warp(20, 5, "Forge", "forge"),
                                        new Warp(6, 12, "Base Camp", "basecamp"),
                                        new Warp(31, 26, "Hollows", "ch"),
                                        new Warp(18, 30, "Nucleus", "nucleus")
                                )),
                        new Island("Home", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/private_island.png"), 414, 488),
                                39, 30, 1, 0.05f,
                                List.of(new Warp(3, 3, "Spawn", "home"))
                        ),
                        new Island("Garden", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/garden.png"), 941, 569),
                                43, 25, 2, 0.1f,
                                List.of(new Warp(10, 8, "Spawn", "garden"))),
                        new Island("The Barn", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/barn.png"), 1080, 1080),
                                41, 15, 1, 0.11f,
                                List.of(new Warp(1, 19, "Spawn", "barn"))
                        ),
                        new Island("Mushroom Desert", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/mushroom_desert.png"), 827, 604),
                                50, 1, 2, 0.15f,
                                List.of(new Warp(14, 29, "Spawn", "desert"),
                                        new Warp(9, 8, "Trapper", "trapper"),
                                        new Warp(18, 16, "Mushroom Cave", "glowing"))
                        ),
                        new Island("The Park", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/park.png"), 1371, 1028),
                                12, 22, 0, 0.15f,
                                List.of(new Warp(34, 32, "Spawn", "park"),
                                        new Warp(28, 14, "Cave", "howl"),
                                        new Warp(16, 6, "Jungle", "jungle"))),
                        new Island("Galetea", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/galatea.png"), 1129, 871),
                                1, 18, 0, 0.15f,
                                List.of(new Warp(13, 28, "Spawn", "galatea"),
                                        new Warp(22, 15, "Murkwater", "murkwater"))),
                        new Island("Bayou", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/bayou.png"), 951, 875),
                                51, 19, 0, 0.1f,
                                List.of(new Warp(5, 3, "Spawn", "bayou"))),
                        new Island("Lotus Atoll", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/lotus.png"), 1280, 1336),
                                54, 9, 0, 0.13f,
                                List.of(new Warp(7, 33, "Spawn", "lotus"))),
                        new Island("Jerry's Workshop", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/jerrys_workshop.png"), 890, 602),
                                2, 28, 2, 0.15f,
                                List.of(new Warp(22, 16, "Jerry", "jerry",
                                        List.of("jerry")))),
                        new Island("Dungeon Hub", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/dungeon_hub.png"), 256, 512),
                                51, 29, 2, 0.03f,
                                List.of(new Warp(6, 15, "Spawn", "dungeons"))
                        ),
                        new Island("Rift NPC", new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                                "textures/gui/islands/rift_npc.png"), 122, 268),
                                56, 27, 2, 0.03f,
                                List.of(new Warp(6, 15, "Rift", "rift"))
                        )

                ),
                new WarpIcon(new LayoutTexture(Identifier.fromNamespaceAndPath(
                        ModernWarpMenu.MOD_ID, "textures/gui/portal.png"), 207, 256),
                        0.02f),
                new Button(new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                        "icon.png"), 512, 512), 60, 30, 0.03f
                ),
                new Button(new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                        "textures/gui/regular_warp_menu.png"), 128, 64), 60, 29, 0.03f
                )
        ),
                "layout"));
        this.layouts.add(new LayoutFile(new Layout(
                Layout.LayoutType.RIFT,
                Layout.EMPTY,
                List.of(
                        new Island("Rift", new LayoutTexture(Identifier.fromNamespaceAndPath(
                                ModernWarpMenu.MOD_ID, "textures/gui/islands/rift.png"), 974, 1051),
                                21, 6, 0, 0.35f,
                                List.of(
                                        new Warp(18, 25, "Wizard Tower", 10),
                                        new Warp(8, 23, "Lagoon Hut", 11),
                                        new Warp(18, 11, "Dreadfarm", 12),
                                        new Warp(22, 19, "Plaza", 13),
                                        new Warp(11, 18, "Colosseum", 14),
                                        new Warp(31, 28, "Stillgore Château", 15),
                                        new Warp(24, 23, "Mountaintop", 16)
                                ))
                ),
                new WarpIcon(new LayoutTexture(Identifier.fromNamespaceAndPath(
                        ModernWarpMenu.MOD_ID, "textures/gui/portal.png"), 207, 256),
                        0.02f),
                new Button(new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                        "icon.png"), 512, 512), 60, 31, 0.05f
                ),
                new Button(new LayoutTexture(Identifier.fromNamespaceAndPath(ModernWarpMenu.MOD_ID,
                        "textures/gui/regular_warp_menu.png"), 128, 64), 60, 29, 0.05f
                )), "rift_layout"));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        this.addLayouts();
        return CompletableFuture.allOf(generateLayouts(output));
    }

    @Override
    public String getName() {
        return "layouts";
    }
}
