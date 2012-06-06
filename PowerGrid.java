/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class Power here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGrid
{

    private static int pendingOp = 0;
    private static int activeType = 0;
    public static final String NAME = "Power grid";
    private static ArrayList<Integer> searched = new ArrayList<Integer>();
    private static ArrayList<Integer> searchedZones = new ArrayList<Integer>();
    private static boolean shouldEvaluate = false;

    public static void evaluate() {

        CSLogger.sharedLogger().info("Evaluating the power grid...");
        long startTime = System.currentTimeMillis();

        // Kill power in the city in all areas except for power plants
        new KillPowerDBUpdateThread().start();

        // Clear search history
        searched.clear();
        searchedZones.clear();

        // Search outward from the power lines leading from the power plants
        for (Zone zone : Data.zonesMatchingCriteria("zone = " + CoalPowerPlant.TYPE_ID + " OR zone = " + NuclearPowerPlant.TYPE_ID)) {
            for (Tile tile : Data.tilesAroundZoneWithCriteria(zone, "powergrid_type = " + PowerLine.TYPE_ID)) {
                new PowerGridEvaluationTileSearchThread(tile).start();
            }
        }

        setShouldEvaluate(false);

        long endTime = System.currentTimeMillis();
        CSLogger.sharedLogger().info("Finished power grid evaluation (" + (endTime - startTime) + " ms).");
    }

    public static void searchTile(Tile tile) {

        // Add tile to search history
        searched.add(new Integer(tile.dbID()));

        // Power the tile
        tile.setPowered(1);
        Data.updateTileWithoutDraw(tile);

        // Power and search outward from surrounding zones
        for (Zone z : Data.zonesInArea(tile.position(), 1)) {
            z.setPowered(1);
            if (!searchedZones.contains(z.dbID())) {
                searchedZones.add(z.dbID());
                new PowerGridEvaluationZoneSearchThread(z).start();
            }
        }

        // Search outward from power lines & nodes
        for (Tile t : Data.tilesMatchingCriteriaAroundTile(tile, "powergrid_type = " + PowerLine.TYPE_ID)) {
            if (!searched.contains(t.dbID())) new PowerGridEvaluationTileSearchThread(t).start();
        }
    }

    public static void searchZone(Zone zone) {

        for (Zone z : Data.zonesAroundZone(zone)) {
            z.setPowered(1);

            for (Tile t : Data.tilesAroundZoneWithCriteria(zone, "powergrid_type = " + PowerLine.TYPE_ID)) {
                if (!searched.contains(t.dbID())) new PowerGridEvaluationTileSearchThread(t).start();
            }

            if (!searchedZones.contains(z.dbID())) {
                searchedZones.add(z.dbID());
                new PowerGridEvaluationZoneSearchThread(z).start();
            }
        }
    }

    protected static void updateTile(Tile tile) {

        Data.updateTile(tile);

        //         new PowerGridEvaluationThread().start();
        PowerGrid.setShouldEvaluate(true);
    }

    protected static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        Data.updateTiles(tiles);
    }

    /*
     * ACCESSORS *
     */

    public static int pendingOp() {
        return pendingOp;
    }

    public static void setPendingOp(int value) {
        pendingOp = value;
    }

    public static int activeType() {
        return activeType;
    }

    public static void setActiveType(int value) {
        activeType = value;
    }

    public static boolean shouldEvaluate() {
        return shouldEvaluate;
    }

    public static void setShouldEvaluate(boolean value) {
        shouldEvaluate = value;
    }

}
