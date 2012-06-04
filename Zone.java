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

        //         PowerGrid.evaluate();
        //         new PowerGridEvaluationThread().start();
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
}
