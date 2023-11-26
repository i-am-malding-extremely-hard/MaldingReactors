package im.malding.maldingreactors.content.reactor;

import im.malding.maldingreactors.content.MaldingBlockEntities;
import im.malding.maldingreactors.util.BlockEntityUtils;
import im.malding.maldingreactors.util.GuiUtil;
import im.malding.maldingreactors.content.logic.ReactorValidator;
import io.wispforest.owo.network.ClientAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ReactorControllerBlock extends ReactorSingleFaceBlock {

    public ReactorControllerBlock(Settings settings) {
        super(settings, ReactorControllerBlockEntity::new);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ReactorControllerBlockEntity controllerBlockEntity = ((ReactorControllerBlockEntity) world.getBlockEntity(pos));

        if (!world.isClient && hand == Hand.MAIN_HAND && !controllerBlockEntity.isValid()) {
            ReactorValidator validator = controllerBlockEntity.getValidator();

            boolean isMultipart = validator.validateReactor(state);

            if(isMultipart){
                controllerBlockEntity.rodControllers = new ArrayList<>(validator.rodControllers);
                controllerBlockEntity.fuelRods = new ArrayList<>(validator.fuelRods);

                controllerBlockEntity.itemPorts = new ArrayList<>(validator.itemPorts);
                controllerBlockEntity.powerPorts = new ArrayList<>(validator.powerPorts);
            } else {
                controllerBlockEntity.rodControllers.clear();
                controllerBlockEntity.fuelRods.clear();

                controllerBlockEntity.itemPorts.clear();
                controllerBlockEntity.powerPorts.clear();
            }

            controllerBlockEntity.setValid(isMultipart);
            controllerBlockEntity.markDirty();
        }

        final var factory = state.createScreenHandlerFactory(world, pos);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            GuiUtil.openGui(serverPlayer, factory, (buf -> buf.writeBlockPos(pos)));
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    public <T extends BlockEntity> BlockEntityType<T> getType() {
        return (BlockEntityType<T>) MaldingBlockEntities.REACTOR_CONTROLLER;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (BlockEntityTicker<T>) BlockEntityUtils.createBlockEntityTicker(this::getType, type);
    }

    public record MultiBlockUpdatePacket(BlockPos pos, boolean isMultiPart) {

        public static void setControllersMultipartState(MultiBlockUpdatePacket packet, ClientAccess access) {
            ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) access.runtime().world.getBlockEntity(packet.pos());

            if (controllerBlockEntity != null) {
                controllerBlockEntity.setValid(packet.isMultiPart());
            }
        }
    }
}
