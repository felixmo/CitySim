/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

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

    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

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

        CSLogger.sharedLogger().info("Did update types for " + (width*height) + " industrial tiles");

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
