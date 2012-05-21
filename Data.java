import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Data
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-16-2012
 * 
 * A higher level interface to the game's DB (on top of 'DataSource.getInstance()') that provides caching of data.
 * 
 */

public class Data  
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES * 
     */

    private static DataSource dataSource = null;                                    // Data source; SQLite interface

    // Data cache
    private static LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<String, Object>() {
                public Object load(String key) throws Exception {
                    CSLogger.sharedLogger().info("Caching data for key: " + key);
                    return dataForKey(key);
                }
            }
        );

    /*
     * IDENTIFIERS *
     */

    // - Internal -
    private static final String MAPSIZE = "map_size";
    private static final String METADATA = "metadata";
    private static final String TILES = "tiles";
    private static final String CITYSTATS = "citystats";
    private static final String ZONESTATS = "zonestats";
    private static final String ZONETILE = "zone_tile";

    // - External -
    // Map size
    public static final String MAPSIZE_ROWS = "rows";
    public static final String MAPSIZE_COLUMNS = "columns";
    // City stats.
    public static final String CITYSTATS_DAYS = "days";
    public static final String CITYSTATS_MONTHS = "months";
    public static final String CITYSTATS_YEARS = "years";
    public static final String CITYSTATS_POPULATION = "population";
    public static final String CITYSTATS_CASH = "cash";
    // Metadata
    public static final String METADATA_NAME = "name";
    // Zones
    public static final String ZONETILE_ZONEID = "zone_id";
    public static final String ZONETILE_TILEID = "tile_id";
    // Zone stats
    public static final String ZONESTATS_RESIDENTIALCOUNT = "residential_count";
    public static final String ZONESTATS_INDUSTRIALCOUNT = "industrial_count";
    public static final String ZONESTATS_COMMERCIALCOUNT = "commercial_count";
    public static final String ZONESTATS_LASTZONEID = "last_zone_id";
    // Road stats
    public static final String ROADSTATS_STREETCOUNT = "street_count";

    // --------------------------------------------------------------------------------------------------------------------

    static {
        if (dataSource == null) {
            dataSource = new DataSource("test");
        }
    }

    /*
     * OPERATIONS *
     */

    public static boolean dbIsNew() {
        CSLogger.sharedLogger().info("Asking data source if DB is new...");
        return DataSource.getInstance().dbIsNew();
    }

    public static boolean connectionIsOpen() {
        CSLogger.sharedLogger().info("Asking data source if connection is open...");
        return DataSource.getInstance().connectionIsOpen();
    }

    public static void resumeConnection() {
        CSLogger.sharedLogger().info("Asking data source to resume connection...");
        DataSource.getInstance().resumeConnection();
    }

    public static void closeConnection() {

        CSLogger.sharedLogger().info("Asking data source to close connection...");

        // Close connection to DB
        DataSource.getInstance().closeConnection();

        // Clean out the cache 
        cache.invalidateAll();
        cache.cleanUp();
        CSLogger.sharedLogger().info("Data cache has been cleared.");
    }

    //

    public static HashMap mapSize() {
        CSLogger.sharedLogger().info("Returning map size");
        return (HashMap)get(MAPSIZE);
    }

    public static void insertMapSize(HashMap mapSize) {
        CSLogger.sharedLogger().info("Inserting map size");
        DataSource.getInstance().insertMapSize(mapSize);
    }

    //

    public static HashMap mapMetadata() {
        CSLogger.sharedLogger().info("Returning map metadata");
        return (HashMap)get(METADATA);
    }

    public static void insertMapMetadata(HashMap metadata) {
        CSLogger.sharedLogger().info("Inserting map metadata");
        DataSource.getInstance().insertMapMetadata(metadata);
    }

    public static void updateMapMetadata(HashMap metadata) {
        DataSource.getInstance().updateMapMetadata(metadata);
    }

    //

    public static ArrayList<ArrayList<Tile>> tiles() {
        CSLogger.sharedLogger().finest("Returning map tiles");
        return (ArrayList<ArrayList<Tile>>)get(TILES);
    }

    public static Tile tileWithID(int id) {
        return DataSource.getInstance().tileWithID(id);
    }

    public static void insertTiles(ArrayList<ArrayList<Tile>> tiles) {
        CSLogger.sharedLogger().info("Inserting map tiles");
        DataSource.getInstance().insertTiles(tiles);
    }

    public static void updateTile(Tile tile) {

        CSLogger.sharedLogger().info("Updating map tile");

        // Update the cached map w/ the changes so that the map can be redrawn using the modified cached data
        // This is much faster than writing the changes to the DB, AND THEN running a query to refresh the cache
        ArrayList<ArrayList<Tile>> cachedTiles = (ArrayList<ArrayList<Tile>>)get(TILES);
        cachedTiles.get(tile.position().x).set(tile.position().y, tile);

        // Re-draw map
        Map.getInstance().draw();

        // Tell minimap it should be updated
        Minimap.getInstance().setShouldUpdate(true);

        // Write changes to DB after displaying the changes
        new TileDBUpdateThread(tile).start();
    }

    public static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().info("Updating " + tiles.size() * tiles.get(0).size() + " map tiles");

        // Update the cached map w/ the changes so that the map can be redrawn using the modified cached data
        // This is much faster than writing the changes to the DB, AND THEN running a query to refresh the cache
        ArrayList<ArrayList<Tile>> cachedTiles = (ArrayList<ArrayList<Tile>>)get(TILES);
        for (int x = 0; x < tiles.size(); x++) {
            for (int y = 0; y < tiles.get(x).size(); y++) {
                cachedTiles.get(x).set(y, (Tile)tiles.get(x).get(y));
            }
        }

        // Re-draw map
        Map.getInstance().draw();

        // Tell minimap it should be updated
        Minimap.getInstance().setShouldUpdate(true);

        // Write changes to DB after displaying the changes
        new TileDBUpdateThread(tiles).start();
    }

    //

    public static HashMap zoneStats() {
        CSLogger.sharedLogger().info("Getting zone stats");
        return DataSource.getInstance().zoneStats();
    }

    public static void insertZoneStats(HashMap stats) {
        CSLogger.sharedLogger().info("Inserting zone stats...");
        DataSource.getInstance().insertZoneStats(stats);
    }

    public static void updateZoneStats(HashMap stats) {
        CSLogger.sharedLogger().info("Updating zone stats...");
        DataSource.getInstance().updateZoneStats(stats);
    }

    public static int lastZoneID() {
        return ((Integer)DataSource.getInstance().zoneStats().get("last_zone_id")).intValue();
    }

    //

    public static HashMap roadStats() {
        CSLogger.sharedLogger().info("Getting road stats");
        return DataSource.getInstance().roadStats();
    }

    public static void insertRoadStats(HashMap stats) {
        CSLogger.sharedLogger().info("Inserting road stats...");
        DataSource.getInstance().insertRoadStats(stats);
    }

    public static void updateRoadStats(HashMap stats) {
        CSLogger.sharedLogger().info("Updating road stats...");
        DataSource.getInstance().updateRoadStats(stats);
    }

    //

    public static int[] tilesInZoneWithID(int id) {
        CSLogger.sharedLogger().info("Getting zone with ID: " + id);
        return DataSource.getInstance().tilesInZoneWithID(id);
    }

    public static void insertZoneWithTiles(HashMap[] zoneTiles) {
        CSLogger.sharedLogger().info("Inserting zone...");
        new ZoneDBInsertThread(zoneTiles).start();
    }

    public static void deleteZoneWithID(int zoneID) {
        CSLogger.sharedLogger().info("Deleting zone with ID: " + zoneID);
        new ZoneDBDeleteThread(zoneID).start();
    }

    //

    public static HashMap cityStats() {
        CSLogger.sharedLogger().info("Returning city stats");
        return (HashMap)get(CITYSTATS);
    }

    public static void insertCityStats(HashMap cityStats) {
        CSLogger.sharedLogger().info("Inserting city stats");
        DataSource.getInstance().insertCityStats(cityStats);
    }

    public static void updateCityStats(HashMap cityStats) {
        CSLogger.sharedLogger().info("Updating city stats");
        // Write new city stats. on seperate thread
        new CityStatsDBUpdateThread(cityStats).start();
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private static Object get(String key) {
        try {
            return cache.get(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object dataForKey(String key) {

        Object data = null;

        if (key.equals(MAPSIZE)) {
            data = DataSource.getInstance().mapSize();
        }
        else if (key.equals(METADATA)) {
            data = DataSource.getInstance().mapMetadata();
        }
        else if (key.equals(TILES)) {
            data = DataSource.getInstance().tiles();
        }
        else if (key.equals(CITYSTATS)) {
            data = DataSource.getInstance().cityStats();
        }

        cache.put(key, data);

        return data;
    }
}
