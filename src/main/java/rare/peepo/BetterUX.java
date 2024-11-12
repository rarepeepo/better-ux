package rare.peepo;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.midnightdust.lib.config.MidnightConfig;

public class BetterUX implements ModInitializer {
    public static final String ID = "better-ux";
    
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    static final Logger LOGGER = LoggerFactory.getLogger(ID);
    
    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        MidnightConfig.init(ID, Config.class);
    }
}