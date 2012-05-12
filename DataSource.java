import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedHashMap;
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

        this.dbName = dbName;

        // Check if the database already exists
        dbIsNew = !fileExists(mapsDirectory + "/" + this.dbName + ".db");

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
     * DB. OPS. *
     */

    // Create the database's schema
    private void createSchema() {

        CSLogger.sharedLogger().finer("Creating schema for DB (\"" + dbName + "\")");

        try {
            PreparedStatement tiles = connection.prepareStatement("CREATE TABLE tiles (id, x, y, type, zone);");
            tiles.execute();
            PreparedStatement mapSize = connection.prepareStatement("CREATE TABLE map_size (" + Data.MAPSIZE_ROWS + ", " + Data.MAPSIZE_COLUMNS + ");");
            mapSize.execute();
            PreparedStatement mapMetadata = connection.prepareStatement("CREATE TABLE map_metadata ("+ Data.METADATA_NAME +");");
            mapMetadata.execute();
            PreparedStatement cityStats = connection.prepareStatement("CREATE TABLE city_stats (" + Data.CITYSTATS_DAYS + ", " + Data.CITYSTATS_MONTHS + ", " + Data.CITYSTATS_YEARS + ", " + Data.CITYSTATS_POPULATION + ", " + Data.CITYSTATS_CASH + ");");
            cityStats.execute();

            CSLogger.sharedLogger().finer("Finished creating schema for DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - MAP -

    public LinkedHashMap mapSize() {

        // Returns map with keys (below), containing values expressing the map size
        // 1. rows
        // 2. columns

        CSLogger.sharedLogger().finer("Retrieving map size from DB (\"" + dbName + "\")...");

        try {

            List results = (List) new QueryRunner().query(connection, "SELECT * from map_size", new MapListHandler());
            LinkedHashMap mapSize = new LinkedHashMap(2);
            Map row = (Map)results.listIterator().next();
            mapSize.putAll(row);

            CSLogger.sharedLogger().finer("Finished retrieving map size from DB (\"" + dbName + "\"). Got map size of " + mapSize.get(Data.MAPSIZE_ROWS) + "x" + mapSize.get(Data.MAPSIZE_COLUMNS));

            return mapSize;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapSize(LinkedHashMap mapSize) {

        CSLogger.sharedLogger().finer("Inserting map size into DB (\"" + dbName + "\") of " + mapSize.get(Data.MAPSIZE_ROWS) + " x " + mapSize.get(Data.MAPSIZE_COLUMNS));

        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_size VALUES (?, ?);");
            // 1. rows
            // 2. columns

            statement.setInt(1, (Integer)mapSize.get(Data.MAPSIZE_ROWS));
            statement.setInt(2, (Integer)mapSize.get(Data.MAPSIZE_COLUMNS));
            statement.addBatch();
            statement.executeBatch();

            CSLogger.sharedLogger().finer("Finished inserting map size into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public LinkedHashMap mapMetadata() {

        CSLogger.sharedLogger().finer("Retrieving map metadata from DB (\"" + dbName + "\")");

        try {
            List results = (List) new QueryRunner().query(connection, "SELECT * from map_metadata", new MapListHandler());
            LinkedHashMap mapMetadata = new LinkedHashMap(1);
            Map row = (Map)results.listIterator().next();
            mapMetadata.putAll(row);

            CSLogger.sharedLogger().finer("Finished retrieving map metadata from DB (\"" + dbName + "\")"); 

            return mapMetadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapMetadata(LinkedHashMap mapMetadata) {

        CSLogger.sharedLogger().finer("Inserting map metadata into DB (\"" + dbName + "\")");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_metadata VALUES (?);");
            // 1. name

            statement.setString(1, (String)mapMetadata.get("name"));
            statement.addBatch();
            statement.executeBatch();

            CSLogger.sharedLogger().finer("Finished inserting map metadata into DB (\"" + dbName + "\")");
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

            PreparedStatement statement = connection.prepareStatement("INSERT INTO tiles VALUES (?, ?, ?, ?, ?);");
            // 1. id
            // 2. x
            // 3. y
            // 4. type

            for (int x = 0; x < tiles.size(); x++) {

                for (int y = 0; y < tiles.get(x).size(); y++) {

                    Tile tile = tiles.get(x).get(y);

                    statement.setInt(1, tile.dbID());
                    statement.setInt(2, tile.position().x);
                    statement.setInt(3, tile.position().y);
                    statement.setInt(4, tile.type());
                    statement.setInt(5, tile.zone());
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().finer("Finished inserting map tiles into DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public void updateTile(Tile tile) {

        CSLogger.sharedLogger().finer("Updating tile (" + tile.dbID() + ") @ (" + tile.position().x + ", " + tile.position().y + ") of zone (" + tile.zone() + "), in DB (\"" + dbName + "\")...");

        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE tiles SET type = ?, zone = ? WHERE id = ?");
            statement.setInt(1, tile.type());
            statement.setInt(2, tile.zone());
            statement.setInt(3, tile.dbID());
            statement.executeUpdate();

            CSLogger.sharedLogger().finer("Finished updating tile " + tile.position().toString() + " in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().finer("Updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("UPDATE tiles SET type = ?, zone = ? WHERE id = ?");
            for (int x = 0; x < tiles.size(); x++) {
                for (int y = 0; y < tiles.get(x).size(); y++) {
                    statement.setInt(1, ((Tile)tiles.get(x).get(y)).type());
                    statement.setInt(2, ((Tile)tiles.get(x).get(y)).zone());
                    statement.setInt(3, ((Tile)tiles.get(x).get(y)).dbID());     
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();

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

            LinkedHashMap mapSize = mapSize();
            Point size = new Point((Integer)mapSize.get("columns"), (Integer)mapSize.get("rows"));

            ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>(size.x);
            for (int i = 0; i < size.x; i++) {
                tiles.add(new ArrayList<Tile>(size.y));
            }

            List results = (List) new QueryRunner().query(connection, "SELECT * from tiles", new MapListHandler());
            Point pos = new Point(0, 0);
            for (int i = 0; i < results.size(); i++) {

                Map row = (Map)results.get(i);
                pos.setLocation((Integer)row.get("x"), (Integer)row.get("y"));

                tiles.get(pos.x).add(pos.y, new Tile(((Integer)row.get("id")).intValue(), new Point(((Integer)row.get("x")).intValue(), ((Integer)row.get("y")).intValue()), ((Integer)row.get("type")).intValue(), ((Integer)row.get("zone")).intValue()));
            }

            CSLogger.sharedLogger().finer("Finished retrieving map tiles from DB (\"" + dbName + "\")");

            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }
/*
    public ArrayList<Tiles> tiles(Tile[] tiles) {

        CSLogger.sharedLogger().info("Retrieiving + " tiles.length + " map tiles from DB  (\"" + dbName + "\")...");

        try {

            ArrayList<Tiles> tiles = new ArrayList<Tiles>(tiles.length);
            
            List results = (List) new QueryRunner().query(connection, "SELECT * from tiles WHERE id BETWEEN " + tiles[0].dbID() + " AND " + tiles[tiles.length-1].dbID(), new MapListHandler());
            Point pos = new Point(0, 0);
            for (int i = 0; i < results.size(); i++) {

                Map row = (Map)results.get(i);
                pos.setLocation((Integer)row.get("x"), (Integer)row.get("y"));

                tiles.get(pos.x).add(pos.y, new Tile(((Integer)row.get("id")).intValue(), new Point(((Integer)row.get("x")).intValue(), ((Integer)row.get("y")).intValue()), ((Integer)row.get("type")).intValue(), ((Integer)row.get("zone")).intValue()));
            }

            CSLogger.sharedLogger().finer("Finished retrieving " + tiles.length + " map tiles from DB (\"" + dbName + "\")");

            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }
    */

    // - CITY -

    public LinkedHashMap cityStats() {

        CSLogger.sharedLogger().finer("Retrieving city stats from DB (\"" + dbName + "\")...");

        try {

            List results = (List) new QueryRunner().query(connection, "SELECT * from city_stats", new MapListHandler());
            LinkedHashMap cityStats = new LinkedHashMap(5);
            Map row = (Map)results.listIterator().next();
            cityStats.putAll(row);

            CSLogger.sharedLogger().finer("Finished retrieving city stats from DB (\"" + dbName + "\")");

            return cityStats;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertCityStats(LinkedHashMap cityStats) {

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

            CSLogger.sharedLogger().finer("Finished inserting city stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateCityStats(LinkedHashMap cityStats) {

        CSLogger.sharedLogger().finer("Updating city stats in DB (\"" + dbName + "\")");

        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE city_stats SET days = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_DAYS));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET months = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_MONTHS));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET years = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_YEARS));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET population = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_POPULATION));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET cash = ?");
            statement.setInt(1, (Integer)cityStats.get(Data.CITYSTATS_CASH));
            statement.executeUpdate();

            CSLogger.sharedLogger().finer("Finished updating city stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
