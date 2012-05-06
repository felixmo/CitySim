import java.util.logging.*;

/**
 * Write a description of class CSLogger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSLogger
{
    private static Logger sharedLogger;     // Shared logger
    private static final Level level = Level.FINER;

    static {

        if (sharedLogger == null) {

            LogManager manager = LogManager.getLogManager();
            manager.reset();

            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(level);
            handler.setFormatter(new LogFormatter());

            sharedLogger = Logger.getLogger("com.felixmo.CitySim.logger");
            sharedLogger.addHandler(handler);
            sharedLogger.setLevel(level);    // Only show events: Finer +
        }
    }

    public static Logger sharedLogger() {
        return sharedLogger;
    }
}
