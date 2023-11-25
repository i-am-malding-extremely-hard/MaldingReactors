package im.malding.maldingreactors.network;

import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
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
}
