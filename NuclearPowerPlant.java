/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class NuclearPowerPlant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NuclearPowerPlant extends PowerGrid
{
    public static final int TYPE_ID = 5;
    public static final int[] MARKERS = { 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715 };
    public static final int SIZE_WIDTH = 4;
    public static final int SIZE_HEIGHT = 4;
    public static final String NAME = "Nuclear power plant";

    public static void buildPowerPlant(ArrayList<ArrayList<Tile>> selectedTiles) {

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

        CSLogger.sharedLogger().info("Did build power plant on " + (width*height) + " tiles.");

        updateTiles(selectedTiles);
    }
}
