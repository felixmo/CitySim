/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.HashMap;

/**
 * Write a description of class PowerNode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerNode extends PowerGrid
{
    public static final int TYPE_ID = 2;
    public static final int[] MARKERS = { 746, 747, 748, 749, 750 };
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Power node";
    public static final int PRICE = 5;

    public static void buildPowerNode(Tile tile, int type) {

        Cash.subtract(PRICE);
        
        tile.setType(type);
        tile.setPowerGridType(TYPE_ID);

        PowerGrid.updateTile(tile);
    }
}