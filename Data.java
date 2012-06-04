/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Point;
import java.util.Arrays;

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

    // General data cache
    private static LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<String, Object>() {
                public Object load(String key) throws Exception {
                    CSLogger.sharedLogger().fine("Caching data for key: " + key);
                    return dataForKey(key);
                }
            }
        );

    // Caches zone_tile data
    private static LoadingCache<String, Object> zoneTileCache = CacheBuilder.newBuilder()
        .maximumSize(4000)
        .build(
            new CacheLoader<String, Object>() {
                public Object load(String key) throws Exception {
                    CSLogger.sharedLogger().fine("Caching zone_tile (" + key + ")");
                    return DataSource.getInstance().tilesInZoneWithID(Integer.parseInt(key));
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
    public static final String ZONES_X = "x";
    public static final String ZONES_Y = "y";
    public static final String ZONES_SCORE = "score";
    public static final String ZONES_POLLUTION = "pollution";
    public static final String ZONES_FIRE_PROTECTION = "fire_protection";
    public static final String ZONES_POLICE_PROTECTION = "police_protection";
    public static final String ZONES_CRIME = "crime";
    public static final String ZONES_FOOD = "food";
    public static final String ZONES_JOBS = "jobs";
    public static final String ZONES_ALLOCATION = "allocation";
    public static final String ZONES_CAPACITY = "capacity";
    public static final String ZONES_STAGE = "stage";
    public static final String[] ZONES_PARAMS = { ZONES_ID, ZONES_ZONE, ZONES_AGE, ZONES_POWERED, ZONES_X, ZONES_Y, ZONES_SCORE, ZONES_POLLUTION, ZONES_FIRE_PROTECTION, ZONES_POLICE_PROTECTION, ZONES_CRIME, ZONES_FOOD, ZONES_JOBS, ZONES_ALLOCATION, ZONES_CAPACITY, ZONES_STAGE };
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
    public static final String TILES_POWERGRID_TYPE = "powergrid_type";
    public static final String TILES_RECREATION_TYPE = "recreation_type";
    public static final String[] TILES_PARAMS = { TILES_ID, TILES_X, TILES_Y, TILES_TYPE, TILES_ZONE, TILES_ZONEID, TILES_ROAD, TILES_POWERED, TILES_POWERGRID_TYPE, TILES_RECREATION_TYPE };
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
        CSLogger.sharedLogger().fine("Asking data source if DB is new...");
        return DataSource.getInstance().dbIsNew();
    }

    public static boolean connectionIsOpen() {
        CSLogger.sharedLogger().fine("Asking data source if connection is open...");
        return DataSource.getInstance().connectionIsOpen();
    }

    public static void resumeConnection() {
        CSLogger.sharedLogger().fine("Asking data source to resume connection...");
        DataSource.getInstance().resumeConnection();
    }

    public static void closeConnection() {

        CSLogger.sharedLogger().fine("Asking data source to close connection...");

        // Close connection to DB
        DataSource.getInstance().closeConnection();

        // Clean out the cache 
        cache.invalidateAll();
        cache.cleanUp();
        CSLogger.sharedLogger().fine("Data cache has been cleared.");
    }

    //

    public static HashMap mapSize() {
        CSLogger.sharedLogger().fine("Returning map size");
        return (HashMap)get(MAPSIZE);
    }

    public static void insertMapSize(HashMap mapSize) {
        CSLogger.sharedLogger().fine("Inserting map size");
        DataSource.getInstance().insertMapSize(mapSize);
    }

    //

    public static HashMap mapMetadata() {
        CSLogger.sharedLogger().fine("Returning map metadata");
        return (HashMap)get(METADATA);
    }

    public static void insertMapMetadata(HashMap metadata) {
        CSLogger.sharedLogger().fine("Inserting map metadata");
        DataSource.getInstance().insertMapMetadata(metadata);
    }

    public static void updateMapMetadata(HashMap metadata) {
        DataSource.getInstance().updateMapMetadata(metadata);
    }

    //

    public static ArrayList<ArrayList<Tile>> tiles() {
        CSLogger.sharedLogger().finer("Returning map tiles");
        return (ArrayList<ArrayList<Tile>>)get(TILES);
    }

    public static Tile[] tilesAroundTile(Tile tile) {
        Tile[] tiles = DataSource.getInstance().tilesMatchingCriteria("x >= " + (tile.position().x-1) + " AND x <= " + (tile.position().x+1) + " AND y >= " + (tile.position().y-1) + " AND y <= " + (tile.position().y+1) + " AND NOT id = " + tile.dbID());
        Arrays.sort(tiles, new TileComparator());
        return tiles;
    }

    public static Tile[] tilesMatchingCriteriaAroundTile(Tile tile, String criteria) {
        Tile[] tiles = DataSource.getInstance().tilesMatchingCriteria("NOT id = " + tile.dbID() + " AND x >= " + (tile.position().x-1) + " AND x <= " + (tile.position().x+1) + " AND y >= " + (tile.position().y-1) + " AND y <= " + (tile.position().y+1) + " AND " + criteria);
        Arrays.sort(tiles, new TileComparator());
        return tiles;
    }

    public static Tile[] tilesMatchingCriteriaTouchingTile(Tile tile, String criteria) {
        return DataSource.getInstance().tilesMatchingCriteria("NOT id = " + tile.dbID() + (criteria.length() > 0 ? " AND " + criteria : "") + " AND ((x = " + (tile.position().x-1) + " AND y = " + tile.position().y + ") OR (x = " + (tile.position().x+1) + " AND y = " + tile.position().y + ") OR ( x = " + tile.position().x + " AND y = " + (tile.position().y-1) + ") OR ( x = " + tile.position().x + " AND y = " + (tile.position().y+1) + "))"); 
    }

    public static Tile[] tilesMatchingCriteriaTouchingTile(Tile tile) {
        return tilesMatchingCriteriaTouchingTile(tile, "");
    }

    public static Tile[] tilesMatchingCriteria(String criteria) {
        return DataSource.getInstance().tilesMatchingCriteria(criteria);
    }

    public static Tile[] tilesAroundZoneWithCriteria(Zone zone, String criteria) {

        int size = 1 + (((Integer)zone.get(Data.ZONES_ZONE)).intValue() <= 3 ? ResidentialZone.SIZE_WIDTH : CoalPowerPlant.SIZE_WIDTH);

        Tile[] left = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "x = " + (((Integer)zone.get(Data.ZONES_X)).intValue()-1) + " AND y >= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-1) + " AND y <= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-1));
        Tile[] top = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "y = " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-1) + " AND x >= " + (((Integer)zone.get(Data.ZONES_X)).intValue()-1) + " AND x <= " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-1));

        Tile[] c1 = new Tile[left.length + top.length];
        System.arraycopy(left, 0, c1, 0, left.length);
        System.arraycopy(top, 0, c1, left.length, top.length); 

        Tile[] right = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "x = " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-1) + " AND y >= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-1) + " AND y <= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-1));
        Tile[] bottom = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "y = " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-1) + " AND x >= " + (((Integer)zone.get(Data.ZONES_X)).intValue()-1) + " AND x <= " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-1));

        Tile[] c2 = new Tile[right.length + bottom.length];
        System.arraycopy(right, 0, c2, 0, right.length);
        System.arraycopy(bottom, 0, c2, right.length, bottom.length); 

        Tile[] c3 = new Tile[c1.length + c2.length];
        System.arraycopy(c1, 0, c3, 0, c1.length);
        System.arraycopy(c2, 0, c3, c1.length, c2.length);

        return c3;
    }

    public static Tile[] tilesInRadiusOfZoneMatchingCriteria(Zone zone, int radius, String criteria) {

        int size = 1 + (((Integer)zone.get(Data.ZONES_ZONE)).intValue() <= 3 ? ResidentialZone.SIZE_WIDTH : CoalPowerPlant.SIZE_WIDTH);

        Tile[] left = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "x = " + (((Integer)zone.get(Data.ZONES_X)).intValue()-radius) + " AND y >= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-radius) + " AND y <= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-radius));
        Tile[] top = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "y = " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-radius) + " AND x >= " + (((Integer)zone.get(Data.ZONES_X)).intValue()-radius) + " AND x <= " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-radius));

        Tile[] c1 = new Tile[left.length + top.length];
        System.arraycopy(left, 0, c1, 0, left.length);
        System.arraycopy(top, 0, c1, left.length, top.length); 

        Tile[] right = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "x = " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-radius) + " AND y >= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()-radius) + " AND y <= " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-radius));
        Tile[] bottom = DataSource.getInstance().tilesMatchingCriteria(criteria + (criteria.length() > 0 ? " AND " : "") + "y = " + (((Integer)zone.get(Data.ZONES_Y)).intValue()+size-radius) + " AND x >= " + (((Integer)zone.get(Data.ZONES_X)).intValue()-radius) + " AND x <= " + (((Integer)zone.get(Data.ZONES_X)).intValue()+size-radius));

        Tile[] c2 = new Tile[right.length + bottom.length];
        System.arraycopy(right, 0, c2, 0, right.length);
        System.arraycopy(bottom, 0, c2, right.length, bottom.length); 

        Tile[] c3 = new Tile[c1.length + c2.length];
        System.arraycopy(c1, 0, c3, 0, c1.length);
        System.arraycopy(c2, 0, c3, c1.length, c2.length);

        return c3;
    }

    public static Tile[] tilesMatchingCriteriaWithinRadiusOfTile(String criteria, Tile tile, int radius) {

        return tilesMatchingCriteria("x >= " + (tile.position().x-radius) + " AND x <= " + (tile.position().x+radius) + " AND y >= " + (tile.position().y-radius) + " AND y <= " + (tile.position().y+radius) + " AND " + criteria);
    }

    public static Tile[] tilesAroundZone(Zone zone) {
        return tilesAroundZoneWithCriteria(zone, "");
    }

    public static Tile tileWithID(int id) {
        return DataSource.getInstance().tileWithID(id);
    }

    public static void insertTiles(ArrayList<ArrayList<Tile>> tiles) {
        CSLogger.sharedLogger().fine("Inserting map tiles");
        DataSource.getInstance().insertTiles(tiles);
    }

    public static void updateTile(Tile tile) {

        CSLogger.sharedLogger().fine("Updating map tile");

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

    public static void updateTileWithoutDraw(Tile tile) {

        CSLogger.sharedLogger().fine("Updating map tile without draw");

        // Update the cached map w/ the changes so that the map can be redrawn using the modified cached data
        // This is much faster than writing the changes to the DB, AND THEN running a query to refresh the cache
        ArrayList<ArrayList<Tile>> cachedTiles = (ArrayList<ArrayList<Tile>>)get(TILES);
        cachedTiles.get(tile.position().x).set(tile.position().y, tile);

        // Write changes to DB after displaying the changes
        new TileDBUpdateThread(tile).start();
    }

    public static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().fine("Updating " + tiles.size() * tiles.get(0).size() + " map tiles");

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

    public static void updateTiles(Tile[] tiles) {

        ArrayList<ArrayList<Tile>> cachedTiles = (ArrayList<ArrayList<Tile>>)get(TILES);
        for (Tile tile : tiles) {
            cachedTiles.get(tile.position().x).set(tile.position().y, tile);
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
        CSLogger.sharedLogger().fine("Getting zone stats");
        return DataSource.getInstance().zoneStats();
    }

    public static void insertZoneStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Inserting zone stats...");
        DataSource.getInstance().insertZoneStats(stats);
    }

    public static void updateZoneStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Updating zone stats...");
        DataSource.getInstance().updateZoneStats(stats);
    }

    public static int idForNewZone() {
        incrementLastZoneID();
        return lastZoneID();
    }

    public static int lastZoneID() {
        int id = ((Integer)DataSource.getInstance().zoneStats().get("last_zone_id")).intValue();
        CSLogger.sharedLogger().fine("Returning last zone ID (" + id + ")");
        return id;
    }

    private static void incrementLastZoneID() {
        int id = lastZoneID()+1;
        CSLogger.sharedLogger().fine("Incrementing last zone ID to (" + id + ")");
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_LASTZONEID, id);
        DataSource.getInstance().updateZoneStats(zoneStats);
    }

    //

    public static HashMap roadStats() {
        CSLogger.sharedLogger().fine("Getting road stats");
        return DataSource.getInstance().roadStats();
    }

    public static void insertRoadStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Inserting road stats...");
        DataSource.getInstance().insertRoadStats(stats);
    }

    public static void updateRoadStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Updating road stats...");
        DataSource.getInstance().updateRoadStats(stats);
    }

    //

    public static Zone[] zones() {
        return DataSource.getInstance().zones();
    }

    public static ResidentialZone[] residentialZones() {
        return DataSource.getInstance().residentialZones();
    }

    public static CommercialZone[] commercialZones() {
        return DataSource.getInstance().commercialZones();
    }

    public static IndustrialZone[] industrialZones() {
        return DataSource.getInstance().industrialZones();
    }

    public static Zone[] zonesMatchingCriteria(String criteria) {
        //         CSLogger.sharedLogger().fine("Running query for zones matching criteria (" + criteria + ")");
        return DataSource.getInstance().zonesMatchingCriteria(criteria);
    }

    public static Zone[] zonesInArea(Point start, int radius) {
        return zonesMatchingCriteria("x >= " + (start.x-radius-2) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius-2) + " AND y <= " + (start.y+radius));
    }

    public static Zone[] zonesInArea(Point start, int radius, int zone) {
        return zonesMatchingCriteria("x >= " + (start.x-radius-2) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius-2) + " AND y <= " + (start.y+radius) + " AND zone = " + zone);
    }

    public static Zone[] zonesInAreaOfZone(Zone zone, int radius, int type) {
        Point start = zone.origin();
        return zonesMatchingCriteria("x >= " + (start.x-radius-4) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius-4) + " AND y <= " + (start.y+radius) + " AND zone = " + type);
    }

    public static Zone[] zonesInAreaOfZone(Zone zone, int radius, int zone1, int zone2) {
        Point start = zone.origin();
        return zonesMatchingCriteria("x >= " + (start.x-radius-4) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius-4) + " AND y <= " + (start.y+radius) + " AND (zone = " + zone1 + " OR zone = " + zone2 + ")");
    }

    public static Zone[] zonesInArea(Point start, int radius, int zone1, int zone2) {
        return zonesMatchingCriteria("x >= " + (start.x-radius) + " AND x <= " + (start.x+radius) + " AND y >= " + (start.y-radius) + " AND y <= " + (start.y+radius) + " AND (zone = " + zone1 + " OR zone = " + zone2 + ")");
    }

    public static Zone[] zonesAroundZone(Zone zone) {
        Tile[] tiles = tilesAroundZone(zone);
        HashSet set = new HashSet();
        for (Tile tile : tiles) {
            Zone z = zoneWithTile(tile);
            if (z != null) set.add(z);
        }
        Zone[] zones = new Zone[set.size()];
        set.toArray(zones);
        return zones;
    }

    public static void insertZone(Zone zone) {
        CSLogger.sharedLogger().fine("Inserting zone...");
        DataSource.getInstance().insertZone(zone);
    }

    public static void insertZone(int id, int type, int x, int y, int powered, int capacity) {
        //         CSLogger.sharedLogger().fine("Inserting zone (" + id + ") of type (" + type + ")");
        HashMap zone = new HashMap(ZONES_PARAMS.length);
        zone.put(ZONES_ID, id);
        zone.put(ZONES_ZONE, type);
        zone.put(ZONES_AGE, 0);
        zone.put(ZONES_POWERED, powered);
        zone.put(ZONES_X, x);
        zone.put(ZONES_Y, y);
        zone.put(ZONES_SCORE, 0);
        zone.put(ZONES_POLLUTION, 0);
        zone.put(ZONES_FIRE_PROTECTION, 0);
        zone.put(ZONES_POLICE_PROTECTION, 0);
        zone.put(ZONES_CRIME, 0);
        zone.put(ZONES_FOOD, 0);
        zone.put(ZONES_JOBS, 0);
        zone.put(ZONES_ALLOCATION, 0);
        zone.put(ZONES_CAPACITY, capacity);
        zone.put(ZONES_STAGE, 0);
        insertZone(new Zone(zone));
    }

    public static void updateZone(Zone zone) {
        CSLogger.sharedLogger().fine("Updating zone...");
        DataSource.getInstance().updateZone(zone);
    }

    public static void updateZones(Zone[] zones) {
        CSLogger.sharedLogger().fine("Updating " + zones.length + " zones...");
        DataSource.getInstance().updateZones(zones);
    }

    public static void deleteZone(int id, int type) {
        CSLogger.sharedLogger().fine("Deleting zone with ID (" + id + ")");
        DataSource.getInstance().deleteZoneWithID(id);
        deleteZoneTileWithID(id);
        switch (type) {
            case CommercialZone.TYPE_ID: CommercialZone.subtractFromCount(9);
            break;
            case IndustrialZone.TYPE_ID: IndustrialZone.subtractFromCount(9);
            break;
            case ResidentialZone.TYPE_ID: ResidentialZone.subtractFromCount(9);
            break;
            default:
            break;
        }
    }

    //

    public static int[] tilesInZoneWithID(int id) {
        CSLogger.sharedLogger().fine("Getting zone_tile with ID: " + id);
        //         return DataSource.getInstance().tilesInZoneWithID(id);
        try {
            return (int[])zoneTileCache.get(id + "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Zone zoneWithTile(Tile tile) {
        return DataSource.getInstance().zoneWithTile(tile);
    }

    public static void insertZoneTiles(HashMap[] zoneTiles) {
        CSLogger.sharedLogger().fine("Inserting zone_tile...");
        new ZoneTileDBInsertThread(zoneTiles).start();
    }

    public static void deleteZoneTileWithID(int zoneID) {
        CSLogger.sharedLogger().fine("Deleting zone_tile with ID: " + zoneID);
        zoneTileCache.invalidate(zoneID + "");
        new ZoneTileDBDeleteThread(zoneID).start();
    }

    //

    public static HashMap cityStats() {
        CSLogger.sharedLogger().fine("Returning city stats");
        return (HashMap)get(CITYSTATS);
    }

    public static void insertCityStats(HashMap cityStats) {
        CSLogger.sharedLogger().fine("Inserting city stats");
        DataSource.getInstance().insertCityStats(cityStats);
    }

    public static void updateCityStats(HashMap cityStats) {
        CSLogger.sharedLogger().fine("Updating city stats");
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

        if (key == MAPSIZE) {
            data = DataSource.getInstance().mapSize();
        }
        else if (key == METADATA) {
            data = DataSource.getInstance().mapMetadata();
        }
        else if (key == TILES) {
            data = DataSource.getInstance().tiles();
        }
        else if (key == CITYSTATS) {
            data = DataSource.getInstance().cityStats();
        }

        return data;
    }
}
