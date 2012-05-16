import java.util.logging.*;

/**
 * Write a description of class CSLogger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CSLogger
{
    private static Logger sharedLogger;              // Shared logger
    private static final Level LEVEL = Level.FINER;  // Only events AT or ABOVE this level will be logged & shown in the console

    static {

        if (sharedLogger == null) {
            // Configure logging
            
            LogManager manager = LogManager.getLogManager();
            manager.reset();

            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(LEVEL);
            handler.setFormatter(new LogFormatter());

            sharedLogger = Logger.getLogger("com.felixmo.CitySim.logger");
            sharedLogger.addHandler(handler);
            sharedLogger.setLevel(LEVEL);
        }
    }

    // Logging is done via a global instance of 'Logger' rather than through CSLogger so that the caller can be logged as well
    public static Logger sharedLogger() {
        return sharedLogger;
    }
}