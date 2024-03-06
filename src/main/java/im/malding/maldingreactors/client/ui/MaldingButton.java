package im.malding.maldingreactors.client.ui;

import im.malding.maldingreactors.client.MaldingTextures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class MaldingButton extends ButtonWidget {

    private static final int SPRITE_WIDTH = 40;
    private static final int SPRITE_HEIGHT = 20;

    private boolean pressed = false;

    public MaldingButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, Supplier::get);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        final int u = 64;
        int v = this.isHovered() ? 132 : 112;
        if (this.pressed) v = 152;

        final int quadrantWidth = this.width / 2 + 1;
        final int quadrantHeight = this.height / 2 + 1;

        context.drawTexture(MaldingTextures.COMMON_UI_ELEMENTS, getX(), getY(), u, v, quadrantWidth, quadrantHeight);
        context.drawTexture(MaldingTextures.COMMON_UI_ELEMENTS, getX() + quadrantWidth - 1, getY(), u + SPRITE_WIDTH - quadrantWidth, v, quadrantWidth, quadrantHeight);

        context.drawTexture(MaldingTextures.COMMON_UI_ELEMENTS, getX(), getY() + quadrantHeight - 1, u, v + SPRITE_HEIGHT - quadrantHeight, quadrantWidth, quadrantHeight);
        context.drawTexture(MaldingTextures.COMMON_UI_ELEMENTS, getX() + quadrantWidth - 1, getY() + quadrantHeight - 1, u + SPRITE_WIDTH - quadrantWidth, v + SPRITE_HEIGHT - quadrantHeight, quadrantWidth, quadrantHeight);
    }

    @Override
    public void onPress() {
        super.onPress();
        this.pressed = !this.pressed;
    }
}
