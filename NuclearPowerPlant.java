/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class NuclearPowerPlant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NuclearPowerPlant extends PowerGridZone
{
    public static final int TYPE_ID = 5;
    public static final int[] MARKERS = { 812, 813, 814, 815, 816, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827 };
    public static final int SIZE_WIDTH = 4;
    public static final int SIZE_HEIGHT = 4;
    public static final String NAME = "Nuclear power plant";
    public static final int PRICE = 5000;

    public NuclearPowerPlant(HashMap properties) {
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
                tile.setType(NuclearPowerPlant.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Building nuclear power plant on " + (width*height) + " tiles...");

        PowerGridZone.updateTiles(selectedTiles);
    }
}
