/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class CommercialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CommercialZone extends Zone
{
    public static final int TYPE_ID = 2;
    public static final int[] MARKERS = { 600, 601, 602, 603, 604, 605, 606, 607, 608 };
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Commercial";
    public static final int[] STAGE_MAXCAPACITY = { 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
    public static final int PRICE = 100;

    public CommercialZone(HashMap properties) {
        super(properties);
    }

    public void simulate() {
        // Set zone score to 0 and do not simulate if no roads connect to zone
        if (Data.tilesAroundZoneWithCriteria(this, "road > 0").length == 0) {
            this.setScore(0);
            return;
        }

        int score = 0;

        // Check for police protection
        score += this.policeProtection() > 0 ? 15 : -5;

        // Check for fire protection
        score += this.fireProtection() > 0 ? 15 : -5;

        // Check for nearby recreational areas
        if (Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "recreation > 0").length > 0) {
            score += 10;
        }

        // Accessiblity to consumers
        score += Data.zonesInAreaOfZone(this, 20, ResidentialZone.TYPE_ID).length * 10;

        // Accessibility to suppliers
        Zone[] iZones = Data.zonesInAreaOfZone(this, 20, IndustrialZone.TYPE_ID);
        score += iZones.length * 10;

        // Calculate pollution levels based on surroundings
        // 0     = NONE
        // 1-20  = LOW
        // 21-40 = MEDIUM
        // 41+   = HIGH
        int pollution = 1;  
        pollution += Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "road > 0").length * 2;                         // Roads (+2 / road)
        pollution += iZones.length * 10;                                                                                // Industrial zones (+10 / zone)
        pollution += Data.zonesInAreaOfZone(this, 20, CoalPowerPlant.TYPE_ID, NuclearPowerPlant.TYPE_ID).length * 20;   // Power plants (+20 / zone)
        this.setPollution(pollution);

        score -= pollution;

        if (this.score() > 0) {
            if ((((score / this.score()) * 100)-100) >= 25 && this.stage() < CommercialZone.STAGE_MAXCAPACITY.length) {
                this.setStage(this.stage()+1);
                this.setCapacity(Math.max((Greenfoot.getRandomNumber(CommercialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)])+1), (int)(CommercialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]/2)) + CommercialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]);
            }
        }

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
                tile.setType(CommercialZone.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Zoning " + (width*height) + " tiles for commercial use...");

        updateTiles(selectedTiles);
    }

    public static int count() {
        return ((Integer)Data.zoneStats().get(Data.ZONESTATS_COMMERCIALCOUNT)).intValue();
    }

    public static void addToCount(int more) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_COMMERCIALCOUNT, new Integer(count()+more));
        Data.updateZoneStats(zoneStats);
    }

    public static void subtractFromCount(int less) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_COMMERCIALCOUNT, new Integer(count()-less));
        Data.updateZoneStats(zoneStats);
    }
}
