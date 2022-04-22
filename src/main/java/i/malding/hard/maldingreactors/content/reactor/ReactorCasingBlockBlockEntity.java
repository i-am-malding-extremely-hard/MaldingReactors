package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingBlockEntities;
import i.malding.hard.maldingreactors.multiblock.ReactorPart;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ReactorCasingBlockBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity, ReactorPart {

    public ReactorCasingBlockBlockEntity(BlockPos pos, BlockState state) {
        super(MaldingBlockEntities.REACTOR_CASING, pos, state);
    }

    @Override
    public Object getRenderAttachmentData() {
        return isValid();
    }

    protected boolean isStructureValid;

    @Override
    public void setValid(boolean valid) {
        this.isStructureValid = valid;
    }

    @Override
    public boolean isValid() {
        return isStructureValid;
    }
}
