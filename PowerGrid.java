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

    public static void evaluate() {

        CSLogger.sharedLogger().info("Evaluating the power grid...");

        // Kill power in the city in all areas except for power plants
        DataSource.getInstance().killPower();

        for (Zone zone : Data.zones()) {
            searched.clear();
            for (Tile tile : Data.tilesAroundZone(zone)) {
                if (tile.powerGridType() == PowerLine.TYPE_ID || tile.powerGridType() == PowerNode.TYPE_ID) {
                    search(tile);
                }
            }
        }
    }

    private static void search(Tile tile) {

        searched.add(new Integer(tile.dbID()));

        // Locate a power source
        Tile[] powerSource = Data.tilesMatchingCriteriaAroundTile(tile, "powered = 1");
        if (powerSource.length > 0) {

            tile.setPowered(1);
            Data.updateTileWithoutDraw(tile);

            for (Zone z : Data.zonesInArea(tile.position(), 1)) {
                if (Data.tilesAroundZoneWithCriteria(z, "powered = 1").length > 0) {
                    z.setPowered(1);
                }
            }

            for (Tile t : Data.tilesMatchingCriteriaAroundTile(tile, "(powergrid_type = " + PowerLine.TYPE_ID + " OR powergrid_type = " + PowerNode.TYPE_ID + ")")) {
                if (!searched.contains(t.dbID())) search(t);
            }
        }
    }

    protected static void updateTile(Tile tile) {

        Data.updateTile(tile);

//         new PowerGridEvaluationThread().start();
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

}
