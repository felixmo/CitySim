/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.util.ArrayList;
import java.awt.Dimension;
import java.util.HashMap;

/**
 * Write a description of class IndustrialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZone extends Zone
{
    public static final int TYPE_ID = 3;
    public static final int[] MARKERS = { 500, 501, 502, 503, 504, 505, 506, 507, 508 };
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Industrial";
    public static final int[] STAGE_MAXCAPACITY = { 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
    public static final int PRICE = 100;

    public IndustrialZone(HashMap properties) {
        super(properties);
    }

    public void simulate() {
        // Set zone score to 0 and do not simulate if no roads connect to zone
        if (Data.tilesAroundZoneWithCriteria(this, "road > 0").length == 0) {
            CSLogger.sharedLogger().info("Zone (" + this.dbID() + ") is not connected to any roads!");
            this.setScore(0);
            return;
        }

        int score = 0;

        // Check for police protection
        score += this.policeProtection() > 0 ? 15 : -5;

        // Check for fire protection
        score += this.fireProtection() > 0 ? 15 : -5;

        // Accessibility to commercial zones
        score += Data.zonesInAreaOfZone(this, 20, CommercialZone.TYPE_ID).length * 20;

        if (this.score() > 0) {
            if ((((score / this.score()) * 100)-100) >= 25 && this.stage() < IndustrialZone.STAGE_MAXCAPACITY.length) {
                this.setStage(this.stage()+1);
                this.setCapacity(Math.max((Greenfoot.getRandomNumber(IndustrialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)])+1), (int)(IndustrialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]/2)) + IndustrialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]);
            }
        }

        //         System.out.println(score);
        this.setScore(Math.max(1, score)); 

        new ZoneDBUpdateThread(this).start();
    }

    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        Cash.subtract(PRICE);

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();
        addToCount(width*height);

        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(IndustrialZone.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Zoning " + (width*height) + " tiles for industrial use...");

        updateTiles(selectedTiles);
    }

    public static int count() {
        return ((Integer)Data.zoneStats().get(Data.ZONESTATS_INDUSTRIALCOUNT)).intValue();
    }

    public static void addToCount(int more) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_INDUSTRIALCOUNT, new Integer(count()+more));
        Data.updateZoneStats(zoneStats);
    }

    public static void subtractFromCount(int less) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_INDUSTRIALCOUNT, new Integer(count()-less));
        Data.updateZoneStats(zoneStats);
    }
}
