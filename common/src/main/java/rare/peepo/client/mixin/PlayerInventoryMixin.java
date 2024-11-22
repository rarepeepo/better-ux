package rare.peepo.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rare.peepo.client.config.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Nameable;

@Environment(value=EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin extends Object implements Inventory, Nameable {
    @Inject(method = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V", at = @At("HEAD"))
    public void onScrollInHotbar(double scrollAmount, CallbackInfo ci) {
        if(!Config.reverseHotbarScroll)
            return;
        var _this = (PlayerInventory)(Object)this;
        var i = (int)Math.signum(scrollAmount);
        _this.selectedSlot += i * 2;
    }
}
