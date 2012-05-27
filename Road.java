/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.ArrayList;

/**
 * Write a description of class Road here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Road  
{
    private static int pendingOp = 0;
    private static int activeType = 0;
    public static final String NAME = "Roads";

    protected static void updateTile(Tile selectedTile) {

        CSLogger.sharedLogger().info("Building road on tile (" + selectedTile.position().x + ", " + selectedTile.position().y + ") of type " + pendingOp);

        Data.updateTile(selectedTile);
    }

    protected static void updateTiles(ArrayList<ArrayList<Tile>> selectedTiles) {

        int width = selectedTiles.size();
        int height = ((ArrayList)selectedTiles.get(0)).size();

        int count = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                ((Tile)selectedTiles.get(i).get(j)).setRoad(pendingOp);

                count++;
            }
        }

        Data.updateTiles(selectedTiles);
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
