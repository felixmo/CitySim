/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class CoalPowerPlant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CoalPowerPlant extends PowerGrid
{
    public static final int TYPE_ID = 4;
    public static final int[] MARKERS = { 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735 };
    public static final int SIZE_WIDTH = 4;
    public static final int SIZE_HEIGHT = 4;
    public static final String NAME = "Coal power plant";

    public static void buildPowerPlant(ArrayList<ArrayList<Tile>> selectedTiles) {
        
        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = selectedTiles.get(j).get(i);
                tile.setType(CoalPowerPlant.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Did build power plant on " + (width*height) + " tiles.");

        updateTiles(selectedTiles);
    }
}
