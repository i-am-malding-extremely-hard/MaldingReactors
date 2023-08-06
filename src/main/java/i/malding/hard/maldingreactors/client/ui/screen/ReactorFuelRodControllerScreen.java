package i.malding.hard.maldingreactors.client.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import i.malding.hard.maldingreactors.client.MaldingTextures;
import i.malding.hard.maldingreactors.content.handlers.ReactorFuelRodControllerScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ReactorFuelRodControllerScreen extends HandledScreen<ReactorFuelRodControllerScreenHandler> {
    public ReactorFuelRodControllerScreen(ReactorFuelRodControllerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(MaldingTextures.REACTOR_BACKGROUND, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
