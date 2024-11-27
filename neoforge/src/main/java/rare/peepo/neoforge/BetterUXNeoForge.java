package rare.peepo.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import rare.peepo.BetterUX;
import rare.peepo.client.BetterUXClient;

@Mod(BetterUX.ID)
public final class BetterUXNeoForge {
    public BetterUXNeoForge() {
        // Run our common setup.
        BetterUX.onInitialize();
        
        if (FMLEnvironment.dist == Dist.CLIENT)
          BetterUXClient.onInitializeClient();
    }
}
