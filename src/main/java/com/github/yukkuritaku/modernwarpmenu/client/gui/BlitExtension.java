package com.github.yukkuritaku.modernwarpmenu.client.gui;

import com.github.yukkuritaku.modernwarpmenu.client.gui.render.state.BlitFloatRenderState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;

public interface BlitExtension {

    static void blit(
            GuiGraphicsExtractor gui, RenderPipeline pipeline, Identifier atlas, float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight, int color
    ) {
        blit(gui, pipeline, atlas, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color);
    }
    static void blit(
            GuiGraphicsExtractor gui, RenderPipeline pipeline,
            Identifier atlas,
            float x,
            float y,
            float u,
            float v,
            float width,
            float height,
            float uWidth,
            float vHeight,
            float textureWidth,
            float textureHeight,
            int color
    ) {
        innerBlit(
                gui, pipeline,
                atlas,
                x,
                x + width,
                y,
                y + height,
                (u + 0.0F) / textureWidth,
                (u + uWidth) / textureWidth,
                (v + 0.0F) / textureHeight,
                (v + vHeight) / textureHeight,
                color
        );
    }
    private static void innerBlit(GuiGraphicsExtractor gui, RenderPipeline pipeline, Identifier atlas, float x0, float x1, float y0, float y1, float u0, float u1, float v0, float v1, int color) {
        GpuTextureView gpuTextureView = Minecraft.getInstance().getTextureManager().getTexture(atlas).getTextureView();
        submitBlit(gui, pipeline, gpuTextureView, x0, y0, x1, y1, u0, u1, v0, v1, color);
    }

    private static void submitBlit(
            GuiGraphicsExtractor gui, RenderPipeline pipeline, GpuTextureView atlasTexture, float x0, float y0, float x1, float y1, float u0, float u1, float v0, float v1, int color
    ) {
        gui.guiRenderState
                .addGuiElement(
                        new BlitFloatRenderState(
                                pipeline, TextureSetup.singleTexture(atlasTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)), new Matrix3x2f(gui.pose()), x0, y0, x1, y1, u0, u1, v0, v1, color, gui.scissorStack.peek()
                        )
                );
    }
}
