/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class Recreation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Recreation  
{
    private static int pendingOp = 0;
    private static int activeType = 0;
    public static final String NAME = "Recreation";

    protected static void updateTile(Tile tile) {

        Data.updateTile(tile);
    }

    protected static void updateTiles(ArrayList<ArrayList<Tile>> tiles) {

        Data.updateTiles(tiles);
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

    public static int activeType() {
        return activeType;
    }

    public static void setActiveType(int value) {
        activeType = value;
    }
}
