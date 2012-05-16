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

public class MenuItemEventListener extends CSEventListener
{

    @Subscribe
    public void listen(MenuItemEvent event) {
        CSLogger.sharedLogger().finer("\"" + event.message() + "\" was selected.");

        // * ZONING *
        if (event.message().equals(MenuItemEvent.RESIDENTIAL)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(ResidentialZone.SIZE);
            Zone.setPendingOp(ResidentialZone.ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as residential. Press 'ESC' when done."));
        }
        else if (event.message().equals(MenuItemEvent.INDUSTRIAL)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(IndustrialZone.SIZE);
            Zone.setPendingOp(IndustrialZone.ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as industrial. Press 'ESC' when done."));
        }
        else if (event.message().equals(MenuItemEvent.COMMERCIAL)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(CommercialZone.SIZE);
            Zone.setPendingOp(CommercialZone.ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as commercial. Press 'ESC' when done."));
        }
        // * TRANSPORTATION *
        else if (event.message().equals(MenuItemEvent.STREETS)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(Street.SIZE);    
            Road.setPendingOp(Street.ID);
            City.getInstance().setTileSelector(new TileSelector( new int[]{ 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 } ));
        }
    }
}