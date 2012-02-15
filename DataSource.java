import java.sql.*;
import java.io.File;
import java.util.ArrayList;

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
    boolean dbIsNew;
    
    private Connection connection;

    private static final String mapsDirectory = "maps"
    
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

    // Check if a file of a given name exists
    private boolean fileExists(String fileName) {

        return new File(fileName).exists();
    }

    // Open a connection to the database, create it if necessary
    private void openConnection(String dbName) {

        try {
            Class.forName("org.sqlite.JDBC");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + mapsDirectory + "/" + dbName + ".db");
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Create the database's schema
    private void createSchema() {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE tiles (x, y, type, traverseable);");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Iterates through a given ArrayList that contains the game map and inserts it into the database
    // Should only be used after the inital map generation
    public void insertTiles(ArrayList<ArrayList<Tile>> tiles) {

        try {

            PreparedStatement statement = connection.prepareStatement("insert into tiles values (?, ?, ?, ?);");
            // 1. x
            // 2. y
            // 3. type
            // 4. traverseable

            for (int x = 0; x < tiles.size(); x++) {
                
                for (int y = 0; y < tiles.get(x).size(); y++) {
                    
                    Tile tile = tiles.get(x).get(y);
                    statement.setInt(1, tile.position().x());
                    statement.setInt(2, tile.position().y());
                    statement.setInt(3, tile.type());
                    statement.setInt(4, tile.traverseable() ? 1 : 0);
                    statement.addBatch();
                }
            }

            connection.setAutoCommit(false);
            statement.executeBatch();
            connection.commit();

        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

}
