/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class Street here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Street extends Road
{
    public static final int TYPE_ID = 1;
    public static final int[] MARKERS = { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 };
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Streets";
    public static final int PRICE = 1;

    public static void buildStreet(Tile tile, int type) {

        Cash.subtract(PRICE);

        Tile up = null, down = null, left = null, right = null;

        Tile[] nearby = Data.tilesMatchingCriteriaTouchingTile(tile, "road = 1");
        for (Tile t : nearby) {
            if (t.position().x < tile.position().x) {
                // LEFT
                left = t;
            }
            else if (t.position().x > tile.position().x) {
                // RIGHT
                right = t;
            }
            else {
                if (t.position().y < tile.position().y) {
                    // UP
                    up = t;
                }
                else if (t.position().y > tile.position().y) {
                    // DOWN
                    down = t;
                }
            }
        }

        if (up != null && left != null) {

            // Straight (h)
            up.setType(101);

            // Straight (v)
            left.setType(102);

            // Bend
            tile.setType(106);
        }
        if (up != null && right != null) {

            // Straight (h)
            up.setType(101);

            // Straight (v)
            right.setType(102);

            // Bend
            tile.setType(103);
        }
        if (down != null && left != null) {

            // Straight (v)
            down.setType(102);

            // Straight (h)
            left.setType(101);

            // Bend
            tile.setType(105);
        }
        else if (down != null && right != null) {

            // Straight (v)
            down.setType(102);

            // Straight (h)
            right.setType(101);

            // Bend
            tile.setType(104);
        }
        else if (up != null || down != null) {

            if (up != null) {
                if (Data.tilesMatchingCriteria("road = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x-1)).length == 1) {
                    // Check for a road to the LEFT of the upper tile
                    up.setType(105);
                }
                else if (Data.tilesMatchingCriteria("road = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x+1)).length == 1) {
                    // Check for a road to the RIGHT of the upper tile
                    up.setType(104);
                }
                else {
                    // Straight (v)
                    up.setType(102);
                }
            }

            if (down != null) {
                if ((Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x-1) + " AND y = " + down.position().y).length == 0) && (Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 0)) {
                    // Check for:
                    // 1. a road LEFT of lower tile
                    // 2. a road BELOW the lower tile

                    // Bend
                    down.setType(103);
                }
                else if ((Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 0) && (Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x+1) + " AND y = " + (down.position().y)).length == 0)) {
                    // Check for:
                    // 1. a road BELOW the lower tile
                    // 2. a road RIGHT of the lower tile

                    // Bend
                    down.setType(106);
                }
                else if ((Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 0) && (Data.tilesMatchingCriteria("road = 1 AND x = " + (down.position().x+1) + " AND y = " + (down.position().y)).length == 0)) {
                    // Check for:
                    // 1. a road LEFT of the lower tile
                    // 2. a road RIGHT of the lower tile
                    
                    // 3-way
                    down.setType(107);
                }
                else {
                    // Straight (v)
                    down.setType(102);
                }
            }

            // Straight (v)
            tile.setType(102);
        }
        else if (left != null || right != null) {

            if (left != null) {
                if (Data.tilesMatchingCriteria("road = 1 AND x = " + (left.position().x-1) + " AND y = " + left.position().y).length == 0) {
                    // Check for a road to the left of the left tile

                    // Bend
                    left.setType(103);
                }
                else {
                    // Straight (h)
                    left.setType(101);
                }
            }

            if (right != null) {
                if (Data.tilesMatchingCriteria("road = 1 AND x = " + (right.position().x+1) + " AND y = " + right.position().y).length == 0) {
                    // Check for a road to the right of the right tile

                    // Bend
                    right.setType(106);
                }
                else if (Data.tilesMatchingCriteria("road = 1 AND y = " + (right.position().y+1) + " AND x = " + right.position().x).length == 1) {
                    // Check for a road above the right tile

                    // Bend
                    right.setType(105);
                }
                else {
                    // Straight (h)
                    right.setType(101);
                }
            }

            // Straight (h)
            tile.setType(101);
        }
        else {
            // Straight (h)
            tile.setType(101);
        }

        tile.setRoad(TYPE_ID);
        addToCount(1);

        Data.updateTile(left);
        Data.updateTile(right);
        Data.updateTile(down);
        Data.updateTile(up);

        Road.updateTile(tile);
    }

    /*
    public static void buildStreet(Tile tile, int type) {

    Cash.subtract(PRICE);

    tile.setType(type);
    tile.setRoad(TYPE_ID);
    addToCount(1);

    Road.updateTile(tile); 
    }
     */
    public static int count() {
        return ((Integer)Data.roadStats().get(Data.ROADSTATS_STREETCOUNT)).intValue();
    }

    public static void addToCount(int more) {
        HashMap roadStats = Data.roadStats();
        roadStats.put(Data.ROADSTATS_STREETCOUNT, new Integer(count()+more));
        Data.updateRoadStats(roadStats);
    }

    public static void subtractFromCount(int less) {
        HashMap roadStats = Data.roadStats();
        roadStats.put(Data.ROADSTATS_STREETCOUNT, new Integer(count()-less));
        Data.updateRoadStats(roadStats);
    }
}