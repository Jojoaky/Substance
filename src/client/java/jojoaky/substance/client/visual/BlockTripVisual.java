package jojoaky.substance.client.visual;

import jojoaky.substance.Config;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class BlockTripVisual extends TripVisual {
    private final BlockState block;

    public BlockTripVisual(TripVisualType type, BlockState block, Vec3 position, int lifetime) {
        super(type, position, lifetime);
        this.block = block;
    }

    @Override
    protected void renderVisual(
            WorldRenderContext context,
            Minecraft minecraft,
            ClientLevel level,
            float age,
            float alpha
    ) {
        float motion = minecraft.options.screenEffectScale().get().floatValue();
        if (motion <= 0 || context.consumers() == null) {
            return;
        }

        context.matrixStack().translate(
                (0.18 + Math.sin(age * 0.05) * 0.06) * motion,
                (0.14 + Math.sin(age * 0.03) * 0.05) * motion,
                0.07 * motion
        );
        var blockImage = TripVisualRenderer.alphaBuffers(context, alpha);
        BlockPos sourcePosition = BlockPos.containing(position());
        int packedLight = LevelRenderer.getLightColor(level, sourcePosition.above());
        minecraft.getBlockRenderer().renderSingleBlock(
                block,
                context.matrixStack(),
                type -> blockImage.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS)),
                packedLight,
                OverlayTexture.NO_OVERLAY
        );
    }
}
