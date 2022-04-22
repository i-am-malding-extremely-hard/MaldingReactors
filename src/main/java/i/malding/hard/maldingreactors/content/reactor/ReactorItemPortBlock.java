package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.MaldingFluids;
import i.malding.hard.maldingreactors.content.MaldingItems;
import i.malding.hard.maldingreactors.data.MaldingTags;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class ReactorItemPortBlock extends ReactorSingleFaceBlock {

    public ReactorItemPortBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorItemPortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> ((ReactorItemPortBlockEntity) blockEntity).tick(world1, pos, state1);
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
        ReactorBaseBlockEntity portBlockEntity = (ReactorBaseBlockEntity) world.getBlockEntity(pos);

        return portBlockEntity != null && portBlockEntity.isFullMultipartStructure() ? super.createScreenHandlerFactory(state, world, pos) : null;
    }

    //--------------------------------------------------------------------------------------------------------------------

    public static ActionResult insertFuelAmount(World world, BlockPos controllerPos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isIn(MaldingTags.REACTOR_FUEL)) {
            if (!world.isClient) {
                if (!(world.getBlockEntity(controllerPos) instanceof ReactorControllerBlockEntity controller)) {
                    return ActionResult.PASS;
                }

                FluidTank fuelTank = controller.getFuelTank();
                FluidTank wasteTank = controller.getWasteTank();

                int stackCount = stack.getCount();

                long amountOfDropletsFromStack = stackCount * FluidConstants.INGOT;

                try (Transaction t = Transaction.openOuter()) {
                    long amountAllowedInsertable = wasteTank.simulateInsert(FluidVariant.of(MaldingFluids.COPIUM.still()), amountOfDropletsFromStack, t);

                    if (amountAllowedInsertable > 0) {
                        long actuallyInsertable = fuelTank.simulateInsert(FluidVariant.of(MaldingFluids.COPIUM.still()), amountAllowedInsertable, t);

                        int amountUsed = MathHelper.floor(actuallyInsertable / (float) FluidConstants.INGOT);

                        if (actuallyInsertable > 0 && amountUsed > 0) {
                            fuelTank.insert(FluidVariant.of(MaldingFluids.COPIUM.still()), amountUsed * FluidConstants.INGOT, t);

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
        if (!(world.getBlockEntity(controllerPos) instanceof ReactorControllerBlockEntity controller)) return ActionResult.PASS;

        if (!world.isClient) {
            FluidTank wasteTank = controller.getWasteTank();

            try (Transaction t = Transaction.openOuter()) {
                long amountOfWasteExtractable = wasteTank.simulateExtract(FluidVariant.of(MaldingFluids.MALDING_COPIUM.still()), wasteTank.getCapacity(), t);

                int maldingCopium = MathHelper.floor(amountOfWasteExtractable / (float) FluidConstants.INGOT);

                long amountOfWasteExtracted = wasteTank.extract(FluidVariant.of(MaldingFluids.MALDING_COPIUM.still()), maldingCopium * FluidConstants.INGOT, t);

                if (amountOfWasteExtracted != 0 && maldingCopium != 0) {
                    ItemStack stack = MaldingItems.MALDING_COPIUM_INGOT.getDefaultStack();

                    stack.setCount(maldingCopium);

                    player.getInventory().offerOrDrop(stack);

                    controller.markDirty();

                    t.commit();
                }
            }
        }

        return ActionResult.SUCCESS;
    }
}
