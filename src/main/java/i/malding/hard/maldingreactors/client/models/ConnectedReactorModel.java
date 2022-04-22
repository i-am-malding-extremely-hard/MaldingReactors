package i.malding.hard.maldingreactors.client.models;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.Random;
import java.util.function.Supplier;

public class ConnectedReactorModel extends ForwardingBakedModel {
    public ConnectedReactorModel(BakedModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if(blockView instanceof RenderAttachedBlockView attachedBlockView && (boolean) attachedBlockView.getBlockEntityRenderAttachment(pos)) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }

    }
}
