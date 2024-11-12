package rare.peepo;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    @Entry
    public static boolean closeOnRightClick = true;
    
    @Entry
    public static MouseButton closeScreenButton = MouseButton.ANY;
    
    @Entry
    public static boolean reverseHotbarScroll = true;
    
    @Entry
    public static boolean focusSearchField = true;
}
