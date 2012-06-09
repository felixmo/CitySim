/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;
import java.lang.Integer;
import java.util.HashMap;
import java.awt.Point;

/**
 * Write a description of class Zoning here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zone  
{   
    protected static int pendingOp = 0; // ID of zone type
    public static final String NAME = "Zoning";

    private HashMap properties;

    //

    public Zone(HashMap properties) {
        this.properties = properties;
    }

    // - 

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int capacity = 0;

        if (pendingOp == ResidentialZone.TYPE_ID) {
            capacity = ResidentialZone.STAGE_MAXCAPACITY[0];
        }
        else if (pendingOp == CommercialZone.TYPE_ID) {
            capacity = CommercialZone.STAGE_MAXCAPACITY[0];
        }
        else if (pendingOp == IndustrialZone.TYPE_ID) {
            capacity = IndustrialZone.STAGE_MAXCAPACITY[0];
        }

        int zoneID = Data.idForNewZone();

        Tile origin = (Tile)selectedTiles.get(0).get(0);
        Data.insertZone(zoneID, pendingOp, origin.position().x, origin.position().y, origin.powered(), capacity);

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        //         CSLogger.sharedLogger().info("Zoning " + width*height + " tiles as type " + pendingOp);

        HashMap[] zoneTiles = new HashMap[width*height];

        int count = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                HashMap zone = new HashMap(2);
                zone.put(Data.ZONETILE_ZONEID, zoneID);
                zone.put(Data.ZONETILE_TILEID, ((Tile)selectedTiles.get(i).get(j)).dbID());

                zoneTiles[count] = zone;

                ((Tile)selectedTiles.get(i).get(j)).setZone(pendingOp);
                ((Tile)selectedTiles.get(i).get(j)).setZoneID(zoneID);

                count++;
            }
        }

        Data.insertZoneTiles(zoneTiles);
        Data.updateTiles(selectedTiles);

        PowerGrid.setShouldEvaluate(true);
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

    //

    public Object get(String key) {
        return this.properties.get(key);
    }

    public int dbID() {
        return ((Integer)properties.get(Data.ZONES_ID)).intValue();
    }

    public int zone() {
        return ((Integer)properties.get(Data.ZONES_ZONE)).intValue();
    }

    public int age() {
        return ((Integer)properties.get(Data.ZONES_AGE)).intValue();
    }

    public int powered() {
        return ((Integer)properties.get(Data.ZONES_POWERED)).intValue();
    }

    public void setPowered(int value) {
        properties.put(Data.ZONES_POWERED, new Integer(value));
        //         DataSource.getInstance().powerZone(this);
        new PowerZoneDBUpdateThread(this).start();
    }

    public Point origin() {
        return new Point((Integer)properties.get(Data.ZONES_X), (Integer)properties.get(Data.ZONES_Y));
    }

    public int score() {
        return ((Integer)properties.get(Data.ZONES_SCORE)).intValue();
    }

    public void setScore(int value) {
        properties.put(Data.ZONES_SCORE, new Integer(value));
    }

    public int pollution() {
        return ((Integer)properties.get(Data.ZONES_POLLUTION)).intValue();
    }

    public void setPollution(int value) {
        properties.put(Data.ZONES_POLLUTION, new Integer(value));
    }

    public int fireProtection() {
        return ((Integer)properties.get(Data.ZONES_FIRE_PROTECTION)).intValue();
    }

    public int policeProtection() {
        return ((Integer)properties.get(Data.ZONES_POLICE_PROTECTION)).intValue();
    }

    public int crime() {
        return ((Integer)properties.get(Data.ZONES_CRIME)).intValue();
    }

    public int food() {
        return ((Integer)properties.get(Data.ZONES_FOOD)).intValue();
    }

    public void setFood(int value) {
        properties.put(Data.ZONES_FOOD, new Integer(value));
    }

    public int jobs() {
        return ((Integer)properties.get(Data.ZONES_JOBS)).intValue();
    }

    public int allocation() {
        return ((Integer)properties.get(Data.ZONES_ALLOCATION)).intValue();
    }

    public void setAllocation(int value) {
        properties.put(Data.ZONES_ALLOCATION, new Integer(value));
    }

    public int capacity() {
        return ((Integer)properties.get(Data.ZONES_CAPACITY)).intValue();
    }

    public void setCapacity(int value) {
        properties.put(Data.ZONES_CAPACITY, new Integer(value));
    }

    public int stage() {
        return ((Integer)properties.get(Data.ZONES_STAGE)).intValue();
    }

    public void setStage(int value) {
        properties.put(Data.ZONES_STAGE, new Integer(value));
    }

    public void incrementStage() {

        if (stage() >= stagesForZoneType(zone()).length-1) return;

        int stage = stage() + 1;

        Tile[] tiles = Data.tilesInZone(this);

        // Initial x coordinate
        int xi = tiles[0].position().x;
        // First column of zone
        int a = 0;
        // Second column of zone
        int b = 1;
        // Thrid column of zone
        int c = 2;
        // Element 
        int x = 0;

        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            if (i > 0) {
                if (tile.position().x == xi) {
                    x = a;
                    a += 3;
                }
                else if (tile.position().x == xi+1) {
                    x = b;
                    b += 3;
                }
                else {
                    x = c;
                    c += 3;
                }
            }
            else {
                a += 3;
            }
            tile.setType(tilesForStageOfZoneType(stage, zone())[x]);
        }

        Data.updateTiles(tiles);

        setStage(stage);
        new ZoneDBUpdateThread(this).start();
    }

    //

    private int[][] stagesForZoneType(int type) {

        switch (type) {
            case ResidentialZone.TYPE_ID:
            return ResidentialZone.stages;
            case IndustrialZone.TYPE_ID:
            return IndustrialZone.stages;
            case CommercialZone.TYPE_ID:
            return CommercialZone.stages;
            default:
            break;
        }

        return null;
    }

    private int[] tilesForStageOfZoneType(int stage, int type) {

        switch (type) {
            case ResidentialZone.TYPE_ID:
            return ResidentialZone.stages[stage];
            case IndustrialZone.TYPE_ID:
            return IndustrialZone.stages[stage];
            case CommercialZone.TYPE_ID:
            return CommercialZone.stages[stage];
            default:
            break;
        }

        return null;
    }
}
