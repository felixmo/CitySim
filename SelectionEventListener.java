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

        if (event.message().equals(SelectionEvent.TILE_SELECTED)) {
            // Procedure for tile selection
            Point pos = event.tile().position();
            System.out.println("Tile (" + pos.x + ", " + pos.y + ") was selected.");
        }
    }
}
