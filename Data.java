import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.logging.*;

public class Data  
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES * 
     */

    private static Hashtable<String, Object> cache = new Hashtable();                                   // Data cache
    private static DataSource dataSource;                                                               // Data source; interface to DB
    private static Logger logger = LogManager.getLogManager().getLogger("com.felixmo.CitySim.logger");  // Shared logger

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
        logger.info("Data source has been set.");
    }

    /*
     * OPERATIONS *
     */

    public static boolean dbIsNew() {
        logger.info("Asking data source if DB is new...");
        return dataSource.dbIsNew();
    }

    public static boolean connectionIsOpen() {
        logger.info("Asking data source if connection is open...");
        return dataSource.connectionIsOpen();
    }

    public static void resumeConnection() {
        logger.info("Asking data source to resume connection...");
        dataSource.resumeConnection();
    }

    public static void closeConnection() {
        logger.info("Asking data source to close connection...");
        dataSource.closeConnection();
        cache.clear();
        logger.info("Data cache has been cleared.");
    }

    public static LinkedHashMap mapSize() {
        logger.info("Returning map size");
        return (LinkedHashMap)dataForKey(MAPSIZE);
    }

    public static void insertMapSize(LinkedHashMap mapSize) {
        logger.info("Inserting map size");
        dataSource.insertMapSize(mapSize);
    }

    public static LinkedHashMap mapMetadata() {
        logger.info("Returning map size");
        return (LinkedHashMap)dataForKey(METADATA);
    }

    public static void insertMapMetadata(LinkedHashMap metadata) {
        logger.info("Inserting map metadata");
        dataSource.insertMapMetadata(metadata);
    }

    public static ArrayList<ArrayList<Tile>> tiles() {
        logger.info("Returning map tiles");
        return (ArrayList<ArrayList<Tile>>)dataForKey(Data.TILES);
    }

    public static void insertTiles(ArrayList<ArrayList<Tile>> tiles) {
        logger.info("Inserting map tiles");
        dataSource.insertTiles(tiles);
    }

    public static LinkedHashMap cityStats() {
        logger.info("Returning city stats");
        return (LinkedHashMap)dataForKey(CITYSTATS);
    }

    public static void insertCityStats(LinkedHashMap cityStats) {
        logger.info("Inserting city stats");
        dataSource.insertCityStats(cityStats);
    }

    public static void updateCityStats(LinkedHashMap cityStats) {
        logger.info("Updating city stats");
        dataSource.updateCityStats(cityStats);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private static Object dataForKey(String key) {

        Object data = null;
        if (!cache.containsKey(key)) {

            logger.info(key + " not found; retrieving data from DB and caching it.");

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
