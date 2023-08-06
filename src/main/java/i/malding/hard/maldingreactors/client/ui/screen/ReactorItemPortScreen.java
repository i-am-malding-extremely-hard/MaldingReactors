package i.malding.hard.maldingreactors.client.ui.screen;

import i.malding.hard.maldingreactors.client.MaldingTextures;
import i.malding.hard.maldingreactors.content.handlers.ReactorItemPortScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ReactorItemPortScreen extends HandledScreen<ReactorItemPortScreenHandler> {

    public ReactorItemPortScreen(ReactorItemPortScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(MaldingTextures.REACTOR_BACKGROUND, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
