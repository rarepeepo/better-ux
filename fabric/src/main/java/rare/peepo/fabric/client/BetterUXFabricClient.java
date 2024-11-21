package rare.peepo.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import rare.peepo.client.BetterUXClient;

public final class BetterUXFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BetterUXClient.onInitializeClient();
    }
}
