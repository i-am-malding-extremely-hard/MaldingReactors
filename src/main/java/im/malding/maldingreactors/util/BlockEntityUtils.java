package im.malding.maldingreactors.util;

import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.Tickable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockEntityUtils {

    public static <T extends BlockEntity, I> List<T> getCollection(World world, I instance, Function<I, Collection<BlockPos>> getter, BlockEntityType<T> type){
        return getCollection(world, getter.apply(instance), type);
    }

    public static <T extends BlockEntity> List<T> getCollection(World world, Collection<BlockPos> blockPositions, BlockEntityType<T> type){
        return blockPositions.stream()
                .map(blockPos -> world.getBlockEntity(blockPos, type))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public static <T extends BlockEntity & Tickable> BlockEntityTicker<T> createBlockEntityTicker(Supplier<BlockEntityType<T>> typeSupplier, BlockEntityType<?> type){
        if(typeSupplier.get() != type) return null;

        return ((world1, pos, state1, blockEntity) -> {
            if (world1.isClient) {
                blockEntity.clientTick();
            } else {
                blockEntity.serverTick();
            }
        });
    }
}
