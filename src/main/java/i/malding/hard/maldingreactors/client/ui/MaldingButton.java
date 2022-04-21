package i.malding.hard.maldingreactors.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import i.malding.hard.maldingreactors.client.MaldingTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MaldingButton extends ButtonWidget {

    private static final int SPRITE_WIDTH = 40;
    private static final int SPRITE_HEIGHT = 20;

    private boolean pressed = false;

    public MaldingButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        final int u = 64;
        int v = this.isHovered() ? 132 : 112;
        if (this.pressed) v = 152;

        final int quadrantWidth = this.width / 2 + 1;
        final int quadrantHeight = this.height / 2 + 1;

        RenderSystem.setShaderTexture(0, MaldingTextures.COMMON_UI_ELEMENTS);

        this.drawTexture(matrices, x, y, u, v, quadrantWidth, quadrantHeight);
        this.drawTexture(matrices, x + quadrantWidth - 1, y, u + SPRITE_WIDTH - quadrantWidth, v, quadrantWidth, quadrantHeight);

        this.drawTexture(matrices, x, y + quadrantHeight - 1, u, v + SPRITE_HEIGHT - quadrantHeight, quadrantWidth, quadrantHeight);
        this.drawTexture(matrices, x + quadrantWidth - 1, y + quadrantHeight - 1, u + SPRITE_WIDTH - quadrantWidth, v + SPRITE_HEIGHT - quadrantHeight, quadrantWidth, quadrantHeight);
    }

    @Override
    public void onPress() {
        super.onPress();
        this.pressed = !this.pressed;
    }
}
