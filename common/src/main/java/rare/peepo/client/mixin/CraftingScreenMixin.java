package rare.peepo.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rare.peepo.Log;
import rare.peepo.client.config.Config;

@Environment(EnvType.CLIENT)
@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin extends HandledScreen<CraftingScreenHandler> implements RecipeBookProvider {
    
    @Shadow
    private RecipeBookWidget recipeBook;

    public CraftingScreenMixin(CraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        // TODO Auto-generated constructor stub
    }
    
    @Inject(method = "init", at = @At("TAIL"))
    public void initialize(CallbackInfo ci) {
        Log.debug("Initializing CraftingScreen");
        if (Config.focusSearchField)
            setFocused(getRecipeBookWidget());
    }

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"))
    public final void on_mouse_clicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        var cs = (CraftingScreen)(Object)this;
        Log.debug(cs);
    }
}
