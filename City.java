import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Timer;
import java.io.File;
import java.util.LinkedHashMap;

/**
 * City
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * 'City' acts as a container for the map
 *
 */

public class City extends World
{

    private Map map;    // Map of the city
    private Date date;
    private HUD hud;
    private Minimap_Viewport minimap_viewport;
    private DataSource dataSource;

    public City() {

        super(1024, 768, 1, false);     // Create a 1024 x 768 'World' with a cell size of 1px that does not restrict actors to the world boundary

        setPaintOrder(Toolbar.class, Selection.class, Label.class, Minimap_Viewport.class, Minimap.class, HUD.class, Map.class);

        dataSource = new DataSource("test");

        if (dataSource.dbIsNew()) {

            // Initialize values for insertion

            LinkedHashMap mapMetadata = new LinkedHashMap();
            mapMetadata.put("name", "Toronto"); // FOR TESTING PURPOSES
            dataSource.insertMapMetadata(mapMetadata);

            LinkedHashMap cityStats = new LinkedHashMap();
            cityStats.put("days", 0);
            cityStats.put("months", 1);
            cityStats.put("years", 0);
            cityStats.put("population", 0);
            dataSource.insertCityStats(cityStats);
        }

        // Start tracking time from the last saved time in DB
        LinkedHashMap cityStats = dataSource.cityStats();
        date = new Date(this, (Integer)cityStats.get("days"), (Integer)cityStats.get("months"), (Integer)cityStats.get("years"));

        // Create and add a new map for the city
        map = new Map(dataSource);
        addObject(map, 512, 333);   // Arbitrary values to place map at origin (top-left corner)

        // Create and add HUD
        hud = new HUD(dataSource);
        addObject(hud, 512, 653);
        
        minimap_viewport = new Minimap_Viewport(new Point(0, 0));
        addObject(minimap_viewport, 112, 658);
    }

    // Called when game is started
    public void started() {
        System.out.println("Game has started...");

        if (!dataSource.connectionIsOpen()) {
            dataSource.resumeConnection();
        }

        // TO DO: start timer when game has actually started (i.e. not in menu)
        date.start();
    }

    // Called when game is paused in Greenfoot
    public void stopped() {
        System.out.println("Game has stopped.");

        // TO DO: pause timer when in menu
        date.stop();

        dataSource.closeConnection();
    }

    // EVENTS
    
    // Call when time has incremented by 1 second
    public void didIncrementDate() {
        dataSource.updateCityStats(currentCityStats());
        hud.refresh(valuesForHUD());   
    }

    public void didMoveViewportTo(Point location) {
        map.viewportDidMoveTo(location);
    }
    
    public void didMoveMapTo(Point location) {
        minimap_viewport.didMoveViewportToCell(location);
    }
    
    // HELPERS

    private LinkedHashMap valuesForHUD() {

        LinkedHashMap values = new LinkedHashMap();

        values.put("name", "Toronto");  // TESTING
        values.put("population", 0);    // TESTING
        values.put("date", date.toString());

        return values;
    }

    private LinkedHashMap currentCityStats() {

        LinkedHashMap stats = new LinkedHashMap();

        stats.put("days", date.days());
        stats.put("months", date.months());
        stats.put("years", date.years());
        stats.put("population", 0);

        return stats;
    }
}
