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
import java.awt.Point;

/**
 * Write a description of class IndustrialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZone extends Zone
{
    public static final int TYPE_ID = 3;
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Industrial";
    public static final int PRICE = 100;

    // STAGES (10 stages, incl. initial stage)
    public static int[][] stages = new int[9][];
    public static final int[] STAGE_INITIALTILE = { 613, 658, 622, 631, 640, 667, 649, 676, 685 };
    public static final int[] STAGE_MAXCAPACITY = { 0, 16, 32, 64, 128, 256, 512, 1024, 1152 };

    static {

        for (int stage = 0; stage < STAGE_INITIALTILE.length; stage++) {

            // Initialize stage
            stages[stage] = new int[SIZE_WIDTH * SIZE_HEIGHT];

            int tile = STAGE_INITIALTILE[stage];

            for (int e = 0; e < SIZE_WIDTH * SIZE_HEIGHT; e++) {
                stages[stage][e] = tile;
                tile++;
            }
        }
    }

    public IndustrialZone(HashMap properties) {
        super(properties);
    }

    public void simulate() {
        // Set zone score to 0 and do not simulate if no roads connect to zone
        if (Data.tilesAroundZoneWithCriteria(this, "road > 0").length == 0) {
            CSLogger.sharedLogger().info("Zone (" + this.dbID() + ") is not connected to any roads!");
            //             this.setScore(0);
            return;
        }

        int score = 0;

        // Start with a score based upon the scores of nearby zones
        //         Zone[] area = Data.zonesInAreaOfZone(this, 5, ResidentialZone.TYPE_ID, IndustrialZone.TYPE_ID, CommercialZone.TYPE_ID);
        //         if (area.length > 0) {
        //             int sSum = 0;
        //             for (Zone z : area) {
        //                 sSum += z.score();
        //             }
        //             int avg = (int)(sSum / area.length);
        //             //             if (avg > 0) {
        //             score = (int)(avg/2);
        //             //             }
        //         }

        // Check for police protection
        Zone[] police = Data.sortedZonesInAreaOfZone(this, 15, PoliceStation.TYPE_ID);
        if (police.length > 0) {
            score += 15;
        }
        else {
            score -= 5;
        }

        // Check for fire protection
        if (Data.zonesInAreaOfZone(this, 15, FireStation.TYPE_ID).length > 0) {
            score += 15;
        }
        else {
            score -= 5;
        }

        // Accessibility to commercial zones
        score +=  Math.max(50, Data.zonesInAreaOfZone(this, 15, CommercialZone.TYPE_ID).length * 5);

        // Calculate pollution levels based on surroundings
        // 0     = NONE
        // 1-30  = LOW
        // 31-50 = MEDIUM
        // 51+   = HIGH
        // 100   = MAX
        Zone[] iZones = Data.zonesInAreaOfZone(this, 15, IndustrialZone.TYPE_ID);
        int pollution = 1;  
        pollution += Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "road > 0").length * 2;                         // Roads (+2 / road)
        pollution += iZones.length * 7;                                                                                // Industrial zones (+10 / zone)
        pollution += Data.zonesInAreaOfZone(this, 15, CoalPowerPlant.TYPE_ID).length * 25;   
        pollution += Data.zonesInAreaOfZone(this, 10, NuclearPowerPlant.TYPE_ID).length * 15;
        pollution = Math.min(100, pollution);

        this.setPollution(pollution);

        score -= (int)(pollution/2);

        if (this.allocation() > 0) {
            // Calculate crime levels
            // < 6 - LOW
            // 6 - 12 - MEDIUM
            // > 12 - HIGH
            int dP = 0;
            if (police.length > 0) {
                Point closetPolice = police[0].origin();
                Point dToPolice = new Point(Math.abs(closetPolice.x - this.origin().x), Math.abs(closetPolice.y - this.origin().y));
                dP = (dToPolice.x + dToPolice.y) / 2;
            }
            int crime = (int)(((this.allocation() / this.capacity()) * 100) / dP == 0 ? 2 : (20 - dP));
            setCrime(crime);

            if (crime < 6) {
                score += 25;
            }
            else if (crime >= 6 & crime <= 12) {
                score -= 5;
            }
            else {
                score -= 10;
            }
        }

        int density = 0;
        if (this.allocation() > 0 && this.capacity() > 0) {
            density = (int)((this.allocation() / this.capacity()) * 100);
        }

        if (this.score() > 0 && this.powered()) {
            if (((((score / this.score()) * 100)-100) >= 25 && this.stage() < IndustrialZone.STAGE_MAXCAPACITY.length) || this.stage() == 0 || (this.stage() < IndustrialZone.STAGE_MAXCAPACITY.length && (score >= 70 && density >= 80))) {
                if (!(this.allocation() == 0 && this.stage() == 1) && (this.stage() == 0 || density >= 70)) {

                    this.setCapacity(Math.max((Greenfoot.getRandomNumber(IndustrialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)])+1), (int)(IndustrialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)]/2)) + IndustrialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)]);
                    this.incrementStage();
                }
            }
        }

        //         System.out.println(score);
        this.setScore(score);   

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
                tile.setType(IndustrialZone.stages[0][k]);
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
