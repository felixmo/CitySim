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
import java.util.Arrays;
import java.util.Collections;
import java.awt.Point;

/**
 * Write a description of class ResidentialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZone extends Zone
{
    public static final int TYPE_ID = 1;
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Residential";
    public static final int PRICE = 100;

    // STAGES (10 stages, incl. initial stage)
    public static int[][] stages = new int[10][];
    public static final int[] STAGE_INITIALTILE = { 241, 241, 241, 241, 241, 241, 262, 271, 388, 397 };
    public static final int[] STAGE_MAXCAPACITY = { 0, 4, 8, 32, 48, 64, 512, 1024, 4096, 5120 };

    static {

        for (int stage = 0; stage < STAGE_INITIALTILE.length; stage++) {

            // Initialize stage
            stages[stage] = new int[SIZE_WIDTH * SIZE_HEIGHT];

            int tile = STAGE_INITIALTILE[stage];

            for (int e = 0; e < SIZE_WIDTH * SIZE_HEIGHT; e++) {
                stages[stage][e] = tile;
                tile++;
            }

            if (stage < 6) {
                int y = 250;
                for (int x = 0; x < stage; x++) {
                    stages[stage][x] = y;
                    y++;
                }
            }
        }
    }

    public ResidentialZone(HashMap properties) {
        super(properties);
    }

    public void simulate() {
        // Set zone score to 0 and do not simulate if no roads connect to zone
        if (Data.tilesAroundZoneWithCriteria(this, "road > 0").length == 0) {
            this.setScore(0);
            return;
        }

        // Start with a base score of 0
        int score = 0;

        Zone[] police = Data.sortedZonesInAreaOfZone(this, 20, PoliceStation.TYPE_ID);
        // Check for police protection
        if (police.length > 0) {
            score += 15;
        }
        else {
            score -= 5;
        }

        // Check for fire protection
        if (Data.zonesInAreaOfZone(this, 20, FireStation.TYPE_ID).length > 0) {
            score += 15;
        }
        else {
            score -= 5;
        }

        // Check for nearby recreational areas
        if (Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "recreation > 0").length > 0) {
            score += 20;
        }

        Zone[] iZones = Data.zonesInAreaOfZone(this, 15, IndustrialZone.TYPE_ID);
        Zone[] cZones = Data.zonesInAreaOfZone(this, 15, CommercialZone.TYPE_ID);

        // Food
        if (cZones.length > 0) {
            this.setFood(1);
        }

        int deaths = 0;

        // Calculate pollution levels based on surroundings
        // 0     = NONE
        // 1-20  = LOW
        // 21-40 = MEDIUM
        // 41+   = HIGH
        int pollution = 0;  
        pollution += Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "road > 0").length * 2;                         // Roads (+2 / road)
        pollution += Data.zonesInAreaOfZone(this, 15, IndustrialZone.TYPE_ID).length * 10;                                                                                // Industrial zones (+10 / zone)
        pollution += Data.zonesInAreaOfZone(this, 15, CoalPowerPlant.TYPE_ID, NuclearPowerPlant.TYPE_ID).length * 20;   // Power plants (+20 / zone)
        this.setPollution(pollution);

        score -= (int)pollution/3;

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
            int crime = (int)(((this.allocation() / this.capacity()) * 100) / (dP == 0 ? 2 : (20 - dP)));
            setCrime(crime);

            if (crime < 6) {
                score += 25;
            }
            else if (crime >= 6 & crime <= 12) {
                score -= 35;
            }
            else {
                score -= 50;
            }

            // Crime-related deaths
            int crimeDeathsModifier = (crime > 12 ? 3 : (crime >= 6 ? 5 : (crime < 6 ? 7 : 0)));
            int crimeDeaths = 0;
            if (crimeDeathsModifier > 0) {
                crimeDeaths = Greenfoot.getRandomNumber(Math.max(1, (int)(this.allocation()/crimeDeathsModifier)))+1;
            }

            if (crimeDeaths > 0) score -= 25;

            deaths += crimeDeaths;

            // Pollution deaths
            int pollutionDeathsModifier = (pollution > 40 ? 2 : (pollution >= 21 ? 3 : (pollution >= 1 ? 4 : 0)));          // Determines the modifer based on pollution levels
            int pollutionDeaths = 0;
            if (pollutionDeathsModifier > 0) {
                pollutionDeaths = Greenfoot.getRandomNumber(Math.max(1, (int)(this.allocation()/pollutionDeathsModifier)))+1;              // Determine the number of pollution based deaths
            }

            if (pollutionDeaths > 0) score -= 25;

            deaths += pollutionDeaths;

            // Natural deaths
            int naturalDeaths = 0;
            if (this.allocation() - deaths > 0) {
                naturalDeaths = Greenfoot.getRandomNumber(Math.max(1, (int)(this.allocation()-deaths)/4))+1;
            }

            deaths += naturalDeaths;

            // Poverty deaths
            int povertyDeaths = 0;
            if (this.food() == 0 && this.allocation() - deaths > 0) {
                povertyDeaths = Greenfoot.getRandomNumber(Math.max(1, (int)(this.allocation()-deaths/2)))+1;
                score -= 50;
            }

            deaths += povertyDeaths;

            this.setAllocation(this.allocation() - Math.min(deaths, this.allocation()-1));
        }

        // Births

        int births = 0;
        if (this.allocation() != this.capacity() && this.powered() == 1 && this.food() == 1) {

            if (this.allocation() == 0) {

                births = Greenfoot.getRandomNumber((int)(this.capacity()/2))+1;
            }
            else {

                births = (int)((this.capacity() - (this.allocation() - deaths))/3);
            }
        }

        if (births > 0) score += 50;

        this.setAllocation(this.allocation() + births);

        if (this.allocation() > 0) {

            int avaliableWorkers = Employment.employResidents(this);

            if (avaliableWorkers < this.allocation()) {
                score += 50;
            }
            else {
                score -= 50;
            }
        }

        if (this.powered() == 1) {
            score += 10;
        }
        else {
            score -= 50;
        }

        if (this.score() > 0) {
            if (((((score / this.score()) * 100)-100) >= 50 && this.stage() < ResidentialZone.STAGE_MAXCAPACITY.length) || this.stage() == 0) {
                this.setCapacity(Math.max((Greenfoot.getRandomNumber(ResidentialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)])+1), (int)(ResidentialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)]/2)) + ResidentialZone.STAGE_MAXCAPACITY[Math.max(1, this.stage()-1)]);
                this.incrementStage();
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
                tile.setType(ResidentialZone.stages[0][k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Zoning " + (width*height) + " tiles for residential use...");

        updateTiles(selectedTiles);
    }

    public static int count() {
        return ((Integer)Data.zoneStats().get(Data.ZONESTATS_RESIDENTIALCOUNT)).intValue();
    }

    public static void addToCount(int more) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_RESIDENTIALCOUNT, new Integer(count()+more));
        Data.updateZoneStats(zoneStats);
    }

    public static void subtractFromCount(int less) {
        HashMap zoneStats = Data.zoneStats();
        zoneStats.put(Data.ZONESTATS_RESIDENTIALCOUNT, new Integer(count()-less));
        Data.updateZoneStats(zoneStats);
    }
}
