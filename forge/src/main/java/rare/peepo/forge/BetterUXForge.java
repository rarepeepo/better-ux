package rare.peepo.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rare.peepo.client.BetterUXClient;
import rare.peepo.BetterUX;

@Mod(BetterUX.ID)
public final class BetterUXForge {
    public BetterUXForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(BetterUX.ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        BetterUX.onInitialize();
        
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BetterUXClient::onInitializeClient);
    }
}
