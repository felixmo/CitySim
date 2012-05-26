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
            CSLogger.sharedLogger().debug("Maps directory exists; checking if map exists...");
            dbIsNew = !fileExists(mapsDirectory + "/" + this.dbName + ".db");
        }
        else {
            CSLogger.sharedLogger().debug("Maps directory does not exist; creating it now...");
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

    // Open a connection to the database; will create SQLite db file if necessary
    private void openConnection(String dbName) {

        CSLogger.sharedLogger().debug("Opening connection to DB named: \"" + dbName + "\"...");

        try {
            Class.forName("org.sqlite.JDBC");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + mapsDirectory + "/" + dbName + ".db");
//             connection = new net.sf.log4jdbc.ConnectionSpy(connection);
            connection.setAutoCommit(true);
            connectionIsOpen = true;

            CSLogger.sharedLogger().debug("Connection to DB (\"" + dbName + "\") has been established.");
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Closes the connection to the database
    public void closeConnection() {

        CSLogger.sharedLogger().debug("Closing connection to DB named: \"" + dbName + "\"...");

        try {
            connection.close();
            connectionIsOpen = false;

            CSLogger.sharedLogger().debug("Connection to DB (\"" + dbName + "\") has been closed.");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Resumes / re-opens the connection to the database
    public void resumeConnection() {

        openConnection(dbName);
    }

    /*
     * DB. OPS.

     *
     */

    // Create the database's schema
    private void createSchema() {

        CSLogger.sharedLogger().debug("Creating schema for DB (\"" + dbName + "\")");

        try {

            for (String table : Data.TABLES_MAPPING.keySet()) {
                String statement_string = "CREATE TABLE " + table + " (";
                int i = 0;
                for (String param : Data.TABLES_MAPPING.get(table)) {
                    statement_string += ((i > 0 ? ", " : "") + param);
                    i++;
                }
                statement_string += ");";
                PreparedStatement statement = connection.prepareStatement(statement_string);
                statement.execute();
                statement.close();
            }

            CSLogger.sharedLogger().debug("Finished creating schema for DB (\"" + dbName + "\")");
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

        CSLogger.sharedLogger().debug("Retrieving map size from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from map_size", new MapListHandler());
            HashMap mapSize = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().debug("Finished retrieving map size from DB (\"" + dbName + "\"). Got map size of " + mapSize.get(Data.MAPSIZE_ROWS) + "x" + mapSize.get(Data.MAPSIZE_COLUMNS));
            return mapSize;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapSize(HashMap mapSize) {

        CSLogger.sharedLogger().debug("Inserting map size into DB (\"" + dbName + "\") of " + mapSize.get(Data.MAPSIZE_ROWS) + " x " + mapSize.get(Data.MAPSIZE_COLUMNS));
        try {

            String statementString = "INSERT INTO " + Data.MAPSIZE + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.MAPSIZE).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.MAPSIZE).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.MAPSIZE)) {
                statement.setObject(i, mapSize.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished inserting map size into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // 

    public HashMap mapMetadata() {

        CSLogger.sharedLogger().debug("Retrieving map metadata from DB (\"" + dbName + "\")");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List) runner.query(connection, "SELECT * from map_metadata", new MapListHandler());

            HashMap mapMetadata = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().debug("Finished retrieving map metadata from DB (\"" + dbName + "\")"); 

            return mapMetadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().debug("Inserting map metadata into DB (\"" + dbName + "\")");

        try {

            String statementString = "INSERT INTO " + Data.METADATA + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.METADATA).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.METADATA).length-1 ? ", " : "");
            }
            statementString += ");";
            System.out.println(statementString);
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.METADATA)) {
                statement.setObject(i, mapMetadata.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished inserting map metadata into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().debug("Updating map metadata in DB (\"" + dbName + "\")");

        try {

            String statementString = "UPDATE " + Data.METADATA + " SET ";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.METADATA).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.METADATA)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.METADATA).length-1 ? ", " : ";"));
            }
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.METADATA)) {
                statement.setObject(i, mapMetadata.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished updating map metadata in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONES -

    public HashMap[] zonesWithCriteria(String criteria) {

        CSLogger.sharedLogger().debug("Running query for zones with criteria (" + criteria + ")");

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE " + criteria, new MapListHandler());

            CSLogger.sharedLogger().debug("Completed query; got " +  results.size() + " zones matching criteria");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        HashMap[] zones = new HashMap[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = (HashMap)results.get(i);
        }

        return zones;
    }

    public void insertZone(HashMap zone) {

        CSLogger.sharedLogger().debug("Inserting zone into DB (\"" + dbName + "\")");

        try {

            String statementString = "INSERT INTO " + Data.ZONES + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ZONES).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.ZONES).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ZONES)) {
                statement.setObject(i, zone.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateZone(HashMap zone) {

        CSLogger.sharedLogger().debug("Updating zone in DB (\"" + dbName + "\")");

        try {

            String statementString = "UPDATE " + Data.ZONES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.ZONES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.ZONES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.ZONES).length-1 ? ", " : " "));
            }
            statementString += "WHERE id = ?;";

            PreparedStatement statement = connection.prepareStatement(statementString);
            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ZONES)) {
                statement.setObject(i, zone.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void increaseZoneAge(int age) {

        CSLogger.sharedLogger().debug("Increasing zone ages (by " + age + ") in DB (\"" + dbName + "\")");

        try {
            new QueryRunner().update(connection, "UPDATE zones SET age = age + " + age);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteZoneWithID(int id) {

        CSLogger.sharedLogger().debug("Deleting zone (" + id + ") from DB (\"" + dbName + "\")");

        try {
            new QueryRunner().update(connection, "DELETE FROM zones WHERE id = " + id);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONE, TILE - 

    public int[] tilesInZoneWithID(int id) {

        CSLogger.sharedLogger().debug("Retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_tile WHERE zone_id = " + id, new MapListHandler());
            int[] tiles = new int[results.size()];
            for (int i = 0; i < results.size(); i++) {
                Map row = (Map)results.get(i);
                tiles[i] = ((Integer)row.get("tile_id")).intValue();
            }
            CSLogger.sharedLogger().debug("Finished retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\").");
            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneTiles(HashMap[] zoneTiles) {

        CSLogger.sharedLogger().debug("Inserting zone into DB (\"" + dbName + "\")");

        try {

            connection.setAutoCommit(false);

            String statementString = "INSERT INTO " + Data.ZONETILE + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ZONETILE).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.ZONETILE).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            for (HashMap zoneTile : zoneTiles) {
                int i = 1;
                for (String param : Data.TABLES_MAPPING.get(Data.ZONETILE)) {
                    statement.setObject(i, zoneTile.get(param));
                    i++;
                }
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().debug("Finished inserting zone into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteZoneTileWithID(int id) {

        CSLogger.sharedLogger().debug("Deleting zone_tile with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            runner.update(connection, "DELETE FROM zone_tile WHERE zone_id = " + id);

            CSLogger.sharedLogger().debug("Finished deleting zone from DB (\"" + dbName + "\").");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONE STATS -

    public HashMap zoneStats() {
        CSLogger.sharedLogger().debug("Retrieving zone stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_stats", new MapListHandler());
            HashMap zone = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().debug("Finished retrieving zone stats from DB (\"" + dbName + "\").");
            return zone;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneStats(HashMap stats) {
        CSLogger.sharedLogger().debug("Inserting zone stats into DB (\"" + dbName + "\")...");

        try {

            String statementString = "INSERT INTO " + Data.ZONESTATS + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ZONESTATS).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.ZONESTATS).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ZONESTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished inserting zone stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateZoneStats(HashMap stats) {
        CSLogger.sharedLogger().debug("Updating zone stats in DB (\"" + dbName + "\")...");

        try {
            String statementString = "UPDATE " + Data.ZONESTATS + " SET ";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ZONESTATS).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.ZONESTATS)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.ZONESTATS).length-1 ? ", " : ";"));
            }
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ZONESTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished updating zone stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ROAD STATS -

    public HashMap roadStats() {
        CSLogger.sharedLogger().debug("Retrieving road stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM road_stats", new MapListHandler());
            HashMap road = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().debug("Finished retrieving road stats from DB (\"" + dbName + "\").");
            return road;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertRoadStats(HashMap stats) {
        CSLogger.sharedLogger().debug("Inserting road stats into DB (\"" + dbName + "\")...");

        try {

            String statementString = "INSERT INTO " + Data.ROADSTATS + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ROADSTATS).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.ROADSTATS).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ROADSTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished inserting road stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateRoadStats(HashMap stats) {
        CSLogger.sharedLogger().debug("Updating road stats in DB (\"" + dbName + "\")...");

        try {
            String statementString = "UPDATE " + Data.ROADSTATS + " SET ";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.ROADSTATS).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.ROADSTATS)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.ROADSTATS).length-1 ? ", " : ";"));
            }
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ROADSTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }
            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished updating road stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - TILES -

    // Iterates through a given ArrayList that contains the game map and inserts it into the database
    // Should only be used after the inital map generation
    public void insertTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().debug("Inserting map tiles into DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            String statementString = "INSERT INTO " + Data.TILES + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.TILES).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.TILES).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            for (int x = 0; x < tiles.size(); x++) {

                for (int y = 0; y < tiles.get(x).size(); y++) {

                    Tile tile = tiles.get(x).get(y);

                    int i = 1;
                    for (String param : Data.TABLES_MAPPING.get(Data.TILES)) {
                        statement.setObject(i, tile.get(param));
                        i++;
                    }

                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().debug("Finished inserting map tiles into DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public void updateTile(Tile tile) {

        CSLogger.sharedLogger().debug("Updating tile in DB (\"" + dbName + "\")...");

        try {

            String statementString = "UPDATE " + Data.TILES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.TILES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.TILES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.TILES).length-1 ? ", " : " "));
            }
            statementString += "WHERE " + Data.TILES_ID +" = ?;";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.TILES)) {
                if (!param.equals(Data.TILES_ID)) {
                    //                     System.out.println("param: " + param + " |  value: " + tile.get(param));
                    statement.setObject(i, tile.get(param));
                    i++;
                }
            }
            statement.setObject(i, tile.get(Data.TILES_ID));
            statement.executeUpdate();
            statement.close();

            CSLogger.sharedLogger().debug("Finished updating tile in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().debug("Updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            String statementString = "UPDATE " + Data.TILES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.TILES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.TILES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.TILES).length-1 ? ", " : " "));
            }
            statementString += "WHERE " + Data.TILES_ID +" = ?;";
            PreparedStatement statement = connection.prepareStatement(statementString);

            for (int x = 0; x < tiles.size(); x++) {
                for (int y = 0; y < tiles.get(x).size(); y++) {
                    Tile tile = (Tile)tiles.get(x).get(y);
                    int i = 1;
                    for (String param : Data.TABLES_MAPPING.get(Data.TILES)) {
                        if (!param.equals(Data.TILES_ID)) {
                            //                             System.out.println("param: " + param + " |  value: " + tile.get(param));
                            statement.setObject(i, tile.get(param));
                            i++;
                        }
                    }
                    statement.setObject(i, tile.get(Data.TILES_ID));
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().debug("Finished updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Tile>> tiles() {

        CSLogger.sharedLogger().debug("Retrieving map tiles from DB (\"" + dbName + "\")...");

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

                HashMap row = (HashMap)results.get(i);
                pos.setLocation((Integer)row.get("x"), (Integer)row.get("y"));
                tiles.get(pos.x).add(pos.y, new Tile(row));
            }

            CSLogger.sharedLogger().debug("Finished retrieving map tiles from DB (\"" + dbName + "\")");

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
            HashMap row = (HashMap)results.get(0);
            return new Tile(row);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    // - CITY -

    public HashMap cityStats() {

        CSLogger.sharedLogger().debug("Retrieving city stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from city_stats", new MapListHandler());

            HashMap cityStats = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().debug("Finished retrieving city stats from DB (\"" + dbName + "\")");

            return cityStats;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertCityStats(HashMap stats) {

        CSLogger.sharedLogger().debug("Inserting city stats into DB (\"" + dbName + "\")");

        try {

            String statementString = "INSERT INTO " + Data.CITYSTATS + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.CITYSTATS).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.CITYSTATS).length-1 ? ", " : "");
            }
            statementString += ");";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.CITYSTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().debug("Finished inserting city stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateCityStats(HashMap stats) {

        CSLogger.sharedLogger().debug("Updating city stats in DB (\"" + dbName + "\")");

        try {

            String statementString = "UPDATE " + Data.CITYSTATS + " SET ";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.CITYSTATS).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.CITYSTATS)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.CITYSTATS).length-1 ? ", " : ";"));
            }
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.CITYSTATS)) {
                statement.setObject(i, stats.get(param));
                i++;
            }
            statement.executeUpdate();
            statement.close();

            CSLogger.sharedLogger().debug("Finished updating city stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
