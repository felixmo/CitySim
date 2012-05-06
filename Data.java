import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * Data
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-16-2012
 * 
 * A higher level interface to the game's DB (on top of 'DataSource') that provides caching of data.
 * 
 */

public class Data  
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES * 
     */

    private static Hashtable<String, Object> cache = new Hashtable();                                   // Data cache
    private static DataSource dataSource;                                                               // Data source; interface to DB

    /*
     * IDENTIFIERS *
     */

    // This
    private static final String MAPSIZE = "map_size";
    private static final String METADATA = "metadata";
    private static final String TILES = "tiles";
    private static final String CITYSTATS = "citystats";
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

    // ---------------------------------------------------------------------------------------------------------------------

    public static void setDataSource(DataSource ds) {
        dataSource = ds;
        CSLogger.sharedLogger().info("Data source has been set.");
    }

    /*
     * OPERATIONS *
     */

    public static boolean dbIsNew() {
        CSLogger.sharedLogger().info("Asking data source if DB is new...");
        return dataSource.dbIsNew();
    }

    public static boolean connectionIsOpen() {
        CSLogger.sharedLogger().info("Asking data source if connection is open...");
        return dataSource.connectionIsOpen();
    }

    public static void resumeConnection() {
        CSLogger.sharedLogger().info("Asking data source to resume connection...");
        dataSource.resumeConnection();
    }

    public static void closeConnection() {
        CSLogger.sharedLogger().info("Asking data source to close connection...");
        dataSource.closeConnection();
        cache.clear();
        CSLogger.sharedLogger().info("Data cache has been cleared.");
    }

    public static LinkedHashMap mapSize() {
        CSLogger.sharedLogger().info("Returning map size");
        return (LinkedHashMap)dataForKey(MAPSIZE);
    }

    public static void insertMapSize(LinkedHashMap mapSize) {
        CSLogger.sharedLogger().info("Inserting map size");
        dataSource.insertMapSize(mapSize);
    }

    public static LinkedHashMap mapMetadata() {
        CSLogger.sharedLogger().info("Returning map size");
        return (LinkedHashMap)dataForKey(METADATA);
    }

    public static void insertMapMetadata(LinkedHashMap metadata) {
        CSLogger.sharedLogger().info("Inserting map metadata");
        dataSource.insertMapMetadata(metadata);
    }

    public static ArrayList<ArrayList<Tile>> tiles() {
        CSLogger.sharedLogger().info("Returning map tiles");
        return (ArrayList<ArrayList<Tile>>)dataForKey(Data.TILES);
    }

    public static void insertTiles(ArrayList<ArrayList<Tile>> tiles) {
        CSLogger.sharedLogger().info("Inserting map tiles");
        dataSource.insertTiles(tiles);
    }

    public static LinkedHashMap cityStats() {
        CSLogger.sharedLogger().info("Returning city stats");
        return (LinkedHashMap)dataForKey(CITYSTATS);
    }

    public static void insertCityStats(LinkedHashMap cityStats) {
        CSLogger.sharedLogger().info("Inserting city stats");
        dataSource.insertCityStats(cityStats);
    }

    public static void updateCityStats(LinkedHashMap cityStats) {
        CSLogger.sharedLogger().info("Updating city stats");
        dataSource.updateCityStats(cityStats);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private static Object dataForKey(String key) {

        Object data = null;
        if (!cache.containsKey(key)) {

            CSLogger.sharedLogger().info(key + " not found; retrieving data from DB and caching it.");

            if (key.equals(MAPSIZE)) {
                data = dataSource.mapSize();
            }
            else if (key.equals(METADATA)) {
                data = dataSource.mapMetadata();
            }
            else if (key.equals(TILES)) {
                data = dataSource.tiles();
            }
            else if (key.equals(CITYSTATS)) {
                data = dataSource.cityStats();
            }

            cache.put(key, data);
        }

        return data != null ? data : cache.get(key);
    }
}
