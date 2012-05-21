import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import java.awt.Point;

/**
 * DataSource
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-15-2012
 * 
 * An interface to the game's SQLite database
 * 
 */

public class DataSource  
{

    // ---------------------------------------------------------------------------------------------------------------

    private static DataSource instance;

    /*
     * INSTANCE VARIABLES *
     */

    // Database properties
    private static String dbName;       // Database name
    private boolean dbIsNew;            // Specifies if database was just created

    private Connection connection;
    private boolean connectionIsOpen;   // Tells if connection is currently open

    /*
     * CONSTANTS *
     */

    private static final String mapsDirectory = "maps";

    // ---------------------------------------------------------------------------------------------------------------

    public DataSource(String dbName) {

        instance = this;

        this.dbName = dbName;

        // Check if the database and the "maps" directory exists; create them if they don't
        if (new File(mapsDirectory).isDirectory()) {
            CSLogger.sharedLogger().info("Maps directory exists; checking if map exists...");
            dbIsNew = !fileExists(mapsDirectory + "/" + this.dbName + ".db");
        }
        else {
            CSLogger.sharedLogger().info("Maps directory does not exist; creating it now...");
            new File(mapsDirectory).mkdir();
            dbIsNew = true;
        }

        openConnection(this.dbName);

        // If the database was just created, create the schema for it
        if (dbIsNew) {
            createSchema();
        }
    }

    /*
     * ACCESSORS *
     */

    public String dbName() {
        return dbName;
    }

    public boolean dbIsNew() {
        return dbIsNew;
    }

    public boolean connectionIsOpen() {

        return connectionIsOpen;
    }

    public static DataSource getInstance() {
        return instance;
    }

    /*
     * HELPERS *
     */

    // Check if a file of a given name exists
    private boolean fileExists(String fileName) {

        return new File(fileName).exists();
    }

    /*
     * CONNECTION *
     */

