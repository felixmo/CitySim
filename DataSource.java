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
            connection.setAutoCommit(false);
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Create the database's schema
    private void createSchema() {
        
        System.out.println("Created schema for db...");

        try {
            PreparedStatement tiles = connection.prepareStatement("CREATE TABLE tiles (id, x, y, type, traverseable);");
            tiles.execute();
            PreparedStatement metadata = connection.prepareStatement("CREATE TABLE metadata (rows, columns);");
            metadata.execute();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - MAP -

    public LinkedHashMap metadata() {

        System.out.println("Getting metadata from db...");

        try {

            List results = (List) new QueryRunner().query(connection, "SELECT * from metadata", new MapListHandler());
            LinkedHashMap metadata = new LinkedHashMap();
            Map row = (Map)results.listIterator().next();
            metadata.putAll(row);

            return metadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void setMetadata(LinkedHashMap properties) {

        System.out.println("Inserting metadata into db...");
        
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO metadata VALUES (?, ?);");
            // 1. rows
            // 2. columns

            statement.setInt(1, (Integer)properties.get("rows"));
            statement.setInt(2, (Integer)properties.get("columns"));
            statement.addBatch();
            statement.executeBatch();
            connection.commit();

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
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public ArrayList<ArrayList<Tile>> tiles() {

        System.out.println("Loading tiles from db...");
        
        try {

            LinkedHashMap metadata = metadata();
            Point mapSize = new Point((Integer)metadata.get("columns"), (Integer)metadata.get("rows"));

            ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>(mapSize.x());
            for (int i = 0; i < mapSize.x(); i++) {
                tiles.add(new ArrayList<Tile>(mapSize.y()));
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

}
