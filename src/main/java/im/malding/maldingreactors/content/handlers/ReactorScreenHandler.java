package im.malding.maldingreactors.content.handlers;

import im.malding.maldingreactors.content.logic.HeatInformation;
import im.malding.maldingreactors.content.reactor.ReactorControllerBlockEntity;
import io.wispforest.owo.client.screens.ScreenUtils;
import io.wispforest.owo.client.screens.SyncedProperty;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class ReactorScreenHandler extends ScreenHandler {

    public final ReactorControllerBlockEntity reactorController;

    public final SyncedProperty<HeatInformation> heatInfo;

    public ReactorScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(MaldingScreenHandlers.REACTOR_CONTROLLER, syncId);

        this.reactorController = (ReactorControllerBlockEntity) inv.player.getWorld().getBlockEntity(buf.readBlockPos());

        heatInfo = this.createProperty(HeatInformation.class, new HeatInformation(0, 0, 0));
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();

        var simulation = this.reactorController.getSimulation();

        if(simulation == null) return;

        this.heatInfo.set(simulation.currentInfo);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, 0);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
