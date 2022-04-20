package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingFluids;
import i.malding.hard.maldingreactors.content.MaldingItems;
import i.malding.hard.maldingreactors.data.MaldingTags;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ReactorItemPortBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;

    public ReactorItemPortBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorItemPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((ReactorItemPortBlockEntity)blockEntity).tick(world1, pos, state1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ReactorItemPortBlockEntity portBlockEntity = (ReactorItemPortBlockEntity) world.getBlockEntity(pos);

        if (portBlockEntity != null && portBlockEntity.isFullMultipartStructure()) {
            BlockPos controllerPos = portBlockEntity.getControllerPos();

            if (player.shouldCancelInteraction()) {
                return extractWasteAmount(world, controllerPos, player);
            } else {
                return insertFuelAmount(world, portBlockEntity.getControllerPos(), player, hand);
            }
        }

        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        ReactorComponentBlockEntity portBlockEntity = (ReactorComponentBlockEntity) world.getBlockEntity(pos);

        return portBlockEntity != null && portBlockEntity.isFullMultipartStructure() ? super.createScreenHandlerFactory(state, world, pos) : null;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static ActionResult insertFuelAmount(World world, BlockPos controllerPos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isIn(MaldingTags.REACTOR_FUEL)) {
            if (!world.isClient) {
                ReactorControllerBlockEntity controller = (ReactorControllerBlockEntity) world.getBlockEntity(controllerPos);

                FluidTank fuelTank = controller.getFuelTank();
                FluidTank wasteTank = controller.getWasteTank();

                int stackCount = stack.getCount();

                long amountOfDropletsFromStack = stackCount * FluidConstants.INGOT;

                try (Transaction t = Transaction.openOuter()) {
                    long amountAllowedInsertable = wasteTank.simulateInsert(FluidVariant.of(MaldingFluids.YELLORIUM.still()), amountOfDropletsFromStack, t);

                    if (amountAllowedInsertable > 0) {
                        long actuallyInsertable = fuelTank.simulateInsert(FluidVariant.of(MaldingFluids.YELLORIUM.still()), amountAllowedInsertable, t);

                        int amountUsed = MathHelper.floor(actuallyInsertable / (float) FluidConstants.INGOT);

                        if (actuallyInsertable > 0 && amountUsed > 0) {
                            fuelTank.insert(FluidVariant.of(MaldingFluids.YELLORIUM.still()), amountUsed * FluidConstants.INGOT, t);

                            stack.decrement(amountUsed);
                            player.setStackInHand(hand, stack);

                            controller.markDirty();

                            t.commit();
                        }
                    }
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public static ActionResult extractWasteAmount(World world, BlockPos controllerPos, PlayerEntity player) {
        ReactorControllerBlockEntity controller = (ReactorControllerBlockEntity) world.getBlockEntity(controllerPos);

        if (!world.isClient) {
            FluidTank wasteTank = controller.getWasteTank();

            try (Transaction t = Transaction.openOuter()) {
                long amountOfWasteExtractable = wasteTank.simulateExtract(FluidVariant.of(MaldingFluids.CYANITE.still()), wasteTank.getCapacity(), t);

                int cyaniteCount = MathHelper.floor(amountOfWasteExtractable / (float) FluidConstants.INGOT);

                long amountOfWasteExtracted = wasteTank.extract(FluidVariant.of(MaldingFluids.CYANITE.still()), cyaniteCount * FluidConstants.INGOT, t);

                if (amountOfWasteExtracted != 0 && cyaniteCount != 0) {
                    ItemStack stack = MaldingItems.CYANITE_INGOT.getDefaultStack();

                    stack.setCount(cyaniteCount);

                    player.getInventory().offerOrDrop(stack);

                    controller.markDirty();

                    t.commit();
                }
            }
        }

        return ActionResult.SUCCESS;
    }
}
