package rare.peepo.client;

import eu.midnightdust.lib.config.MidnightConfig;
import rare.peepo.BetterUX;
import rare.peepo.Log;
import rare.peepo.client.config.Config;

public final class BetterUXClient {
    // This code runs only on the client.
    public static void onInitializeClient() {
        Log.info("Initializing BetterUX (client)");
        MidnightConfig.init(BetterUX.ID, Config.class);
    }
}
