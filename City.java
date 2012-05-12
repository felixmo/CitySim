import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.io.File;
import java.awt.Point;

/**
 * City
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * 'City' acts as a container for all objects / "Actors" in the game (incl. map, HUD, etc.)
 *
 */

public class City extends World
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * STATIC VARIABLES *
     */
    private static City instance;

    /*
     * CONSTANTS *
     */
    private final int FREQ_WRITE = 10;               // # of secs before updated data is written to DB
    // Initial values
    private final int INITIAL_CASH = 100000;         // amount of $ to start with
    private final int INITIAL_POP = 0;               // # of people in the population to start with
    private final int INITIAL_DATE_DAYS = 1;         
    private final int INITIAL_DATE_MONTHS = 1;
    private final int INITIAL_DATE_YEARS = 0;

    /*
     * REFERENCES *
     */
    private Map map;                                // Map of the city
    private Date date;                              // Current date (in-game) / time elapsed
    private HUD hud;                                // HUD display containing game info and controls
    private Minimap_Viewport minimap_viewport;      // Representation of the viewport in the minimap
    private Cash cash;                              // Current avaliable cash (in-game)
    private MenuBar menuBar;                        // Menu bar containing game controls
    private Hint hint;

    /*
     * INSTANCE VARIABLES *
     */
    private int writeCountdown = 0;                  // Countdown until updated data is written to DB

    // ---------------------------------------------------------------------------------------------------------------------

    public City() {

        super(1024, 768, 1, false);     // Create a 1024 x 768 'World' with a cell size of 1px that does not restrict 'actors' to the world boundary
        
        // Set Greenfoot paint order to ensure that Actors are layered properly
        setPaintOrder(Hint.class, MenuItem.class, Menu.class, MenuBarItem.class, MenuBar.class, Label.class, Minimap_Viewport.class, Minimap.class, HUD.class, Selection.class, Map.class);

        // FOR TESTING ONLY 
        // Delete the DB so that map re-generates each run
//         new File("maps/test.db").delete();

        // Configure data source
        Data.setDataSource(new DataSource("test"));    // FOR TESTING PURPOSES

        // If the data source has just created a new DB (b/c it did not exist), seed it with initial stats. and metadata
        if (Data.dbIsNew()) {

            // Metadata
            LinkedHashMap mapMetadata = new LinkedHashMap(1);
            mapMetadata.put(Data.METADATA_NAME, "Toronto"); // FOR TESTING PURPOSES
            Data.insertMapMetadata(mapMetadata);

            // City stats
            LinkedHashMap cityStats = new LinkedHashMap(5);
            cityStats.put(Data.CITYSTATS_DAYS, INITIAL_DATE_DAYS);
            cityStats.put(Data.CITYSTATS_MONTHS, INITIAL_DATE_MONTHS);
            cityStats.put(Data.CITYSTATS_YEARS, INITIAL_DATE_YEARS);
            cityStats.put(Data.CITYSTATS_POPULATION, INITIAL_POP);
            cityStats.put(Data.CITYSTATS_CASH, INITIAL_CASH);
            Data.insertCityStats(cityStats);
        }

        // Resume tracking date from last saved date
        LinkedHashMap cityStats = Data.cityStats();
        date = new Date((Integer)cityStats.get(Data.CITYSTATS_DAYS), (Integer)cityStats.get(Data.CITYSTATS_MONTHS), (Integer)cityStats.get(Data.CITYSTATS_YEARS));

        // Create and add a new map for the city
        map = new Map();
        addObject(map, 512, 333);

        // Create and add HUD
        hud = new HUD();
        addObject(hud, 512, 653);

        // Create and add the representation of the viewport into the minimap
        minimap_viewport = new Minimap_Viewport(new Point(0, 0));
        addObject(minimap_viewport, 112, 658);

        // Create and add the menubar
        menuBar = new MenuBar();
        addObject(menuBar, 512, 14);

        // Menu bar items
        ArrayList<String> menuBarItems = new ArrayList(4);
        menuBarItems.add("Zone");
        menuBarItems.add("Roads");
        menuBarItems.add("Power");
        menuBarItems.add("Protection");
        menuBar.setItems(menuBarItems);

        // Menu items
        /*
         * NOTE *
         * Menu items need to be declared in 'MenuItemEvent' as well and implemented in 'MenuItemEventListener'.
         */
        ArrayList<String> zoneItems = new ArrayList(3);
        zoneItems.add("Residential");
        zoneItems.add("Commercial");
        zoneItems.add("Industrial");
        menuBar.setMenuItemsForItem("Zone", zoneItems);

        // Initalize the cash store from the last known value in the DB
        cash = new Cash((Integer)cityStats.get(Data.CITYSTATS_CASH));

        instance = this;
    }

    public static City getInstance() {
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * EVENTS *
     */

    // Called when game is started
    public void started() {
        CSLogger.sharedLogger().info("Game has started...");

        if (!Data.connectionIsOpen()) {
            Data.resumeConnection();
        }

        // TO DO: start timer when game has actually started (i.e. not in menu)
        date.start();
    }

    // Called when game is paused in Greenfoot
    public void stopped() {
        CSLogger.sharedLogger().info("Game has stopped.");

        // TO DO: pause timer when in menu
        date.stop();

        Data.closeConnection();
    }

    // Called when date is incremented (every 1 sec)
    public void didIncrementDate() {

        // Write to DB
        writeCountdown++;
        if (writeCountdown == FREQ_WRITE) {
            Data.updateCityStats(currentCityStats());  
            writeCountdown = 0;
            // Run Java garbage collector to cleanup
            System.gc();
        }

        // Refresh values for HUD every 1 sec
        hud.refresh(valuesForHUD());
    }

    // Called when the minimap viewport has been moved (i.e. minimap has been clicked on)
    public void didMoveViewportTo(int x, int y) {
        // Move the map
        map.viewportDidMoveTo(x, y);
    }

    // Called when the map is moved
    public void didMoveMapTo(int x, int y) {
        // Move the representation of the viewport in the minimap
        minimap_viewport.didMoveViewportToCell(x, y);
    }
    
    public void removeHint() {
        removeObject(this.hint);
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * HELPERS *
     */

    // Returns formatted values for the HUD labels
    private LinkedHashMap valuesForHUD() {

        LinkedHashMap values = new LinkedHashMap(4);

        values.put(HUD.NAME, "Toronto");  // TESTING
        values.put(HUD.POPULATION, 0);    // TESTING
        values.put(HUD.DATE, date.toString());
        values.put(HUD.CASH, cash.toString());

        return values;
    }

    // Returns the city stats. w/o formatting (i.e. for writing the values to the DB)
    private LinkedHashMap currentCityStats() {

        LinkedHashMap stats = new LinkedHashMap(5);

        stats.put(Data.CITYSTATS_DAYS, date.days());
        stats.put(Data.CITYSTATS_MONTHS, date.months());
        stats.put(Data.CITYSTATS_YEARS, date.years());
        stats.put(Data.CITYSTATS_POPULATION, 0);
        stats.put(Data.CITYSTATS_CASH, cash.value());

        return stats;
    }
    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * ACCESSORS *
     */
    
    public Hint hint() {
        return this.hint;
    }
    
    public void setHint(Hint aHint) {
        this.hint = aHint;
        addObject(this.hint, Hint.ORIGIN_X, Hint.ORIGIN_Y);
    }
    
    public HUD hud() {
        return this.hud;
    }
}
