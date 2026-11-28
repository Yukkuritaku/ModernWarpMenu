package com.github.yukkuritaku.modernwarpmenu.client.gui.render.state;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
public record BlitFloatRenderState(
        RenderPipeline pipeline,
        TextureSetup textureSetup,
        Matrix3x2f pose,
        float x0,
        float y0,
        float x1,
        float y1,
        float u0,
        float u1,
        float v0,
        float v1,
        int color,
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements GuiElementRenderState {
    public BlitFloatRenderState(
            RenderPipeline pipeline,
            TextureSetup textureSetup,
            Matrix3x2f pose,
            float x0,
            float y0,
            float x1,
            float y1,
            float u0,
            float u1,
            float v0,
            float v1,
            int color,
            @Nullable ScreenRectangle scissorArea
    ) {
        this(pipeline, textureSetup, pose, x0, y0, x1, y1, u0, u1, v0, v1, color, scissorArea, getBounds((int) x0, (int) y0, (int) x1, (int) y1, pose, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer consumer) {
        consumer.addVertexWith2DPose(this.pose(), this.x0(), this.y0()).setUv(this.u0(), this.v0()).setColor(this.color());
        consumer.addVertexWith2DPose(this.pose(), this.x0(), this.y1()).setUv(this.u0(), this.v1()).setColor(this.color());
        consumer.addVertexWith2DPose(this.pose(), this.x1(), this.y1()).setUv(this.u1(), this.v1()).setColor(this.color());
        consumer.addVertexWith2DPose(this.pose(), this.x1(), this.y0()).setUv(this.u1(), this.v0()).setColor(this.color());
    }

    @Nullable
    private static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, Matrix3x2f pose, @Nullable ScreenRectangle scissorArea) {
        ScreenRectangle screenRectangle = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRectangle) : screenRectangle;
    }
}

