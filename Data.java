import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;

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

    public static final String MAPSIZE = "map_size";
    public static final String METADATA = "map_metadata";
    public static final String TILES = "tiles";
    public static final String CITYSTATS = "city_stats";
    public static final String ZONESTATS = "zone_stats";
    public static final String ZONETILE = "zone_tile";
    public static final String ZONES = "zones";
    public static final String ROADSTATS = "road_stats";
    public static final HashMap<String, String[]> TABLES_MAPPING;

    // Zones
    public static final String ZONES_ID = "id";
    public static final String ZONES_ZONE = "zone";
    public static final String ZONES_AGE = "age";
    public static final String ZONES_POWERED = "powered";
    public static final String ZONES_HASWATER = "has_water";
    public static final String ZONES_X = "x";
    public static final String ZONES_Y = "y";
    public static final String ZONES_PRIMARY_ALLOCATION = "primary_allocation";
    public static final String ZONES_PRIMARY_CAPACITY = "primary_capacity";
    public static final String ZONES_SECONDARY_ALLOCATION = "secondary_allocation";
    public static final String ZONES_SECONDARY_CAPACITY = "secondary_capacity";
    public static final String[] ZONES_PARAMS = { ZONES_ID, ZONES_ZONE, ZONES_AGE, ZONES_POWERED, ZONES_HASWATER, ZONES_X, ZONES_Y, ZONES_PRIMARY_ALLOCATION, ZONES_PRIMARY_CAPACITY, ZONES_SECONDARY_ALLOCATION, ZONES_SECONDARY_CAPACITY };
    // Zone tiles
    public static final String ZONETILE_ZONEID = "zone_id";
    public static final String ZONETILE_TILEID = "tile_id";
    public static final String[] ZONETILE_PARAMS = { ZONETILE_ZONEID, ZONETILE_TILEID };
    // Zone stats
    public static final String ZONESTATS_RESIDENTIALCOUNT = "residential_count";
    public static final String ZONESTATS_INDUSTRIALCOUNT = "industrial_count";
    public static final String ZONESTATS_COMMERCIALCOUNT = "commercial_count";
    public static final String ZONESTATS_LASTZONEID = "last_zone_id";
    public static final String[] ZONESTATS_PARAMS = { ZONESTATS_RESIDENTIALCOUNT, ZONESTATS_INDUSTRIALCOUNT, ZONESTATS_COMMERCIALCOUNT, ZONESTATS_LASTZONEID };
    // Road stats
    public static final String ROADSTATS_STREETCOUNT = "street_count";
    public static final String[] ROADSTATS_PARAMS = { ROADSTATS_STREETCOUNT };
    // Tiles
    public static final String TILES_ID = "id";
    public static final String TILES_X = "x";
    public static final String TILES_Y = "y";
    public static final String TILES_TYPE = "type";
    public static final String TILES_ZONE = "zone";
    public static final String TILES_ZONEID = "zone_id";
    public static final String TILES_ROAD = "road";
    public static final String TILES_POWERED = "powered";
    public static final String TILES_HASWATER = "has_water";
    public static final String[] TILES_PARAMS = { TILES_ID, TILES_X, TILES_Y, TILES_TYPE, TILES_ZONE, TILES_ZONEID, TILES_ROAD, TILES_POWERED, TILES_HASWATER};
    // Map size
    public static final String MAPSIZE_ROWS = "rows";
    public static final String MAPSIZE_COLUMNS = "columns";
    public static final String[] MAPSIZE_PARAMS = { MAPSIZE_ROWS, MAPSIZE_COLUMNS };
    // Metadata
    public static final String METADATA_NAME = "name";
    public static final String[] METADATA_PARAMS = { METADATA_NAME };
    // City stats.
    public static final String CITYSTATS_DAYS = "days";
    public static final String CITYSTATS_MONTHS = "months";
    public static final String CITYSTATS_YEARS = "years";
    public static final String CITYSTATS_POPULATION = "population";
    public static final String CITYSTATS_CASH = "cash";
    public static final String[] CITYSTATS_PARAMS = { CITYSTATS_DAYS, CITYSTATS_MONTHS, CITYSTATS_YEARS, CITYSTATS_POPULATION, CITYSTATS_CASH };

    // --------------------------------------------------------------------------------------------------------------------

    static {

        TABLES_MAPPING = new HashMap<String, String[]>();
        TABLES_MAPPING.put(ZONES, ZONES_PARAMS);
        TABLES_MAPPING.put(ZONETILE, ZONETILE_PARAMS);
        TABLES_MAPPING.put(ZONESTATS, ZONESTATS_PARAMS);
        TABLES_MAPPING.put(ROADSTATS, ROADSTATS_PARAMS);
        TABLES_MAPPING.put(TILES, TILES_PARAMS);
        TABLES_MAPPING.put(MAPSIZE, MAPSIZE_PARAMS);
        TABLES_MAPPING.put(METADATA, METADATA_PARAMS);
        TABLES_MAPPING.put(CITYSTATS, CITYSTATS_PARAMS);

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
        CSLogger.sharedLogger().trace("Returning map tiles");
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
                Tile tile = (Tile)tiles.get(x).get(y);
                cachedTiles.get(tile.position().x).set(tile.position().y, tile);
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

    public static int idForNewZone() {
        incrementLastZoneID();
        return lastZoneID();
    }

    public static int lastZoneID() {
        int id = ((Integer)DataSource.getInstance().zoneStats().get("last_zone_id")).intValue();
        CSLogger.sharedLogger().info("Returning last zone ID (" + id + ")");
        return id;
    }

    private static void incrementLastZoneID() {
        int id = lastZoneID()+1;
        CSLogger.sharedLogger().info("Incrementing last zone ID to (" + id + ")");
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_LASTZONEID, id);
        DataSource.getInstance().updateZoneStats(zoneStats);
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

    public static HashMap[] zonesWithCriteria(String criteria) {
        //         CSLogger.sharedLogger().info("Running query for zones matching criteria (" + criteria + ")");
        return DataSource.getInstance().zonesWithCriteria(criteria);
    }

    public static HashMap[] zonesInArea(Point start, int radius) {
        return zonesWithCriteria("x >= " + (start.x-radius) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius) + " AND y <= " + (start.y+radius));
    }

    public static HashMap[] zonesInArea(Point start, int radius, int zone) {
        return zonesWithCriteria("x >= " + (start.x-radius) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius) + " AND y <= " + (start.y+radius) + " AND zone = " + zone);
    }

    public static HashMap[] zonesInArea(Point start, int radius, int zone1, int zone2) {
        return zonesWithCriteria("x >= " + (start.x-radius) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius) + " AND y <= " + (start.y+radius) + " AND (zone = " + zone1 + " OR zone = " + zone2 + ")");
    }

    public static void insertZone(HashMap zone) {
        CSLogger.sharedLogger().info("Inserting zone...");
        DataSource.getInstance().insertZone(zone);
    }

    public static void insertZone(int id, int type, int powered, int hasWater, int x, int y) {
        CSLogger.sharedLogger().info("Inserting zone (" + id + ") of type (" + type + ")");
        HashMap zone = new HashMap(ZONES_PARAMS.length);
        zone.put(ZONES_ID, id);
        zone.put(ZONES_ZONE, type);
        zone.put(ZONES_AGE, 0);
        zone.put(ZONES_POWERED, powered);
        zone.put(ZONES_HASWATER, hasWater);
        zone.put(ZONES_X, x);
        zone.put(ZONES_Y, y);
        DataSource.getInstance().insertZone(zone);
    }

    public static void updateZone(HashMap zone) {
        CSLogger.sharedLogger().info("Updating zone...");
        DataSource.getInstance().updateZone(zone);
    }

    public static void deleteZoneWithID(int id) {
        CSLogger.sharedLogger().info("Deleting zone with ID (" + id + ")");
        DataSource.getInstance().deleteZoneWithID(id);
    }

    //

    public static int[] tilesInZoneWithID(int id) {
        CSLogger.sharedLogger().info("Getting zone_tile with ID: " + id);
        return DataSource.getInstance().tilesInZoneWithID(id);
    }

    public static void insertZoneTiles(HashMap[] zoneTiles) {
        CSLogger.sharedLogger().info("Inserting zone_tile...");
        new ZoneTileDBInsertThread(zoneTiles).start();
    }

    public static void deleteZoneTileWithID(int zoneID) {
        CSLogger.sharedLogger().info("Deleting zone_tile with ID: " + zoneID);
        new ZoneTileDBDeleteThread(zoneID).start();
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
