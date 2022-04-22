package i.malding.hard.maldingreactors.content.handlers;

import i.malding.hard.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class ReactorScreenHandler extends ScreenHandler {

    public final ReactorControllerBlockEntity reactorControllerBlockEntity;

    public ReactorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(MaldingScreenHandlers.REACTOR_CONTROLLER, syncId);
        this.reactorControllerBlockEntity = (ReactorControllerBlockEntity) inv.player.getWorld().getBlockEntity(buf.readBlockPos());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
