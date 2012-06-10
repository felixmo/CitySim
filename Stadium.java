/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Write a description of class Stadium here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stadium extends RecreationZone
{
    public static final int TYPE_ID = 8;
    public static final int[] TILES = { 780, 781, 782, 783, 784, 785, 786, 787, 788, 789, 790, 791, 792, 793, 794, 795 };
    public static final int SIZE_WIDTH = 4;
    public static final int SIZE_HEIGHT = 4;
    public static final String NAME = "Stadium";
    public static final int PRICE = 5000;

    /**
     * Constructor for objects of class Stadium
     */
    public Stadium(HashMap properties)
    {
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
                tile.setType(Stadium.TILES[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Building stadium on " + (width*height) + " tiles...");

        Zone.updateTiles(selectedTiles);
    }
}
