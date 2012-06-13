/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

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
import java.util.concurrent.Callable;
// import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

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

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    // ---------------------------------------------------------------------------------------------------------------

    public DataSource(String dbName) {

        instance = this;

        this.dbName = dbName;

        // Check if the database and the "maps" directory exists; create them if they don't
        if (new File(mapsDirectory).isDirectory()) {
            CSLogger.sharedLogger().fine("Maps directory exists; checking if map exists...");
            dbIsNew = !fileExists(mapsDirectory + "/" + this.dbName + ".db");
        }
        else {
            CSLogger.sharedLogger().fine("Maps directory does not exist; creating it now...");
            new File(mapsDirectory).mkdir();
            dbIsNew = true;
        }

        openConnection(this.dbName);

        // If the database was just created, create the schema for it
        if (dbIsNew) {
            createTables();
            createIndexes();
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

        CSLogger.sharedLogger().fine("Opening connection to DB named: \"" + dbName + "\"...");

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

            CSLogger.sharedLogger().fine("Connection to DB (\"" + dbName + "\") has been established.");
        } 
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // Closes the connection to the database
    public void closeConnection() {

        CSLogger.sharedLogger().fine("Closing connection to DB named: \"" + dbName + "\"...");

        try {
            connection.close();
            connectionIsOpen = false;

            CSLogger.sharedLogger().fine("Connection to DB (\"" + dbName + "\") has been closed.");
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

    // Create the database's tables
    private void createTables() {

        CSLogger.sharedLogger().fine("Creating schema for DB (\"" + dbName + "\")");

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

            CSLogger.sharedLogger().fine("Finished creating schema for DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void createIndexes() {

        try {
            PreparedStatement tilesIdIndex = connection.prepareStatement("CREATE INDEX tiles_id_idx ON tiles(id)");
            tilesIdIndex.execute();
            tilesIdIndex.close();

            PreparedStatement tilesXIndex = connection.prepareStatement("CREATE INDEX tiles_x_idx ON tiles(x)");
            tilesXIndex.execute();
            tilesXIndex.close();

            PreparedStatement tilesYIndex = connection.prepareStatement("CREATE INDEX tiles_y_idx ON tiles(y)");
            tilesYIndex.execute();
            tilesYIndex.close();

            PreparedStatement zonesIdIndex = connection.prepareStatement("CREATE INDEX zones_id_idx ON zones(id)");
            zonesIdIndex.execute();
            zonesIdIndex.close();

            PreparedStatement zonesXIndex = connection.prepareStatement("CREATE INDEX zones_x_idx ON zones(x)");
            zonesXIndex.execute();
            zonesXIndex.close();

            PreparedStatement zonesYIndex = connection.prepareStatement("CREATE INDEX zones_y_idx ON zones(y)");
            zonesYIndex.execute();
            zonesYIndex.close();

            PreparedStatement zonesZoneIndex = connection.prepareStatement("CREATE INDEX zones_zone_idx ON zones(zone)");
            zonesZoneIndex.execute();
            zonesZoneIndex.close();
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

        CSLogger.sharedLogger().fine("Retrieving map size from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from map_size", new MapListHandler());
            HashMap mapSize = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().fine("Finished retrieving map size from DB (\"" + dbName + "\"). Got map size of " + mapSize.get(Data.MAPSIZE_ROWS) + "x" + mapSize.get(Data.MAPSIZE_COLUMNS));
            return mapSize;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapSize(HashMap mapSize) {

        CSLogger.sharedLogger().fine("Inserting map size into DB (\"" + dbName + "\") of " + mapSize.get(Data.MAPSIZE_ROWS) + " x " + mapSize.get(Data.MAPSIZE_COLUMNS));
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

            CSLogger.sharedLogger().fine("Finished inserting map size into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // 

    public HashMap mapMetadata() {

        CSLogger.sharedLogger().fine("Retrieving map metadata from DB (\"" + dbName + "\")");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List) runner.query(connection, "SELECT * from map_metadata", new MapListHandler());

            HashMap mapMetadata = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().fine("Finished retrieving map metadata from DB (\"" + dbName + "\")"); 

            return mapMetadata;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().fine("Inserting map metadata into DB (\"" + dbName + "\")");

        try {

            String statementString = "INSERT INTO " + Data.METADATA + " VALUES (";
            for (int i = 0; i < Data.TABLES_MAPPING.get(Data.METADATA).length; i++) {
                statementString += "?" + (i < Data.TABLES_MAPPING.get(Data.METADATA).length-1 ? ", " : "");
            }
            statementString += ");";
            // System.out.println(statementString);
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.METADATA)) {
                statement.setObject(i, mapMetadata.get(param));
                i++;
            }

            statement.addBatch();
            statement.executeBatch();
            statement.close();

            CSLogger.sharedLogger().fine("Finished inserting map metadata into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateMapMetadata(HashMap mapMetadata) {

        CSLogger.sharedLogger().fine("Updating map metadata in DB (\"" + dbName + "\")");

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

            CSLogger.sharedLogger().fine("Finished updating map metadata in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONES -

    public Zone[] zones() {

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones", new MapListHandler());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        Zone[] zones = new Zone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new Zone((HashMap)results.get(i));
        }

        return zones;
    }

    public ResidentialZone[] residentialZones() {

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE zone = 1", new MapListHandler());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        ResidentialZone[] zones = new ResidentialZone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new ResidentialZone((HashMap)results.get(i));
        }

        return zones;
    }

    public CommercialZone[] commercialZones() {

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE zone = 2", new MapListHandler());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        CommercialZone[] zones = new CommercialZone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new CommercialZone((HashMap)results.get(i));
        }

        return zones;
    }

    public IndustrialZone[] industrialZones() {

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE zone = 3", new MapListHandler());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        IndustrialZone[] zones = new IndustrialZone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new IndustrialZone((HashMap)results.get(i));
        }

        return zones;
    }

    public PowerGridZone[] powerPlants() {

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE zone = 4 OR zone = 5", new MapListHandler());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        PowerGridZone[] zones = new PowerGridZone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new PowerGridZone((HashMap)results.get(i));
        }

        return zones;
    }

    public Zone[] zonesMatchingCriteria(String criteria) {

        CSLogger.sharedLogger().fine("Running query for zones with criteria (" + criteria + ")");

        List results = null;

        try {
            QueryRunner runner = new QueryRunner();
            results = (List)runner.query(connection, "SELECT * FROM zones WHERE " + criteria, new MapListHandler());

            CSLogger.sharedLogger().fine("Completed query; got " +  results.size() + " zones matching criteria");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        Zone[] zones = new Zone[results.size()];

        for (int i = 0; i < zones.length; i++) {
            zones[i] = new Zone((HashMap)results.get(i));
        }

        return zones;
    }

    public void insertZone(Zone zone) {

        CSLogger.sharedLogger().fine("Inserting zone into DB (\"" + dbName + "\")");

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

    public void updateZone(Zone zone) {

        CSLogger.sharedLogger().fine("Updating zone in DB (\"" + dbName + "\")");

        try {

            String statementString = "UPDATE " + Data.ZONES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.ZONES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.ZONES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.ZONES).length-1 ? ", " : " "));
            }
            statementString += "WHERE id = ?;";

            PreparedStatement statement = connection.prepareStatement(statementString);
            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.ZONES)) {
                if (param != Data.TILES_ID) {
                    //                     System.out.println(param + ": " + zone.get(param));
                    //                     statement.setInt(i, ((Integer)(zone.get(param))).intValue());
                    statement.setObject(i, zone.get(param));
                    i++;
                }
            }
            statement.setObject(i, new Integer(zone.dbID()));

            statement.addBatch();
            statement.executeBatch();
            statement.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateZones(Zone[] zones) {

        try {

            //             Connection dbConn = DriverManager.getConnection("jdbc:sqlite:" + mapsDirectory + "/" + dbName + ".db");
            connection.setAutoCommit(false);

            String statementString = "UPDATE " + Data.ZONES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.ZONES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.ZONES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.ZONES).length-1 ? ", " : " "));
            }
            statementString += "WHERE id = ?;";

            // System.out.println(statementString);

            PreparedStatement statement = connection.prepareStatement(statementString);

            for (Zone zone : zones) {
                int i = 1;
                for (String param : Data.TABLES_MAPPING.get(Data.ZONES)) {
                    if (param != Data.TILES_ID) {
                        //                     System.out.println(param + ": " + zone.get(param));
                        //                     statement.setInt(i, ((Integer)(zone.get(param))).intValue());
                        statement.setObject(i, zone.get(param));
                        i++;
                    }
                }
                statement.setObject(i, new Integer(zone.dbID()));

                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            //             dbConn.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void increaseZoneAge(int age) {

        CSLogger.sharedLogger().fine("Increasing zone ages (by " + age + ") in DB (\"" + dbName + "\")");

        try {
            new QueryRunner().update(connection, "UPDATE zones SET age = age + " + age);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteZoneWithID(int id) {

        CSLogger.sharedLogger().fine("Deleting zone (" + id + ") from DB (\"" + dbName + "\")");

        try {
            new QueryRunner().update(connection, "DELETE FROM zones WHERE id = " + id);
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONE, TILE - 

    public int[] tilesInZoneWithID(int id) {

        CSLogger.sharedLogger().fine("Retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_tile WHERE zone_id = " + id, new MapListHandler());
            int[] tiles = new int[results.size()];
            for (int i = 0; i < results.size(); i++) {
                Map row = (Map)results.get(i);
                tiles[i] = ((Integer)row.get("tile_id")).intValue();
            }
            CSLogger.sharedLogger().fine("Finished retrieving tiles in zone with ID (" + id + ") from DB (\"" + dbName + "\").");
            return tiles;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public Zone zoneWithTile(Tile tile) {

        try {
            List results = (List)(new QueryRunner().query(connection, "SELECT zone_id FROM zone_tile WHERE tile_id = " + tile.dbID(), new MapListHandler()));
            if (results.size() > 0) {
                Map row = (Map)results.get(0);
                return zonesMatchingCriteria("id = " + ((Integer)row.get("zone_id")).intValue())[0];
            }
            else {
                return null;
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneTiles(HashMap[] zoneTiles) {

        CSLogger.sharedLogger().fine("Inserting zone into DB (\"" + dbName + "\")");

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

            CSLogger.sharedLogger().fine("Finished inserting zone into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void deleteZoneTileWithID(int id) {

        CSLogger.sharedLogger().fine("Deleting zone_tile with ID (" + id + ") from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            runner.update(connection, "DELETE FROM zone_tile WHERE zone_id = " + id);

            CSLogger.sharedLogger().fine("Finished deleting zone from DB (\"" + dbName + "\").");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ZONE STATS -

    public HashMap zoneStats() {
        CSLogger.sharedLogger().fine("Retrieving zone stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM zone_stats", new MapListHandler());
            HashMap zone = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().fine("Finished retrieving zone stats from DB (\"" + dbName + "\").");
            return zone;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertZoneStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Inserting zone stats into DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished inserting zone stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateZoneStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Updating zone stats in DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished updating zone stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - ROAD STATS -

    public HashMap roadStats() {
        CSLogger.sharedLogger().fine("Retrieving road stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * FROM road_stats", new MapListHandler());
            HashMap road = (HashMap)results.listIterator().next();
            CSLogger.sharedLogger().fine("Finished retrieving road stats from DB (\"" + dbName + "\").");
            return road;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertRoadStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Inserting road stats into DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished inserting road stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateRoadStats(HashMap stats) {
        CSLogger.sharedLogger().fine("Updating road stats in DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished updating road stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // - TILES -

    // Iterates through a given ArrayList that contains the game map and inserts it into the database
    // Should only be used after the inital map generation
    public void insertTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().fine("Inserting map tiles into DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished inserting map tiles into DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace(); 
        }
    }

    public void updateTile(Tile tile) {

        CSLogger.sharedLogger().fine("Updating tile in DB (\"" + dbName + "\")...");

        try {

            String statementString = "UPDATE " + Data.TILES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.TILES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.TILES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.TILES).length-1 ? ", " : " "));
            }
            statementString += "WHERE " + Data.TILES_ID +" = ?;";
            PreparedStatement statement = connection.prepareStatement(statementString);

            int i = 1;
            for (String param : Data.TABLES_MAPPING.get(Data.TILES)) {
                if (param != Data.TILES_ID) {
                    //                     System.out.println("param: " + param + " |  value: " + tile.get(param));
                    statement.setObject(i, tile.get(param));
                    i++;
                }
            }
            statement.setObject(i, tile.get(Data.TILES_ID));
            statement.executeUpdate();
            statement.close();

            CSLogger.sharedLogger().fine("Finished updating tile in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        CSLogger.sharedLogger().fine("Updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");

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
                        if (param != Data.TILES_ID) {
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

            CSLogger.sharedLogger().fine("Finished updating " + tiles.size() * tiles.get(0).size() + " tiles, in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateTiles(Tile[] tiles) {

        CSLogger.sharedLogger().fine("Updating " + tiles.length + " tiles, in DB (\"" + dbName + "\")...");

        try {

            connection.setAutoCommit(false);

            String statementString = "UPDATE " + Data.TILES + " SET ";
            for (int i = 1; i < Data.TABLES_MAPPING.get(Data.TILES).length; i++) {
                statementString += (Data.TABLES_MAPPING.get(Data.TILES)[i] + " = ?" + (i < Data.TABLES_MAPPING.get(Data.TILES).length-1 ? ", " : " "));
            }
            statementString += "WHERE " + Data.TILES_ID +" = ?;";
            PreparedStatement statement = connection.prepareStatement(statementString);

            //             for (int x = 0; x < tiles.size(); x++) {
            //                 for (int y = 0; y < tiles.get(x).size(); y++) {
            for (Tile tile : tiles) {
                //                     Tile tile = (Tile)tiles.get(x).get(y);
                int i = 1;
                for (String param : Data.TABLES_MAPPING.get(Data.TILES)) {
                    if (param != Data.TILES_ID) {
                        //                             System.out.println("param: " + param + " |  value: " + tile.get(param));
                        statement.setObject(i, tile.get(param));
                        i++;
                    }
                }
                statement.setObject(i, tile.get(Data.TILES_ID));
                statement.addBatch();
                //                 }
            }

            statement.executeBatch();
            connection.commit();
            statement.close();

            connection.setAutoCommit(true);

            CSLogger.sharedLogger().fine("Finished updating " + tiles.length + " tiles, in DB (\"" + dbName + "\")...");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Tile>> tiles() {

        CSLogger.sharedLogger().fine("Retrieving map tiles from DB (\"" + dbName + "\")...");

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

            CSLogger.sharedLogger().fine("Finished retrieving map tiles from DB (\"" + dbName + "\")");

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

    public Tile[] tilesMatchingCriteria(String criteria) {

        try {

            FutureTask<Tile[]> task = new FutureTask<Tile[]>(new TilesMatchingCriteriaQueryCallable(connection, criteria));
            executorService.submit(task);

            try {
                return task.get();
            }
            catch (ExecutionException ee) {
                ee.printStackTrace();    
            }

        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        return null;
    }

    // - CITY -

    public HashMap cityStats() {

        CSLogger.sharedLogger().fine("Retrieving city stats from DB (\"" + dbName + "\")...");

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT * from city_stats", new MapListHandler());

            HashMap cityStats = (HashMap)results.listIterator().next();

            CSLogger.sharedLogger().fine("Finished retrieving city stats from DB (\"" + dbName + "\")");

            return cityStats;
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    public void insertCityStats(HashMap stats) {

        CSLogger.sharedLogger().fine("Inserting city stats into DB (\"" + dbName + "\")");

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

            CSLogger.sharedLogger().fine("Finished inserting city stats into DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateCityStats(HashMap stats) {

        CSLogger.sharedLogger().fine("Updating city stats in DB (\"" + dbName + "\")");

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

            CSLogger.sharedLogger().fine("Finished updating city stats in DB (\"" + dbName + "\")");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    // POWER GRID

    public void killPower() {

        CSLogger.sharedLogger().fine("Killing power in the city");

        try {
            new QueryRunner().update(connection, "UPDATE tiles SET powered = -1 WHERE NOT (zone = 4 OR zone = 5)");
            new QueryRunner().update(connection, "UPDATE zones SET powered = -1 WHERE NOT (zone = 4 OR zone = 5)");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void powerZone(Zone zone) {
        CSLogger.sharedLogger().fine("Powering zone (" + zone.dbID() + ")");

        try {
            new QueryRunner().update(connection, "UPDATE tiles SET powered = " + zone.poweredBy() + " WHERE zone_id = " + zone.dbID());    
            new QueryRunner().update(connection, "UPDATE zones SET powered = " + zone.poweredBy() + " WHERE id = " + zone.dbID());
            new QueryRunner().update(connection, "UPDATE zones SET allocation = (allocation + 1) WHERE id = " + zone.poweredBy());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int allocationForPowerPlant(Zone zone) {
        try {
            
            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT powered FROM zones WHERE powered = " + zone.dbID(), new MapListHandler());
            return results.size();

            //             CSLogger.sharedLogger().fine("Completed query; got " +  results.size() + " zones matching criteria");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return 0;
    }

    // SIMULATION

    public void resetJobAllocations() {

        CSLogger.sharedLogger().fine("Reseting job allocations...");

        try {
            new QueryRunner().update(connection, "UPDATE zones SET allocation = 0 WHERE zone = 2 OR zone = 3");
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int allocationForZone(Zone zone) {

        try {

            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT allocation FROM zones WHERE id = " + zone.dbID(), new MapListHandler());

            HashMap z = (HashMap)results.listIterator().next();

            return ((Integer)(z.get(Data.ZONES_ALLOCATION))).intValue();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return 0;
    }

    public int totalIndustrialCapacity() {

        try {
            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT SUM(capacity) AS capacity_sum FROM zones WHERE zone = " + IndustrialZone.TYPE_ID, new MapListHandler());
            HashMap map = (HashMap)results.listIterator().next();

            if (map.get("capacity_sum") == null) {
                return 0;
            }

            return ((Integer)map.get("capacity_sum")).intValue();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return 0;
    }

    public int totalCommercialCapacity() {

        try {
            QueryRunner runner = new QueryRunner();
            List results = (List)runner.query(connection, "SELECT SUM(capacity) AS capacity_sum FROM zones WHERE zone = " + CommercialZone.TYPE_ID, new MapListHandler());
            HashMap map = (HashMap)results.listIterator().next();

            if (map.get("capacity_sum") == null) {
                return 0;
            }

            return ((Integer)map.get("capacity_sum")).intValue();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }

        return 0;
    }

    public void increaseJobAllocationForZone(int value, Zone zone) {

        try {
            new QueryRunner().update(connection, "UPDATE zones SET allocation = allocation + " + value + " WHERE id = " + zone.dbID());
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateJobAllocationForZone(int value, Zone zone) {

        try {
            new QueryRunner().update(connection, "UPDATE zones SET allocation = " + value + " WHERE id = " + zone.dbID()); 
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
