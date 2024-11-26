package rare.peepo.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import rare.peepo.BetterUX;

@Environment(EnvType.CLIENT)
public class IconButtonWidget extends ButtonWidget {
    public static final int Size = 10;
    final Identifier texture_1;
    final Identifier texture_2;

    public IconButtonWidget(int x, int y, String name, ButtonWidget.PressAction pressAction) {
        super(x, y, Size, Size, ScreenTexts.EMPTY, pressAction, DEFAULT_NARRATION_SUPPLIER);
        
        texture_1 = new Identifier(BetterUX.ID, "textures/gui/" + name + ".png");
        texture_2 = new Identifier(BetterUX.ID, "textures/gui/" + name + "_hover.png");
        
        var s = Text.translatable(BetterUX.ID + "." + name + "Button.tooltip");
        this.setTooltip(Tooltip.of(s));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(this.isHovered() ? texture_2 : texture_1, this.getX(), this.getY(),
                0, 0, 0, this.width, this.height, this.width, this.height);
    }
}
