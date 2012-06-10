/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.awt.Point;
import java.util.Arrays;
import java.lang.Thread;

/**
 * 'City' acts as a container for all objects / "Actors" in the game (incl. map, HUD, etc.)
 * 
 * @author Felix Mo
 * @version v0.1
 * @since 2012-02-11
 */

public class City extends World
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTANTS *
     */

    private final int FREQ_WRITE = 10;               // # of secs before updated data is written to DB
    // Initial values
    private final int INITIAL_CASH = 100000;         // amount of $ to start with
    private final int INITIAL_POP = 0;               // # of people in the population to start with
    private final int INITIAL_DATE_DAYS = 1;         // number of days elasped in the inital date
    private final int INITIAL_DATE_MONTHS = 1;       // number of months elasped in the inital date
    private final int INITIAL_DATE_YEARS = 0;        // number of years elapsed in the inital date

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * STATIC VARIABLES *
     */

    private static City instance;                    // pointer to an instance of 'City'; to be used globally to access 'City'

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * REFERENCES *
     */

    private MenuBar menuBar;                        // Menu bar containing game controls
    private Hint hint;                              // Active hint
    private TileSelector tileSelector;              // Active tile selector

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * INSTANCE VARIABLES *
     */

    private int writeCountdown = 0;                  // Countdown until updated data is written to DB
    private int lastZoneID = 0;                      // Keeps track of the last ID assigned to a zone; this value is incremented and assigned to new zones

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Constructs a City.
     */
    public City() {

        super(1024, 768, 1, false);     // Create a 1024 x 768 'World' with a cell size of 1px that does not restrict 'actors' to the world boundary

        // Mark a new session in the logs
        CSLogger.sharedLogger().info("***** NEW SESSION *****");

        // Set Greenfoot paint order to ensure that Actors are layered properly
        setPaintOrder(TileSelectorItem.class, TileSelector.class, Hint.class, MenuItem.class, Menu.class, MenuBarItem.class, MenuBar.class, Label.class, MinimapViewport.class, Minimap.class, HUD.class, Selection.class, Map.class);

        // FOR TESTING ONLY 
        // Delete the DB so that map re-generates each run

        //         new File("maps/test.db").delete();

        // If the data source has just created a new DB (b/c it did not exist), seed it with initial stats. and metadata
        if (Data.dbIsNew()) {

            // - Metadata -
            HashMap mapMetadata = new HashMap(1);
            // Initial metadata
            mapMetadata.put(Data.METADATA_NAME, "Toronto"); // FOR TESTING PURPOSES

            Data.insertMapMetadata(mapMetadata);

            // - City stats -
            HashMap cityStats = new HashMap(5);
            // Initial city stats.
            cityStats.put(Data.CITYSTATS_DAYS, INITIAL_DATE_DAYS);
            cityStats.put(Data.CITYSTATS_MONTHS, INITIAL_DATE_MONTHS);
            cityStats.put(Data.CITYSTATS_YEARS, INITIAL_DATE_YEARS);
            cityStats.put(Data.CITYSTATS_POPULATION, INITIAL_POP);
            cityStats.put(Data.CITYSTATS_CASH, INITIAL_CASH);

            Data.insertCityStats(cityStats);

            // - Zone stats -
            HashMap zoneStats = new HashMap(4);
            zoneStats.put(Data.ZONESTATS_RESIDENTIALCOUNT, 0);
            zoneStats.put(Data.ZONESTATS_INDUSTRIALCOUNT, 0);
            zoneStats.put(Data.ZONESTATS_COMMERCIALCOUNT, 0);
            zoneStats.put(Data.ZONESTATS_LASTZONEID, -1);

            Data.insertZoneStats(zoneStats);

            // - Road stats -
            HashMap roadStats = new HashMap(1);
            roadStats.put(Data.ROADSTATS_STREETCOUNT, 0);

            Data.insertRoadStats(roadStats);
        }

        // Resume tracking date from last saved date
        HashMap cityStats = Data.cityStats();
        Date.set((Integer)cityStats.get(Data.CITYSTATS_DAYS), (Integer)cityStats.get(Data.CITYSTATS_MONTHS), (Integer)cityStats.get(Data.CITYSTATS_YEARS));
        Population.set((Integer)cityStats.get(Data.CITYSTATS_POPULATION));

        // Create and add a new map for the city
        addObject(new Map(), 512, 333);

        // Create and add HUD
        addObject(new HUD(), 512, 653);

        // Create and add the representation of the viewport into the minimap
        addObject(new MinimapViewport(new Point(0, 0)), 112, 658);

        // Create and add the menubar
        menuBar = new MenuBar();
        addObject(menuBar, 512, 14);

        // - Menu bar items -
        ArrayList<String> menuBarItems = new ArrayList(6);
        menuBarItems.add(Zone.NAME);
        menuBarItems.add(Road.NAME);
        menuBarItems.add(PowerGrid.NAME);
        menuBarItems.add(ProtectionZone.NAME);
        menuBarItems.add(Recreation.NAME);
        menuBarItems.add(Tool.NAME);
        menuBar.setItems(menuBarItems);

        // * Menu items * 

        /*
         * NOTE *
         * Menu items need to be declared in 'MenuItemEvent' as well and implemented in 'MenuItemEventListener'.
         */

        // -> Zoning (first)
        ArrayList<String> zoneItems = new ArrayList(3);
        zoneItems.add(ResidentialZone.NAME);
        zoneItems.add(CommercialZone.NAME);
        zoneItems.add(IndustrialZone.NAME);
        menuBar.setMenuItemsForItem(Zone.NAME, zoneItems);

        // -> Transportation
        ArrayList<String> roadItems = new ArrayList(1);
        roadItems.add(Street.NAME);
        menuBar.setMenuItemsForItem(Road.NAME, roadItems);

        // -> Power
        ArrayList<String> powerItems = new ArrayList(3);
        powerItems.add(PowerLine.NAME);
        powerItems.add(CoalPowerPlant.NAME);
        powerItems.add(NuclearPowerPlant.NAME);
        menuBar.setMenuItemsForItem(PowerGrid.NAME, powerItems);

        // -> Protection
        ArrayList<String> protectionItems = new ArrayList(2);
        protectionItems.add(FireStation.NAME);
        protectionItems.add(PoliceStation.NAME);
        menuBar.setMenuItemsForItem(ProtectionZone.NAME, protectionItems);
        
        // Recreation
        ArrayList<String> recreationItems = new ArrayList(2);
        recreationItems.add(Park.NAME);
        recreationItems.add(Stadium.NAME);
        menuBar.setMenuItemsForItem(Recreation.NAME, recreationItems);

        // -> Tools (last)
        ArrayList<String> toolItems = new ArrayList(1);
        toolItems.add(Bulldozer.NAME);
        menuBar.setMenuItemsForItem(Tool.NAME, toolItems);

        // * END of menu items *

        // Initalize the cash store from the last known value in the DB
        Cash.set(((Integer)cityStats.get(Data.CITYSTATS_CASH)));

        instance = this;
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * GREENFOOT EVENTS *
     */

    /**
     * Overrides started() in {@link World} from the Greenfoot framework. This is called when the game is started.
     */
    public void started() {
        CSLogger.sharedLogger().info("Game has started...");

        if (!Data.connectionIsOpen()) {
            Data.resumeConnection();
        }

        // TO DO: start timer when game has actually started (i.e. not in menu)
        Date.start();
    }

    /**
     * Overrides stopped() in {@link World} from the Greenfoot framework. This is called when the game is stopped.
     */
    public void stopped() {
        CSLogger.sharedLogger().info("Game has stopped.");

        // TO DO: pause timer when in menu
        Date.stop();

        Data.closeConnection();
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * DATE * 
     */

    /**
     * <p>
     * This method is called when date is incremented (every 1 sec).
     * </p>
     * 
     * <p>
     * This method tells {@link Data} to write the latest statistics to the database and hints to the JVM to perform GC <b>every 10 secs</b>, and refreshes the HUD values <b>every 1 sec</b>.
     * </p>
     */
    public void didIncrementDate() {

        // Write to DB
        writeCountdown++;

        if (writeCountdown % 5 == 0) {
            if (CSThread.count() == 0) {
                new CitySimulationThread().start();
            }
        }

        if (writeCountdown == FREQ_WRITE) {

            Data.updateCityStats(currentCityStats());  
            writeCountdown = 0;

            // Run Java garbage collector to cleanup
            System.gc();
        }

        // Refresh values for HUD every 1 sec
        HUD.getInstance().refresh(valuesForHUD());

        // Refresh minimap if there are changes to map
        if (Minimap.getInstance().shouldUpdate()) {
            new MinimapDrawThread().start();
            Minimap.getInstance().setShouldUpdate(false);
        }

        if (writeCountdown % 3 == 0 && PowerGrid.shouldEvaluate()) {
            new PowerGridEvaluationThread().start();
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * MAP *
     */

    /**
     * <p>
     * This method is called when the minimap viewport has been moved.
     * </p>
     * (i.e. minimap has been clicked on)
     */
    public void didMoveViewportTo(int x, int y) {
        // Move the map
        Map.getInstance().viewportDidMoveTo(x, y);
    }

    /**
     * <p>
     * This method is called when the map is moved. 
     * </p>
     * (i.e. when the user scrolls the map).
     * <p>
     * 
     * </p>
     * <p>
     * Updates the viewport position on the minimap after the map has moved.
     * </p>
     */
    public void didMoveMapTo(int x, int y) {
        // Move the representation of the viewport in the minimap
        MinimapViewport.getInstance().didMoveViewportToCell(x, y);
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * MODAL VIEWS / DIALOGS *
     */

    /**
     * Removes the active hint from view.
     */
    public void removeHint() {
        removeObject(this.hint);
    }

    /**
     * Removes the active tile selector from view.
     */
    public void removeTileSelector() {
        removeObjects(Arrays.asList(this.tileSelector.items()));
        removeObject(this.tileSelector);
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * HELPERS *
     */

    // Returns formatted values for the HUD labels
    private HashMap valuesForHUD() {

        HashMap values = new HashMap(4);

        values.put(HUD.NAME, Population.category() + " of Toronto");  // TESTING
        values.put(HUD.POPULATION, Population.size());    // TESTING
        values.put(HUD.DATE, Date.asString());
        values.put(HUD.CASH, Cash.asString());

        return values;
    }

    // Returns the city stats. w/o formatting (i.e. for writing the values to the DB)
    private HashMap currentCityStats() {

        HashMap stats = new HashMap(5);

        stats.put(Data.CITYSTATS_DAYS, Date.days());
        stats.put(Data.CITYSTATS_MONTHS, Date.months());
        stats.put(Data.CITYSTATS_YEARS, Date.years());
        stats.put(Data.CITYSTATS_POPULATION, Population.size());
        stats.put(Data.CITYSTATS_CASH, Cash.value());

        return stats;
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ACCESSORS *
     */

    /**
     * Returns the active {@link Hint}.
     * 
     * @return The active {@link Hint}.
     */
    public Hint hint() {
        return this.hint;
    }

    /**
     * Sets the active hint. This reference to it will be used later to remove it from view.
     * 
     * @param hint The hint to be made active.
     */
    public void setHint(Hint hint) {
        this.hint = hint;
        addObject(this.hint, Hint.ORIGIN_X, Hint.ORIGIN_Y);
    }

    /**
     * Returns the active {@link TileSelector}.
     * 
     * @return The active {@link TileSelector}.
     */
    public TileSelector tileSelector() {
        return this.tileSelector;
    }

    /**
     * Sets the active {@link TileSelector}.
     * 
     * @param tileSelector The {@link TileSelector} to be made active.
     */
    public void setTileSelector(TileSelector tileSelector) {
        this.tileSelector = tileSelector;
        addObject(this.tileSelector, TileSelector.ORIGIN_X, TileSelector.ORIGIN_Y);
    }

    /**
     * Returns an instance {@link City}.
     * 
     * @return An instance of City.
     */
    public static City getInstance() {
        return instance;
    }
}
