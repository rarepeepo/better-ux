package rare.peepo.client.mixin;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rare.peepo.Log;
import rare.peepo.client.config.Config;
import rare.peepo.client.config.MouseButton;
import rare.peepo.client.gui.IconButtonWidget;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    
    @Shadow
    protected int backgroundWidth;
    @Shadow
    protected int backgroundHeight;
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
            Log.debug("right click");
            var slot = getSlotAt(mouseX, mouseY);
            if (slot != null)
                Log.debug("clicked on slot: " + slot.id);
            else
                close();
        } else {
            if (Config.closeScreenButton.ordinal() != button && Config.closeScreenButton != MouseButton.ANY)
                return;
            if (isClickOutsideBounds(mouseX, mouseY, x, y, button)) {
                Log.debug("Closing dialog");
                close();
            }
        }
    }
    
    List<Widget> buttons = new ArrayList<Widget>();
    
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo callbackinfo) {
        if (client == null || client.player == null)
            return;
      
        if (Config.showSortButton) {
            // Enchantment screen is a tiny bit off.
            var p = IsEnchantmentScreen() ? 1 : 0;
            var b = new IconButtonWidget(
                    this.x + this.backgroundWidth - 18,
                    this.y + (backgroundHeight - 95) + p,
                    "sort", button -> {
                        Log.debug("Clicked sort inventory button");
                    });
            this.addDrawableChild(b);
            buttons.add(b);
        }
        
        if (!isStorageContainer())
            return;
        
        if (Config.showSortButton) {
            var b = new IconButtonWidget(
                    this.x + this.backgroundWidth - 18,
                    this.y + 6,
                    "sort", button -> {
                        Log.debug("Clicked sort container button");
                    });
            this.addDrawableChild(b);
        }

        if (Config.showTakeButton) {
            var b = new IconButtonWidget(
                    this.x + this.backgroundWidth - 29,
                    this.y + (backgroundHeight - 95),
                    "take", button -> {
                        Log.debug("Clicked take button");
                    });
            buttons.add(b);
            this.addDrawableChild(b);
        }
        
        if (Config.showStoreButton) {
            var b = new IconButtonWidget(
                    this.x + this.backgroundWidth - 40,
                    this.y + (backgroundHeight - 95),
                    "store", button -> {
                        Log.debug("Clicked store button");
                    });
            buttons.add(b);
            this.addDrawableChild(b);
        }
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // For the player inventory screen, the button position needs to be adjusted if the
        // recipe book has been toggled.
        for (var i = 0; i < buttons.size(); i++) {
            buttons.get(i).setX(this.x + this.backgroundWidth - 18 -
                    i * (IconButtonWidget.Size + 1));
        }
    }
    
    boolean isStorageContainer() {
        var handler = this.getScreenHandler();
        // Don't know if this can ever even happen, but just to be on the safe side.
        if (handler == null)
            return false;
        // Player inventory screen brought up with 'e'.
        if (handler instanceof PlayerScreenHandler || handler instanceof CraftingScreenHandler)
            return false;
        // Chests or Barrels.
        if (handler instanceof GenericContainerScreenHandler)
            return true;
        // 3 x 9 (Inventory) + 9 (Hotbar) = 36
        final int numPlayerInventorySlots = 36;
        // If we are still here at this point, we just assume something is a storage container
        // if it has at least 9 slots (in addition to the 36 player inventory slots).
        return handler.slots.size() >= numPlayerInventorySlots + 9;
    }
    
    boolean IsEnchantmentScreen() {
        return this.getScreenHandler() instanceof EnchantmentScreenHandler;
    }
}
