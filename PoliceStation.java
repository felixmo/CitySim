/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Write a description of class PoliceStation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PoliceStation extends ProtectionZone
{
    public static final int TYPE_ID = 7;
    public static final int[] MARKERS = { 810, 811, 812, 813, 814, 815, 816, 817, 818 };
    public static final int SIZE_WIDTH = 3;
    public static final int SIZE_HEIGHT = 3;
    public static final String NAME = "Police station";
    public static final int PRICE = 500;

    public PoliceStation(HashMap properties) {
        super(properties);
    }

    public static void build(ArrayList<ArrayList<Tile>> selectedTiles) {

        Cash.subtract(PRICE);

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(PoliceStation.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Building police station on " + (width*height) + " tiles...");

        Zone.updateTiles(selectedTiles);
    }
}
