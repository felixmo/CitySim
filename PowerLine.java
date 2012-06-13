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
//     public static final int[] MARKERS = { Tile.POWERLINE_H, Tile.POWERLINE_V, Tile.POWERLINE_B_TR, Tile.POWERLINE_B_BR, Tile.POWERLINE_B_BL, Tile.POWERLINE_B_TL };
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Power line";
    public static final int PRICE = 5;

    public static void buildPowerLine(Tile tile, int type) {

        CSLogger.sharedLogger().info("Building power line on tile (" + tile.dbID() + ")");
        
        Cash.subtract(PRICE);

        Tile up = null, down = null, left = null, right = null;

        Tile[] nearby = Data.tilesMatchingCriteriaTouchingTile(tile, "powergrid = 1");
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

            // Straight (v)
            up.setType(Tile.POWERLINE_V);

            // Straight (v)
            left.setType(Tile.POWERLINE_H);

            // Bend
            tile.setType(Tile.POWERLINE_B_TL);
        }
        else if (up != null && right != null) {

            // Straight (h)
            up.setType(Tile.POWERLINE_V);

            // Straight (h)
            right.setType(Tile.POWERLINE_H);

            // Bend
            tile.setType(Tile.POWERLINE_B_TR);
        }
        else if (down != null && left != null) {

            // Straight (v)
            down.setType(Tile.POWERLINE_V);

            // Straight (h)
            left.setType(Tile.POWERLINE_H);

            // Bend
            tile.setType(Tile.POWERLINE_B_BL);
        }
        else if (down != null && right != null) {

            // Straight (v)
            down.setType(Tile.POWERLINE_V);

            // Straight (h)
            right.setType(Tile.POWERLINE_H);

            // Bend
            tile.setType(Tile.POWERLINE_B_BR);
        }
        else if (up != null || down != null) {

            if (up != null) {

                if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (up.position().x-1) + " AND y = " + (up.position().y)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (up.position().x+1) + " AND y = " + (up.position().y)).length == 1)) {
                    // Check for:
                    // 1. a power line LEFT of the upper tile
                    // 2. a power line RIGHT of the upper tile

                    if (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (up.position().x) + " AND y = " + (up.position().y-1)).length == 1) {
                        // Check for:
                        // 1. a power line ON TOP of the upper tile

                        // 4-way
                        up.setType(Tile.POWERLINE_INTERSECTION);
                    }
                    else {

                        // 3-way (down)
                        up.setType(Tile.POWERLINE_H_D);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x-1)).length == 1) {
                    // Check for a power line to the LEFT of the upper tile

                    // Check for a power line on TOP of the upper tile
                    if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (up.position().y-1) + " AND x = " + (up.position().x)).length == 0) {
                        // Bend
                        up.setType(Tile.POWERLINE_B_BL);
                    }
                    else {
                        // 3 way (left)
                        up.setType(Tile.POWERLINE_V_L);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x+1)).length == 1) {

                    // Check for a power line to the RIGHT of the upper tile

                    // Check for a power line on TOP of the upper tile
                    if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (up.position().y-1) + " AND x = " + (up.position().x)).length == 0) {
                        // Bend
                        up.setType(Tile.POWERLINE_B_BR);
                    }
                    else {
                        // 3 way (right)
                        up.setType(Tile.POWERLINE_V_R);
                    }
                }
                else {

                    // Straight (v)
                    if (up.road() == 0) {
                        up.setType(Tile.POWERLINE_V);
                    }
                }
            }

            if (down != null) {               

                if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (down.position().x-1) + " AND y = " + (down.position().y)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (down.position().x+1) + " AND y = " + (down.position().y)).length == 1)) {
                    // Check for:
                    // 1. a power line LEFT of the lower tile
                    // 2. a power line RIGHT of the lower tile

                    if (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 1) {
                        // Check for:
                        // 1. a power line BELOW the lower tile

                        // 4-way
                        down.setType(Tile.POWERLINE_INTERSECTION);
                    }
                    else {

                        // 3-way (up)
                        down.setType(Tile.POWERLINE_H_U);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (down.position().y) + " AND x = " + (down.position().x-1)).length == 1) {
                    // Check for a power line to the LEFT of the lower tile

                    // Check for a power line BELOW the lower tile
                    if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (down.position().y-1) + " AND x = " + (down.position().x)).length == 0) {
                        // Bend
                        down.setType(Tile.POWERLINE_B_TL);
                    }
                    else {
                        // 3 way (left)
                        down.setType(Tile.POWERLINE_V_L);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (down.position().y) + " AND x = " + (down.position().x+1)).length == 1) {

                    // Check for a power line to the RIGHT of the lower tile

                    // Check for a power line BELOW the lower tile
                    if (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (down.position().y-1) + " AND x = " + (down.position().x)).length == 0) {
                        // Bend
                        down.setType(Tile.POWERLINE_B_TR);
                    }
                    else {
                        // 3 way (right)
                        down.setType(Tile.POWERLINE_V_R);
                    }
                }
                else {
                    // Straight (h)
                    if (down.road() == 0) {
                        down.setType(Tile.POWERLINE_V);
                    }
                }
            }

            if (tile.type() == Tile.STREET_H || tile.type() == Tile.POWERLINE_ROAD_H) {
                tile.setType(Tile.POWERLINE_ROAD_H);
            }
            else {
                // Straight (v)
                tile.setType(Tile.POWERLINE_V);
            }
        }
        else if (left != null || right != null) {

            if (left != null) {
                if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1)) {
                    // Check for a power line on top and below the left tile

                    if (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1) {
                        // Check for a power line to the left of the left tile

                        // 4-way
                        left.setType(Tile.POWERLINE_INTERSECTION);
                    }
                    else {
                        // 3-way (right)
                        left.setType(Tile.POWERLINE_V_R);
                    }
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1)) {
                    // Check for a power line on top of, and left of, the left tile

                    // 3-way (up)
                    left.setType(Tile.POWERLINE_H_U);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1)) {
                    // Check for a power line below, and left of, the left tile

                    // 3-way (down)
                    left.setType(Tile.POWERLINE_H_D);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 0)) {
                    // Check for a power line on top of, and below, the left tile

                    // Bend
                    left.setType(Tile.POWERLINE_B_TR);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (left.position().y-1) + " AND x = " + left.position().x).length == 0) && (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (left.position().y+1) + " AND x = " + left.position().x).length == 1)) {
                    // Check for a power line on top of, and below, the left tile

                    // Bend
                    left.setType(Tile.POWERLINE_B_BR);
                }
                else {
                    // Straight (h)
                    if (left.road() == 0) {
                        left.setType(Tile.POWERLINE_H);
                    }
                }
            }

            if (right != null) {

                if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1)) {
                    // Check for a power line on top and below the right tile

                    if (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1) {
                        // Check for a power line to the right of the right tile

                        // 4-way
                        right.setType(Tile.POWERLINE_INTERSECTION);
                    }
                    else {
                        // 3-way (left)
                        right.setType(Tile.POWERLINE_V_L);
                    }
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1)) {
                    // Check for a power line on top of the right tile

                    // 3-way (up)
                    right.setType(Tile.POWERLINE_H_U);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1)) {
                    // Check for a power line below right tile

                    // 3-way (down)
                    right.setType(Tile.POWERLINE_H_D);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 0)) {
                    // Check for a power line on top of, and below, the right tile

                    // Bend
                    right.setType(Tile.POWERLINE_B_TL);
                }
                else if ((Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (right.position().y-1) + " AND x = " + right.position().x).length == 0) && (Data.tilesMatchingCriteria("powergrid = 1 AND y = " + (right.position().y+1) + " AND x = " + right.position().x).length == 1)) {
                    // Check for a power line on top of, and below, the right tile

                    // Bend
                    right.setType(Tile.POWERLINE_B_BL);
                }
                else {
                    // Straight (h)
                    if (right.road() == 0) {
                        right.setType(Tile.POWERLINE_H);
                    }
                }
            }
            if (tile.type() == Tile.STREET_V || tile.type() == Tile.POWERLINE_ROAD_V) {
                tile.setType(Tile.POWERLINE_ROAD_V);
            }
            else {
                // Straight (h)
                tile.setType(Tile.POWERLINE_H);
            }
        }
        else {

            if (tile.type() == Tile.STREET_V || tile.type() == Tile.POWERLINE_ROAD_V) {
                tile.setType(Tile.POWERLINE_ROAD_V);
            }
            else if (tile.type() == Tile.STREET_H || tile.type() == Tile.POWERLINE_ROAD_H) {
                tile.setType(Tile.POWERLINE_ROAD_H);
            }
            else {
                // Straight (h)
                tile.setType(Tile.POWERLINE_H);
            }
        }

        if (left != null) {
            left.setPowerGrid(TYPE_ID);
            Data.updateTile(left);
        }
        if (right != null) {
            right.setPowerGrid(TYPE_ID);
            Data.updateTile(right);
        }
        if (down != null) {
            down.setPowerGrid(TYPE_ID);
            Data.updateTile(down);
        }
        if (up != null) {
            up.setPowerGrid(TYPE_ID);
            Data.updateTile(up);
        }

        tile.setPowerGrid(TYPE_ID);

        PowerGrid.updateTile(tile);
    }
}