    // Open a connection to the database, create it if necessary
    private void openConnection(String dbName) {

        CSLogger.sharedLogger().info("Opening connection to DB named: \"" + dbName + "\"...");

        try {
            Class.forName("org.sqlite.JDBC");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + mapsDirectory + "/" + dbName + ".db");
            connection.setAutoCommit(true);
            connectionIsOpen = true;

            CSLogger.sharedLogger().info("Connection to DB (\"" + dbName + "\") has been established.");
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void closeConnection() {

        CSLogger.sharedLogger().info("Closing connection to DB named: \"" + dbName + "\"...");

        try {
            connection.close();
            connectionIsOpen = false;

            CSLogger.sharedLogger().info("Connection to DB (\"" + dbName + "\") has been closed.");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void resumeConnection() {

        openConnection(dbName);
    }

    /*
     * DB. OPS.

     *
     */

    // Create the database's schema
    private void createSchema() {

        CSLogger.sharedLogger().finer("Creating schema for DB (\"" + dbName + "\")");

        try {

            // Zones
            PreparedStatement zones = connection.prepareStatement("CREATE TABLE zone_tile (zone_id, tile_id);");
            zones.execute();
            zones.close();

            // Zone stats
            PreparedStatement zoneStats = connection.prepareStatement("CREATE TABLE zone_stats (residential_count, industrial_count, commercial_count, last_zone_id);");
            zoneStats.execute();
            zoneStats.close();

            // Road stats
            PreparedStatement roadStats = connection.prepareStatement("CREATE TABLE road_stats (street_count);");
            roadStats.execute();
            roadStats.close();

            // Tiles
            PreparedStatement tiles = connection.prepareStatement("CREATE TABLE tiles (id, x, y, type, zone, zone_id, road);");
            tiles.execute();
            tiles.close();

            // Map size
            PreparedStatement mapSize = connection.prepareStatement("CREATE TABLE map_size (" + Data.MAPSIZE_ROWS + ", " + Data.MAPSIZE_COLUMNS + ");");
            mapSize.execute();
            mapSize.close();

            // Map metadata
            PreparedStatement mapMetadata = connection.prepareStatement("CREATE TABLE map_metadata ("+ Data.METADATA_NAME + ");");
            mapMetadata.execute();
            mapMetadata.close();

            // City stats.
            PreparedStatement cityStats = connection.prepareStatement("CREATE TABLE city_stats (" + Data.CITYSTATS_DAYS + ", " + Data.CITYSTATS_MONTHS + ", " + Data.CITYSTATS_YEARS + ", " + Data.CITYSTATS_POPULATION + ", " + Data.CITYSTATS_CASH + ");");
            cityStats.execute();
            cityStats.close();

            CSLogger.sharedLogger().finer("Finished creating schema for DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - MAP -

    public HashMap mapSize() {

        // Returns map with keys (below), containing values expressing the map size
        // 1. rows
        // 2. columns

        CSLogger.sharedLogger().finer("Retrieving map size from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from map_size", new MapListHandler());
            HashMap mapSize = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().finer("Finished retrieving map size from DB (\"" + dbName + "\"). Got map size of " + mapSize.get(Data.MAPSIZE_ROWS) + "x" + mapSize.get(Data.MAPSIZE_COLUMNS));
            return mapSize;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapSize(HashMap mapSize) {
        CSLogger.sharedLogger().finer("Inserting map size into DB (\"" + dbName + "\") of " + mapSize.get(Data.MAPSIZE_ROWS) + " x " + mapSize.get(Data.MAPSIZE_COLUMNS));
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_size VALUES (?, ?);");
            // 1. rows
            // 2. columns

            statement.setInt(1, (Integer)mapSize.get(Data.MAPSIZE_ROWS));
            statement.setInt(2, (Integer)mapSize.get(Data.MAPSIZE_COLUMNS));
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished inserting map size into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public HashMap mapMetadata() {

        CSLogger.sharedLogger().finer("Retrieving map metadata from DB (\"" + dbName + "\")");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List) runner.query(connection, "SELECT * from map_metadata", new MapListHandler());

            HashMap mapMetadata = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().finer("Finished retrieving map metadata from DB (\"" + dbName + "\")"); 

            return mapMetadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().finer("Inserting map metadata into DB (\"" + dbName + "\")");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_metadata VALUES (?);");
            // 1. name

            statement.setString(1, (String)mapMetadata.get("name"));
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished inserting map metadata into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().finer("Updating map metadata in DB (\"" + dbName + "\")");

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE map_metadata SET name = ?;");
            // 1. name

            statement.setString(1, (String)mapMetadata.get("name"));
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished updating map metadata in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONES - 

    public int[] tilesInZoneWithID(int id) {
        CSLogger.sharedLogger().info("Retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_tile WHERE zone_id = " + id, new MapListHandler());
            int[] tiles = new int[results.size()+1];
            for (int i = 0; i < results.size(); i++) {
                Map row = (Map)results.get(i);
                tiles[i] = ((Integer)row.get("tile_id")).intValue();
            }
            CSLogger.sharedLogger().info("Finished retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\").");
            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneWithTiles(HashMap[] zoneTiles) {
        CSLogger.sharedLogger().finer("Inserting zone into DB (\"" + dbName + "\")");

        try {

            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO zone_tile VALUES (?, ?);");
            // 1. zone_id
            // 2. tile_id

            for (HashMap zoneTile : zoneTiles) {
                statement.setInt(1, ((Integer)zoneTile.get("zone_id")).intValue());
                statement.setInt(2, ((Integer)zoneTile.get("tile_id")).intValue());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().finer("Finished inserting zone into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteZoneWithID(int id) {

        CSLogger.sharedLogger().finer("Deleting zone_tile with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            runner.update(connection, "DELETE FROM zone_tile WHERE zone_id = " + id);

            CSLogger.sharedLogger().finer("Finished deleting zone from DB (\"" + dbName + "\").");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONE STATS -
    public HashMap zoneStats() {
        CSLogger.sharedLogger().finer("Retrieving zone stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_stats", new MapListHandler());
            HashMap zone = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().info("Finished retrieving zone stats from DB (\"" + dbName + "\").");
            return zone;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneStats(HashMap stats) {
        CSLogger.sharedLogger().finer("Inserting zone stats into DB (\"" + dbName + "\")...");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO zone_stats VALUES (?, ?, ?, ?);");
            // 1. residential_count
            // 2. industrial_count
            // 3. commercial_count
            // 4. last_zone_id

            statement.setInt(1, ((Integer)stats.get(Data.ZONESTATS_RESIDENTIALCOUNT)).intValue());
            statement.setInt(2, ((Integer)stats.get(Data.ZONESTATS_INDUSTRIALCOUNT)).intValue());
            statement.setInt(3, ((Integer)stats.get(Data.ZONESTATS_COMMERCIALCOUNT)).intValue());
            statement.setInt(4, ((Integer)stats.get(Data.ZONESTATS_LASTZONEID)).intValue());
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished inserting zone stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateZoneStats(HashMap stats) {
        CSLogger.sharedLogger().finer("Updating zone stats in DB (\"" + dbName + "\")...");

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE zone_stats SET " + Data.ZONESTATS_RESIDENTIALCOUNT + " = ?, " + Data.ZONESTATS_INDUSTRIALCOUNT + " = ?, " + Data.ZONESTATS_COMMERCIALCOUNT + " = ?, " + Data.ZONESTATS_LASTZONEID + " = ?;");
            // 1. residential_count
            // 2. industrial_count
            // 3. commercial_count
            // 4. last_zone_id

            statement.setInt(1, ((Integer)stats.get(Data.ZONESTATS_RESIDENTIALCOUNT)).intValue());
            statement.setInt(2, ((Integer)stats.get(Data.ZONESTATS_INDUSTRIALCOUNT)).intValue());
            statement.setInt(3, ((Integer)stats.get(Data.ZONESTATS_COMMERCIALCOUNT)).intValue());
            statement.setInt(4, ((Integer)stats.get(Data.ZONESTATS_LASTZONEID)).intValue());
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished updating zone stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ROAD STATS -

    public HashMap roadStats() {
        CSLogger.sharedLogger().finer("Retrieving road stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM road_stats", new MapListHandler());
            HashMap road = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().info("Finished retrieving road stats from DB (\"" + dbName + "\").");
            return road;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertRoadStats(HashMap stats) {
        CSLogger.sharedLogger().finer("Inserting road stats into DB (\"" + dbName + "\")...");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO road_stats VALUES (?);");
            // 1. street_count

            statement.setInt(1, ((Integer)stats.get(Data.ROADSTATS_STREETCOUNT)).intValue());
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished inserting road stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateRoadStats(HashMap stats) {
        CSLogger.sharedLogger().finer("Updating road stats in DB (\"" + dbName + "\")...");

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE road_stats SET " + Data.ROADSTATS_STREETCOUNT + " = ?;");
            // 1. street_count

            statement.setInt(1, ((Integer)stats.get(Data.ROADSTATS_STREETCOUNT)).intValue());
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished updating road stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - TILES -

    // Iterates through a given ArrayList that contains the game map and inserts it into the database
    // Should only be used after the inital map generation
    public void insertTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().finer("Inserting map tiles into DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO tiles VALUES (?, ?, ?, ?, ?, ?, ?);");
            // 1. id
            // 2. x
            // 3. y
            // 4. type
            // 5. zone
            // 6. road

            for (int x = 0; x < tiles.size(); x++) {

                for (int y = 0; y < tiles.get(x).size(); y++) {

                    Tile tile = tiles.get(x).get(y);

                    statement.setInt(1, tile.dbID());
                    statement.setInt(2, tile.position().x);
                    statement.setInt(3, tile.position().y);
                    statement.setInt(4, tile.type());
                    statement.setInt(5, tile.zone());
                    statement.setInt(6, tile.zoneID());
                    statement.setInt(7, tile.road());
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().finer("Finished inserting map tiles into DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public void updateTile(Tile tile) {

        CSLogger.sharedLogger().finer("Updating tile in DB (\"" + dbName + "\")...");

        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE tiles SET type = ?, zone = ?, zone_id = ?, road = ? WHERE id = ?");
            statement.setInt(1, tile.type());
            statement.setInt(2, tile.zone());
            statement.setInt(3, tile.zoneID());
            statement.setInt(4, tile.road());
            statement.setInt(5, tile.dbID());
            statement.executeUpdate();
            statement.close();

            CSLogger.sharedLogger().finer("Finished updating tile in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().finer("Updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("UPDATE tiles SET type = ?, zone = ?, zone_id = ?, road = ? WHERE id = ?");
            for (int x = 0; x < tiles.size(); x++) {
                for (int y = 0; y < tiles.get(x).size(); y++) {
                    statement.setInt(1, ((Tile)tiles.get(x).get(y)).type());
                    statement.setInt(2, ((Tile)tiles.get(x).get(y)).zone());
                    statement.setInt(3, ((Tile)tiles.get(x).get(y)).zoneID());
                    statement.setInt(4, ((Tile)tiles.get(x).get(y)).road());
                    statement.setInt(5, ((Tile)tiles.get(x).get(y)).dbID());     
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().finer("Finished updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Tile>> tiles() {

        CSLogger.sharedLogger().info("Retrieving map tiles from DB (\"" + dbName + "\")...");

        try {

            HashMap mapSize = mapSize();
            Point size = new Point((Integer)mapSize.get("columns"), (Integer)mapSize.get("rows"));

            ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>(size.x);
            for (int i = 0; i < size.x; i++) {
                tiles.add(new ArrayList<Tile>(size.y));
            }

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from tiles", new MapListHandler());

            Point pos = new Point(0, 0);
            for (int i = 0; i < results.size(); i++) {

                Map row = (Map)results.get(i);
                pos.setLocation((Integer)row.get("x"), (Integer)row.get("y"));

                tiles.get(pos.x).add(pos.y, new Tile(((Integer)row.get("id")).intValue(), new Point(((Integer)row.get("x")).intValue(), ((Integer)row.get("y")).intValue()), ((Integer)row.get("type")).intValue(), ((Integer)row.get("zone")).intValue(), ((Integer)row.get("zone_id")).intValue(), ((Integer)row.get("road")).intValue()));
            }

            CSLogger.sharedLogger().finer("Finished retrieving map tiles from DB (\"" + dbName + "\")");

            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public Tile tileWithID(int id) {

        try {
            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM tiles WHERE id = " + id, new MapListHandler());
            Map row = (Map)results.get(0);
            return new Tile(((Integer)row.get("id")).intValue(), new Point(((Integer)row.get("x")).intValue(), ((Integer)row.get("y")).intValue()), ((Integer)row.get("type")).intValue(), ((Integer)row.get("zone")).intValue(), ((Integer)row.get("zone_id")).intValue(), ((Integer)row.get("road")).intValue());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    // - CITY -

    public HashMap cityStats() {

        CSLogger.sharedLogger().finer("Retrieving city stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from city_stats", new MapListHandler());

            HashMap cityStats = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().finer("Finished retrieving city stats from DB (\"" + dbName + "\")");

            return cityStats;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertCityStats(HashMap cityStats) {

        CSLogger.sharedLogger().finer("Inserting city stats into DB (\"" + dbName + "\")");

        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO city_stats VALUES (?, ?, ?, ?, ?);");
            // 1. days
            // 2. months
            // 3. years
            // 4. population

            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_DAYS));
            statement.setInt(2, (Integer)cityStats.get(Data.CITYSTATS_MONTHS));
            statement.setInt(3, (Integer)cityStats.get(Data.CITYSTATS_YEARS));
            statement.setInt(4, (Integer)cityStats.get(Data.CITYSTATS_POPULATION));
            statement.setInt(5, (Integer)cityStats.get(Data.CITYSTATS_CASH));
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().finer("Finished inserting city stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateCityStats(HashMap cityStats) {

        CSLogger.sharedLogger().finer("Updating city stats in DB (\"" + dbName + "\")");

        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE city_stats SET days = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_DAYS));
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("UPDATE city_stats SET months = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_MONTHS));
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("UPDATE city_stats SET years = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_YEARS));
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("UPDATE city_stats SET population = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_POPULATION));
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("UPDATE city_stats SET cash = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_CASH));
            statement.executeUpdate();
            statement.close();

            CSLogger.sharedLogger().finer("Finished updating city stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
