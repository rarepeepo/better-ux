package rare.peepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {
    static final Logger logger = LoggerFactory.getLogger(BetterUX.ID);
    
    public static void info(Object o) {
            logger.info("[{}] {}", BetterUX.ID, o);
    }
    
    public static void info(String format, Object... arguments) {
        if (!logger.isInfoEnabled())
            return;
        logger.info(
            String.format("[%s] %s", BetterUX.ID, format), arguments
        );
    }
    
    public static void warn(Object o) {
        logger.warn("[{}] {}", BetterUX.ID, o);
    }
    
    public static void warn(String format, Object... arguments) {
        if (!logger.isWarnEnabled())
            return;
        logger.warn(
            String.format("[%s] %s", BetterUX.ID, format), arguments
        );
    }
    
    public static void debug(Object o) {
        logger.debug("[{}] {}", BetterUX.ID, o);
    }
    
    public static void debug(String format, Object... arguments) {
        if (!logger.isDebugEnabled())
            return;
        logger.debug(
            String.format("[%s] %s", BetterUX.ID, format), arguments
        );
    }
}
