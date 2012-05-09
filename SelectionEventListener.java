import com.google.common.eventbus.Subscribe;
import greenfoot.*;
import java.awt.Point;

/**
 * Write a description of class SelectionEventListener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionEventListener extends CSEventListener
{
    
    @Subscribe
    public void listen(SelectionEvent event) {
        CSLogger.sharedLogger().finer("Message received: " + event.message());

        if (event.message().equals(SelectionEvent.TILES_SELECTED)) {
            
            Zone.zoneTiles(event.tiles());
        }
    }
}
