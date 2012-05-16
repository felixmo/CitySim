import com.google.common.eventbus.Subscribe;

/**
 * Write a description of class TileSelectorEventListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelectorEventListener extends CSEventListener
{

    @Subscribe
    public void listen(TileSelectorEvent event) {
        CSLogger.sharedLogger().finer("\"" + event.message() + "\" was selected.");

        Road.setActiveType(event.type());
    }
}
