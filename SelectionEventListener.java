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

        if (event.message().equals(SelectionEvent.TILES_SELECTED_FOR_ZONING)) {

            CSLogger.sharedLogger().info("Pending zone op: " + Zone.pendingOp());

            switch (Zone.pendingOp()) {
                case ResidentialZone.ID: ResidentialZone.zoneTiles(event.tiles());  
                                         break;
                case IndustrialZone.ID: IndustrialZone.zoneTiles(event.tiles());
                                        break;
                case CommercialZone.ID: CommercialZone.zoneTiles(event.tiles());
                                        break;
                default: break;
            }
        }
        else if (event.message().equals(SelectionEvent.TILE_SELECTED_FOR_ROAD)) {
            
            switch (Road.pendingOp()) {
                case Street.ID: Street.buildStreet(event.tile(), Road.activeType());
                                break;
                                
                default: break;
            }
        }
    }
}
