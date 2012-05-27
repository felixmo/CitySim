/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class Tool here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tool  
{

    private static int pendingOp = 0; // ID of zone type
    public static final String NAME = "Tools";

    protected static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        Data.updateTiles(tiles);
    }
    
    protected static void updateTile(Tile tile) {
        
        Data.updateTile(tile);
    }

    /*
     * ACCESSORS *
     */

    public static int pendingOp() {
        return pendingOp;
    }

    public static void setPendingOp(int value) {
        pendingOp = value;
    }
}
