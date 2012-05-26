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
        CSLogger.sharedLogger().debug("\"" + event.message() + "\" was selected.");

        if (Road.pendingOp() > 0) {
            Road.setActiveType(event.type());
        }
        else if (Power.pendingOp() > 0) {
            Power.setActiveType(event.type());
        }
    }
}
