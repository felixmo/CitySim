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
            Map.getInstance().selection().setUnacceptedTypes( new int[]{ Tile.WATER } );
            Map.getInstance().selection().setSize(ResidentialZone.SIZE_WIDTH, ResidentialZone.SIZE_HEIGHT);
            Zone.setPendingOp(ResidentialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as residential. Press 'ESC' when done."));
        }
        else if (event.message().equals(MenuItemEvent.INDUSTRIAL)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(IndustrialZone.SIZE_WIDTH, IndustrialZone.SIZE_HEIGHT);
            Zone.setPendingOp(IndustrialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as industrial. Press 'ESC' when done."));
        }
        else if (event.message().equals(MenuItemEvent.COMMERCIAL)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(CommercialZone.SIZE_WIDTH, CommercialZone.SIZE_HEIGHT);
            Zone.setPendingOp(CommercialZone.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to zone as commercial. Press 'ESC' when done."));
        }
        // * TRANSPORTATION *
        else if (event.message().equals(MenuItemEvent.STREETS)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            // TODO: unaccpeted zone
            Map.getInstance().selection().setSize(Street.SIZE_WIDTH, Street.SIZE_HEIGHT);    
            Road.setPendingOp(Street.TYPE_ID);
            City.getInstance().setTileSelector(new TileSelector( new int[]{ 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 } ));
        }
        // * TOOLS *
        else if (event.message().equals(MenuItemEvent.BULLDOZER)) {
            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setUnacceptedTypes( new int[]{ Tile.WATER } );
            Map.getInstance().selection().setSize(Bulldozer.SIZE_WIDTH, Bulldozer.SIZE_HEIGHT);
            Tool.setPendingOp(Bulldozer.TYPE_ID);
            City.getInstance().setHint(new Hint("Select the areas you wish to bulldoze. Press 'ESC' when done."));
        }
    }
}