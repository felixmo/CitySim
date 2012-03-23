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

    // * Constants and class & instance variables *

    // Database properties
    private String dbName;
    private boolean dbIsNew;

    private Connection connection;
    private boolean connectionIsOpen;

    private static final String mapsDirectory = "maps";

    // * END of constants and class & instance variables *

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

    // ACCESSORS

    public String dbName() {
        return dbName;
    }

    public boolean dbIsNew() {
        return dbIsNew;
    }
    
    public boolean connectionIsOpen() {
        
        return connectionIsOpen;
    }
    
    // END of ACCESSORS

    // Check if a file of a given name exists
    private boolean fileExists(String fileName) {

        return new File(fileName).exists();
    }

    // Open a connection to the database, create it if necessary
    private void openConnection(String dbName) {

        System.out.println("Opening connection to db...");

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
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    public void closeConnection() {
        
        System.out.println("Closing connection to db...");
        
        try {
            connection.close();
            connectionIsOpen = false;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    public void resumeConnection() {
        
        openConnection(dbName);
    }

    // Create the database's schema
    private void createSchema() {

        System.out.println("Created schema for db...");

        try {
            PreparedStatement tiles = connection.prepareStatement("CREATE TABLE tiles (id, x, y, type, traverseable);");
            tiles.execute();
            PreparedStatement mapSize = connection.prepareStatement("CREATE TABLE map_size (rows, columns);");
            mapSize.execute();
            PreparedStatement mapMetadata = connection.prepareStatement("CREATE TABLE map_metadata (name);");
            mapMetadata.execute();
            PreparedStatement cityStats = connection.prepareStatement("CREATE TABLE city_stats (minutes, hours, days, months, years, population);");
            cityStats.execute();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - MAP -

    public LinkedHashMap mapSize() {

        System.out.println("Getting map size from db...");

        try {

            List results = (List) new QueryRunner().query(connection, "SELECT * from map_size", new MapListHandler());
            LinkedHashMap mapSize = new LinkedHashMap();
            Map row = (Map)results.listIterator().next();
            mapSize.putAll(row);

            return mapSize;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
        
        return null;
    }

    public void insertMapSize(LinkedHashMap mapSize) {

        System.out.println("Inserting map size into db...");

        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_size VALUES (?, ?);");
            // 1. rows
            // 2. columns

            statement.setInt(1, (Integer)mapSize.get("rows"));
            statement.setInt(2, (Integer)mapSize.get("columns"));
            statement.addBatch();
            statement.executeBatch();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public LinkedHashMap mapMetadata() {

        System.out.println("Getting map metadata from db...");

        try {
            List results = (List) new QueryRunner().query(connection, "SELECT * from map_metadata", new MapListHandler());
            LinkedHashMap mapMetadata = new LinkedHashMap();
            Map row = (Map)results.listIterator().next();
            mapMetadata.putAll(row);

            return mapMetadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapMetadata(LinkedHashMap mapMetadata) {

        System.out.println("Inserting map metadata into db...");

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO map_metadata VALUES (?);");
            // 1. name

            statement.setString(1, (String)mapMetadata.get("name"));
            statement.addBatch();
            statement.executeBatch();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - TILES -

    // Iterates through a given ArrayList that contains the game map and inserts it into the database
    // Should only be used after the inital map generation
    public void insertTiles(ArrayList<ArrayList<Tile>> tiles) {

        System.out.println("Inserting tiles into db...");

        try {
            
            connection.setAutoCommit(false);
            
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tiles VALUES (?, ?, ?, ?, ?);");
            // 1. id
            // 2. x
            // 3. y
            // 4. type
            // 5. traverseable

            for (int x = 0; x < tiles.size(); x++) {

                for (int y = 0; y < tiles.get(x).size(); y++) {

                    Tile tile = tiles.get(x).get(y);
                    statement.setInt(1, tile.dbID());
                    statement.setInt(2, tile.position().x());
                    statement.setInt(3, tile.position().y());
                    statement.setInt(4, tile.type());
                    statement.setInt(5, tile.traverseable() ? 1 : 0);
                    statement.addBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            
            connection.setAutoCommit(true);
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public ArrayList<ArrayList<Tile>> tiles() {

        System.out.println("Loading tiles from db...");

        try {

            LinkedHashMap mapSize = mapSize();
            Point size = new Point((Integer)mapSize.get("columns"), (Integer)mapSize.get("rows"));

            ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>(size.x());
            for (int i = 0; i < size.x(); i++) {
                tiles.add(new ArrayList<Tile>(size.y()));
            }

            List results = (List) new QueryRunner().query(connection, "SELECT * from tiles", new MapListHandler());
            Point pos = new Point(0, 0);
            for (int i = 0; i < results.size(); i++) {

                Map row = (Map)results.get(i);
                pos.setX((Integer)row.get("x"));
                pos.setY((Integer)row.get("y"));

                tiles.get(pos.x()).add(pos.y(), new Tile(new Point((Integer)row.get("x"), (Integer)row.get("y")), (Integer)row.get("type"), (Integer)row.get("traverseable") == 0 ? false : true));
            }

            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    // - CITY -

    public LinkedHashMap cityStats() {

        System.out.println("Loading city stats from db...");

        try {

            List results = (List) new QueryRunner().query(connection, "SELECT * from city_stats", new MapListHandler());
            LinkedHashMap cityStats = new LinkedHashMap();
            Map row = (Map)results.listIterator().next();
            cityStats.putAll(row);
            return cityStats;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertCityStats(LinkedHashMap cityStats) {

        System.out.println("Inserting city stats to db...");

        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO city_stats VALUES (?, ?, ?, ?, ?, ?);");
            // 1. minutes
            // 2. hours
            // 3. days
            // 4. months
            // 5. years
            // 6. population

            statement.setInt(1, (Integer)cityStats.get("minutes"));
            statement.setInt(2, (Integer)cityStats.get("hours"));
            statement.setInt(3, (Integer)cityStats.get("days"));
            statement.setInt(4, (Integer)cityStats.get("months"));
            statement.setInt(5, (Integer)cityStats.get("years"));
            statement.setInt(6, (Integer)cityStats.get("population"));
            statement.addBatch();
            statement.executeBatch();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateCityStats(LinkedHashMap cityStats) {

        System.out.println("Updating city stats to db...");

        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE city_stats SET minutes = ?");
            statement.setInt(1, (Integer)cityStats.get("minutes"));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET hours = ?");
            statement.setInt(1, (Integer)cityStats.get("hours"));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET days = ?");
            statement.setInt(1, (Integer)cityStats.get("days"));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET months = ?");
            statement.setInt(1, (Integer)cityStats.get("months"));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET years = ?");
            statement.setInt(1, (Integer)cityStats.get("years"));
            statement.executeUpdate();

            statement = connection.prepareStatement("UPDATE city_stats SET population = ?");
            statement.setInt(1, (Integer)cityStats.get("population"));
            statement.executeUpdate();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
