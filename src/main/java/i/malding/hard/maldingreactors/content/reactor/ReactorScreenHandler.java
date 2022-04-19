package i.malding.hard.maldingreactors.content.reactor;

import i.malding.hard.maldingreactors.content.AllScreenTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ReactorScreenHandler extends ScreenHandler {
    public ReactorScreenHandler(int syncId, PlayerInventory inv) {
        super(AllScreenTypes.REACTOR_CONTROLLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory().canPlayerUse(player);
    }
}
