/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

/**
 * Write a description of class Park here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Park extends Recreation
{
    public static final int TYPE_ID = 1;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Park";
    public static final int PRICE = 20;
    
    public static void build(Tile tile) {
        
        Cash.subtract(PRICE);
        
        tile.setRecreation(TYPE_ID);
        tile.setType(Tile.PARK);
        
        Recreation.updateTile(tile);
    }
}
