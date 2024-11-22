package rare.peepo.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rare.peepo.Log;
import rare.peepo.client.config.*;

import org.spongepowered.asm.mixin.injection.At;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeDisplayListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.screen.AbstractRecipeScreenHandler;

@Environment(value=EnvType.CLIENT)
@Mixin(RecipeBookWidget.class)
public abstract class RecipeBookWidgetMixin implements RecipeGridAligner<Ingredient>, Drawable, Element, Selectable,
    RecipeDisplayListener {
    @Shadow
    private TextFieldWidget searchField;
    
    @Shadow
    private boolean searching;

    @Inject(method = "initialize(IILnet/minecraft/client/MinecraftClient;ZLnet/minecraft/screen/AbstractRecipeScreenHandler;)V",
            at = @At("TAIL"))
    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow,
            AbstractRecipeScreenHandler<?> craftingScreenHandler, CallbackInfo ci) {
        Log.info("Initializing RecipeBookWidget");
        var _this = (RecipeBookWidget)(Object)this;
        if (_this.isOpen() && Config.focusSearchField) {
            _this.setFocused(true);
            searchField.setFocused(true);
          
            searching = true;
        }
    }
}
