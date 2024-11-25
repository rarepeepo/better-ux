package rare.peepo.client.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    @Entry
    public static boolean closeOnRightClick = true;
    
    @Entry
    public static MouseButton closeScreenButton = MouseButton.ANY;
    
    @Entry
    public static boolean reverseHotbarScroll = false;
    
    @Entry
    public static boolean focusSearchField = true;
    
    @Entry
    public static boolean showSortButton = true;
    
    @Entry
    public static boolean showTakeButton = true;
    
    @Entry
    public static boolean showStoreButton = true;
}
