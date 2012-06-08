/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class CoalPowerPlant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CoalPowerPlant extends PowerGridZone
{
    public static final int TYPE_ID = 4;
    public static final int[] MARKERS = { 746, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 759, 760, 761 };
    public static final int SIZE_WIDTH = 4;
    public static final int SIZE_HEIGHT = 4;
    public static final String NAME = "Coal power plant";
    public static final int PRICE = 3000;

    public CoalPowerPlant(HashMap properties) {
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
                tile.setType(CoalPowerPlant.MARKERS[k]);
                k++;
            }
        }

        CSLogger.sharedLogger().info("Building coal power plant on " + (width*height) + " tiles...");

        PowerGridZone.updateTiles(selectedTiles);
    }
}
