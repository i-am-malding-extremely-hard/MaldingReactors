package i.malding.hard.maldingreactors.content.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class ReactorScreenHandler extends ScreenHandler {
    public ReactorScreenHandler(int syncId, PlayerInventory inv) {
        super(AllScreenHandlerTypes.REACTOR_CONTROLLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
