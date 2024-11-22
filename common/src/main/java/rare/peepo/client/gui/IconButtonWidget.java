package rare.peepo.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import rare.peepo.BetterUX;

@Environment(value=EnvType.CLIENT)
public class IconButtonWidget extends TexturedButtonWidget {
    public static final int Size = 10;

    public IconButtonWidget(int x, int y, String name, ButtonWidget.PressAction pressAction) {
        super(x, y, Size, Size,
                new ButtonTextures(
                        new Identifier(BetterUX.ID, name),
                        new Identifier(BetterUX.ID, name + "_hover")),
                pressAction);
        var s = Text.translatable(BetterUX.ID + "." + name + "Button.tooltip");
        this.setTooltip(Tooltip.of(s));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier identifier = this.textures.get(this.isNarratable(), this.isHovered());
        context.drawGuiTexture(identifier, this.getX(), this.getY(), this.width, this.height);
    }
}
