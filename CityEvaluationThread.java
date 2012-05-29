/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;
import java.awt.Point;

/**
 * Write a description of class CityEvaluationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CityEvaluationThread extends CSThread
{
    public CityEvaluationThread() {
        super("");
    }

    public void run() {
/*
        CSLogger.sharedLogger().info("Began evaluating the city...");
        long startTime = System.currentTimeMillis();

        // Find nearby C & I zones for R zones

        Zone[] resZones = Data.zonesMatchingCriteria("zone = " + ResidentialZone.TYPE_ID);
        System.out.println("Evaluating " + resZones.length + " residential zones");

        for (Zone res : resZones) {

            int fifteen = 0;
            int thirty = 0;
            int fifty = 0;

            // 15 tile range
            HashMap[] fifteenRange = Data.zonesInArea(new Point(((Integer)res.get("x")).intValue(), ((Integer)res.get("y")).intValue()), 15, IndustrialZone.TYPE_ID, CommercialZone.TYPE_ID);
            fifteen = fifteenRange.length;

            // 30 tile range
            HashMap[] thirtyRange = Data.zonesInArea(new Point(((Integer)res.get("x")).intValue(), ((Integer)res.get("y")).intValue()), 30, IndustrialZone.TYPE_ID, CommercialZone.TYPE_ID);
            thirty = thirtyRange.length; 

            // 50 tile range
            HashMap[] fiftyRange = Data.zonesInArea(new Point(((Integer)res.get("x")).intValue(), ((Integer)res.get("y")).intValue()), 50, IndustrialZone.TYPE_ID, CommercialZone.TYPE_ID);
            fifty = fiftyRange.length;

            System.out.println("R @ (" + (Integer)res.get("x") + ", " + (Integer)res.get("y") + ") | 15: " + fifteen + " | 30: " + thirty + " | 50: " + fifty);
        }

        CSLogger.sharedLogger().info("Finished evaluating the city in " + (System.currentTimeMillis() - startTime) + " ms");
    */
    }
}
