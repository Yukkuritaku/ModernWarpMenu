package com.github.yukkuritaku.modernwarpmenu.data.layout;

import com.github.yukkuritaku.modernwarpmenu.data.layout.texture.LayoutTexture;
import com.mojang.blaze3d.platform.Window;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public class Island {

    public static final MapCodec<Island> CODEC = RecordCodecBuilder.<Island>mapCodec(instance ->
            instance.group(
                            ExtraCodecs.NON_EMPTY_STRING.fieldOf("name").forGetter(island -> island.name),
                            LayoutTexture.CODEC.fieldOf("texture").forGetter(island -> island.texture),
                            LayoutTexture.CODEC.fieldOf("hover_effect_texture").forGetter(island -> island.hoverEffectTexture),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("grid_x").forGetter(island -> island.gridX),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("grid_y").forGetter(island -> island.gridY),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("z_level").forGetter(island -> island.zLevel),
                            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("width_percentage").forGetter(island -> island.widthPercentage),
                            Warp.CODEC.codec().listOf().fieldOf("warp_list").forGetter(island -> island.warpList),
                            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("default_warp_index", 0).forGetter(island -> island.defaultWarpIndex)
                    )
                    .apply(instance, Island::new))
            .validate(Island::validate);
    /**
     * Grid unit width is screenWidth / widthFactor
     */
    public static final int GRID_UNIT_WIDTH_FACTOR = 64;
    /**
     * Grid unit height is screenHeight / heightFactor
     */
    public static final int GRID_UNIT_HEIGHT_FACTOR = 36;

    public final String name;
    public final LayoutTexture texture;
    public final LayoutTexture hoverEffectTexture;
    public final int gridX;
    public final int gridY;
    public final int zLevel;
    public final float widthPercentage;
    public final List<Warp> warpList;
    private final int defaultWarpIndex;
    private transient int width;
    private transient int height;


    Island(String name,
           LayoutTexture texture,
           int gridX,
           int gridY,
           int zLevel,
           float widthPercentage,
           List<Warp> warpList) {
        this(name, texture, texture, gridX, gridY, zLevel, widthPercentage, warpList, 0);
    }
    Island(String name,
           LayoutTexture texture,
           LayoutTexture hoverEffectTexture,
           int gridX,
           int gridY,
           int zLevel,
           float widthPercentage,
           List<Warp> warpList,
           int defaultWarpIndex) {
        this.name = name;
        this.texture = texture;
        this.hoverEffectTexture = hoverEffectTexture;
        this.gridX = gridX;
        this.gridY = gridY;
        this.zLevel = zLevel;
        this.widthPercentage = widthPercentage;
        this.warpList = warpList;
        this.defaultWarpIndex = defaultWarpIndex;
    }

    private static DataResult<Island> validate(Island island){
        if (island.gridX < 0 || island.gridX > GRID_UNIT_WIDTH_FACTOR) {
            return DataResult.error(() -> "Island " + island.name + " grid_x is outside screen");
        }
        if (island.gridY < 0 || island.gridY > GRID_UNIT_HEIGHT_FACTOR) {
            return DataResult.error(() -> "Island " + island.name + " grid_y is outside screen");
        }
        if (island.zLevel < 0) {
            return DataResult.error(() -> "Island " + island.name + " z_level is outside screen");
        } else if (island.zLevel >= 10) {
            return DataResult.error(() -> "Island " + island.name + " z_level is too high. Z levels 10+ are reserved for warp buttons.");
        }
        if (island.widthPercentage < 0 || island.widthPercentage > 1) {
            return DataResult.error(() -> "Island " + island.name + " width_percentage must be between 0 and 1");
        }
        if (island.warpList == null || island.warpList.isEmpty()) {
            return DataResult.error(() -> "Island " + island.name + " has no warps");
        }
        return DataResult.success(island);
    }

    public void init(Window res) {
        float scaleFactor;
        this.width = (int) (res.getGuiScaledWidth() * this.widthPercentage);
        scaleFactor = (float) this.width / this.texture.width();
        this.height = (int) (this.texture.height() * scaleFactor);
    }


    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
