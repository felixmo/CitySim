import com.google.common.eventbus.EventBus;

/**
 * CSEventBus
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Manages the shared EventBus
 * 
 */

public class CSEventBus  
{
    private static EventBus sharedEventBus = null;
    
    static {
        if (sharedEventBus == null) {
            sharedEventBus = new EventBus("com.felixmo.CitySim.CSEventBus.EventBus");
            
            // Register listeners
            sharedEventBus.register(new ButtonEventListener());
            sharedEventBus.register(new MenuItemEventListener());
        }
    }
    
    public static void post(Object event) {
        sharedEventBus.post(event);
    }
}
