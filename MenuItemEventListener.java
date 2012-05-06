import com.google.common.eventbus.Subscribe;

/**
 * MenuItemEventListener
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Listens for menu item events and acts accordingly
 * 
 */

public class MenuItemEventListener  extends CSEventListener
{
    
    @Subscribe
    public void listen(MenuItemEvent event) {
        CSLogger.sharedLogger().finer("\"" + event.message() + "\" was selected.");
    }
}