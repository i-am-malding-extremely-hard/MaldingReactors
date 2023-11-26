package im.malding.maldingreactors.network;

import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorFuelRodControllerBlockEntity;
import im.malding.maldingreactors.content.reactor.ReactorItemPortBlockEntity;
import io.wispforest.owo.network.ServerAccess;
import net.minecraft.util.math.BlockPos;

public class MaldingReactorPackets {
    public record ReactorToggle(BlockPos pos, boolean toggleState){
        public ReactorToggle(ReactorControllerBlockEntity entity){
            this(entity.getPos(), !entity.isReactorActive());
        }

        public static void tryActivation(ReactorToggle packet, ServerAccess access){
            var world = access.player().getWorld();

            if(world.getBlockEntity(packet.pos()) instanceof ReactorControllerBlockEntity reactorController){
                reactorController.attemptToggle(packet.toggleState());
            }
        }
    }

    public record ControlRodAdjustment(BlockPos pos, int reactionRate){
        public static void adjustRate(ControlRodAdjustment packet, ServerAccess access){
            var world = access.player().getWorld();

            if(world.getBlockEntity(packet.pos()) instanceof ReactorFuelRodControllerBlockEntity reactorFuelRodController){
                reactorFuelRodController.setReactionRate(packet.reactionRate());
            }
        }
    }

    public record ItemPortExportWaste(BlockPos pos){
        public static void exportWaste(ItemPortExportWaste packet, ServerAccess access){
            var world = access.player().getWorld();

            if(world.getBlockEntity(packet.pos()) instanceof ReactorItemPortBlockEntity reactorItemPort){
                reactorItemPort.attemptWasteExport();
            }
        }
    }

    public record ToggleItemPortExport(BlockPos pos, boolean autoExport){
        public ToggleItemPortExport(ReactorItemPortBlockEntity blockEntity){
            this(blockEntity.getPos(), !blockEntity.autoExportWaste);
        }

        public static void toggleExport(ToggleItemPortExport packet, ServerAccess access){
            var world = access.player().getWorld();

            if(world.getBlockEntity(packet.pos()) instanceof ReactorItemPortBlockEntity reactorItemPort){
                reactorItemPort.autoExportWaste(packet.autoExport());
            }
        }
    }
}
