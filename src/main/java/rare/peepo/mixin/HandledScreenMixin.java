package rare.peepo.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rare.peepo.BetterUX;
import rare.peepo.Config;
import rare.peepo.MouseButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

@Environment(value=EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    private static final Logger log = BetterUX.getLogger();
    
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    @Shadow
    protected abstract boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button);
    @Shadow
    protected abstract Slot getSlotAt(double x, double y);

    protected HandledScreenMixin(Text title) {
        super(title);
        // TODO Auto-generated constructor stub
    }

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"))
    public final void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        if (Config.closeOnRightClick && button == MouseButton.RIGHT.ordinal()) {
            log.info("right click");
            var slot = getSlotAt(mouseX, mouseY);
            if (slot != null)
                log.info("clicked on slot: " + slot.id);
            else
                close();
        } else {
            if (Config.closeScreenButton.ordinal() != button && Config.closeScreenButton != MouseButton.ANY)
                return;
            if (isClickOutsideBounds(mouseX, mouseY, x, y, button)) {
                log.info("Closing dialog");
                close();
            }
        }
    }
}
