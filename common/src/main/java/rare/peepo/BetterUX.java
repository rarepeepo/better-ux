package rare.peepo;

import rare.peepo.network.Network;

public final class BetterUX {
    public static final String ID = "betterux";

    // This code runs on _both_ server and client. Note that if the server and client
    // happen to be the same (i.e. localhost/singleplayer) this method will only be
    // called once.
    public static void onInitialize() {
        // Write common init code here.
        Log.info("Initializing BetterUX");
        
        Network.init();
    }
}
