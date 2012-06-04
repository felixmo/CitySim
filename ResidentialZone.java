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

/**
 * Write a description of class ResidentialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZone extends Zone
{
    public static final int TYPE_ID = 1;
    public static final int[] MARKERS = { 400, 401, 402, 403, 404, 405, 406, 407, 408 };
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Residential";
    public static final int[] STAGE_MAXCAPACITY = { 4, 8, 16, 32, 64, 128, 256, 512, 1024 };
    public static final int PRICE = 100;

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

        // Check for police protection
        score += this.policeProtection() > 0 ? 15 : -5;

        // Check for fire protection
        score += this.fireProtection() > 0 ? 15 : -5;

        // Check for nearby recreational areas
        if (Data.tilesInRadiusOfZoneMatchingCriteria(this, 10, "recreation_type > 0").length > 0) {
            score += 20;
        }

        // Assign jobs
        Zone[] iZones = Data.zonesInAreaOfZone(this, 20, IndustrialZone.TYPE_ID);
        Zone[] cZones = Data.zonesInAreaOfZone(this, 20, CommercialZone.TYPE_ID);

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

            int pollutionDeathsModifier = (pollution > 40 ? 2 : (pollution >= 21 ? 3 : (pollution >= 1 ? 4 : 0)));          // Determines the modifer based on pollution levels
            int pollutionDeaths = 0;
            if (pollutionDeathsModifier > 0) {
                pollutionDeaths = Greenfoot.getRandomNumber(Math.max(1, (int)(this.allocation()/pollutionDeathsModifier)))+1;              // Determine the number of pollution based deaths
            }

            if (pollutionDeaths > 0) score -= 75;

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
            this.setAllocation(this.allocation() - deaths);
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
            // Combine arrays and shuffle so jobs can be distributed
            ArrayList<Zone> workplaces = new ArrayList<Zone>(Arrays.asList(iZones));
            workplaces.addAll(Arrays.asList(cZones));
            Collections.shuffle(workplaces);

            // Reset I & C zone allocations (should be done at beginning)

            int avaliableWorkers = this.allocation();

            for (Zone workplace : workplaces) {
                if (workplace.allocation() < workplace.capacity()) {
                    int hires = Math.max(1, (int)this.allocation()/workplaces.size());
                    DataSource.getInstance().increaseJobAllocationForZone(hires, workplace);
                    avaliableWorkers -= hires;
                }
            }

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
            if ((((score / this.score()) * 100)-100) >= 50 && this.stage() < ResidentialZone.STAGE_MAXCAPACITY.length) {
                this.setStage(this.stage()+1);
                this.setCapacity(Math.max((Greenfoot.getRandomNumber(ResidentialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)])+1), (int)(ResidentialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]/2)) + ResidentialZone.STAGE_MAXCAPACITY[Math.max(0, this.stage()-1)]);
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
                tile.setType(ResidentialZone.MARKERS[k]);
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
