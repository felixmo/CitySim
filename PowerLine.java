/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class Wire here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerLine extends PowerGrid
{
    public static final int TYPE_ID = 1;
    public static final int[] MARKERS = { 740, 741, 742, 743, 744, 745 };
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Power line";
    public static final int PRICE = 5;

    public static void buildPowerLine(Tile tile, int type) {

        Cash.subtract(PRICE);
        
        tile.setType(type);
        tile.setPowerGridType(TYPE_ID);

        PowerGrid.updateTile(tile);
    }
}