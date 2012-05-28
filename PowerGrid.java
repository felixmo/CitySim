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
        
        // Clear the search history
        searched.clear();

        // Kill power in the city in all areas except for power plants
        DataSource.getInstance().killPower();

        // Get the power lines & nodes surrounding the power plants
        for (HashMap zone : Data.zonesMatchingCriteria("zone = " + CoalPowerPlant.TYPE_ID + " OR zone = " + NuclearPowerPlant.TYPE_ID)) {
            for (Tile tile : Data.tilesAroundPowerPlant(zone)) {
                if (tile.powerGridType() == PowerLine.TYPE_ID || tile.powerGridType() == PowerNode.TYPE_ID) {
                    search(tile);
                }
            }
        }

        // A collection of zones that have been changed and should be updated
        ArrayList<HashMap> needUpdate = new ArrayList<HashMap>();

        // Iterate through zones all check if they're now powered
        for (HashMap zone : Data.zones()) {
            
            boolean isPowered = false;
             
            // Check if zones have nearby powered tiles; if so, power the zone
            for (Tile tile : Data.tilesAroundPowerPlant(zone)) {
                if (tile.powered() > 0) {
                    isPowered = true;
                }
            }
            
            // If the zone should be powered, change attribute and flag for update
            if (isPowered) {
                zone.put(Data.ZONES_POWERED, new Integer(1));
                needUpdate.add(zone);
            }
        }

        // Send updates to the DB
        HashMap[] batchUpdate = new HashMap[needUpdate.size()];
        needUpdate.toArray(batchUpdate);
        Data.updateZones(batchUpdate);
    }

    private static void search(Tile tile) {

        CSLogger.sharedLogger().debug("Searching tile @ (" + tile.position().x + ", " + tile.position().y + ")");

        Tile[] powerSource = Data.tilesMatchingCriteriaAroundTile(tile, "powered = 1");
        if (powerSource.length > 0) {
            CSLogger.sharedLogger().debug("Tile @ (" + tile.position().x + ", " + tile.position().y + ") has power.");
            tile.setPowered(1);
            Data.updateTile(tile);
        }
        
        searched.add(new Integer(tile.dbID()));

        for (Tile t : Data.tilesMatchingCriteriaAroundTile(tile, "(powergrid_type = " + PowerLine.TYPE_ID + " OR powergrid_type = " + PowerNode.TYPE_ID + ")")) {
            if (!searched.contains(t.dbID())) search(t);
        }
    }

    protected static void updateTile(Tile tile) {

        Data.updateTile(tile);

        evaluate();
    }

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {
        // Should only be called for power plants

        int zoneID = Data.idForNewZone();

        // TODO: check if zone is powered and has water
        Tile center = (Tile)selectedTiles.get(1).get(1);
        Data.insertZone(zoneID, pendingOp, 1, center.position().x, center.position().y);

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int count = 0;

        CSLogger.sharedLogger().info("Zoning " + width*height + " tiles as type " + pendingOp);

        HashMap[] zoneTiles = new HashMap[width*height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                HashMap zone = new HashMap(2);
                zone.put(Data.ZONETILE_ZONEID, zoneID);
                zone.put(Data.ZONETILE_TILEID, ((Tile)selectedTiles.get(i).get(j)).dbID());
                zoneTiles[count] = zone;

                ((Tile)selectedTiles.get(i).get(j)).setPowered(1);
                ((Tile)selectedTiles.get(i).get(j)).setZone(pendingOp);
                ((Tile)selectedTiles.get(i).get(j)).setZoneID(zoneID);

                count++;
            }
        }

        Data.insertZoneTiles(zoneTiles);
        Data.updateTiles(selectedTiles);

        evaluate();
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
