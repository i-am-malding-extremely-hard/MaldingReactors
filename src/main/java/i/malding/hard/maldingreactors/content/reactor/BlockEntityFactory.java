package i.malding.hard.maldingreactors.content.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockEntityFactory {

    @Nullable
    BlockEntity createBlockEntity(BlockPos pos, BlockState state);

    static BlockEntityFactory typed(BlockEntityType<?> type, TriFunction<BlockEntityType<?>, BlockPos, BlockState, BlockEntity> typeFactory){
        return (pos, state) -> typeFactory.apply(type, pos, state);
    }
}
