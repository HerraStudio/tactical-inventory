package dev.tactical.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tactical.loot.SafeBlockEntity;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.data.ModelData;

public final class SafeRenderer implements BlockEntityRenderer<SafeBlockEntity> {
    @FunctionalInterface
    public interface Model {
        void render(SafeBlockEntity safe,float partialTick,PoseStack pose,MultiBufferSource buffers,int light,int overlay);
    }
    private static final Model PLACEHOLDER=(safe,partialTick,pose,buffers,light,overlay)->
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.STONE.defaultBlockState(),pose,buffers,light,overlay,ModelData.EMPTY,null);
    private static Model model=PLACEHOLDER;

    public static void setModel(Model replacement) { model=Objects.requireNonNull(replacement); }
    public static void resetModel() { model=PLACEHOLDER; }

    @Override public void render(SafeBlockEntity safe,float partialTick,PoseStack pose,MultiBufferSource buffers,int light,int overlay) {
        pose.pushPose();
        try { model.render(safe,partialTick,pose,buffers,light,overlay); }
        finally { pose.popPose(); }
    }
}
