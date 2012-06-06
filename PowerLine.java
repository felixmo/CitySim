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

        CSLogger.sharedLogger().info("Building power line on tile (" + tile.dbID() + ")");

        Tile up = null, down = null, left = null, right = null;

        Tile[] nearby = Data.tilesMatchingCriteriaTouchingTile(tile, "powergrid_type = 1");
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
            up.setType(741);

            // Straight (v)
            left.setType(740);

            // Bend
            tile.setType(745);
        }
        else if (up != null && right != null) {

            // Straight (h)
            up.setType(741);

            // Straight (h)
            right.setType(740);

            // Bend
            tile.setType(742);
        }
        else if (down != null && left != null) {

            // Straight (v)
            down.setType(741);

            // Straight (h)
            left.setType(740);

            // Bend
            tile.setType(744);
        }
        else if (down != null && right != null) {

            // Straight (v)
            down.setType(741);

            // Straight (h)
            right.setType(740);

            // Bend
            tile.setType(743);
        }
        else if (up != null || down != null) {

            if (up != null) {

                if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (up.position().x-1) + " AND y = " + (up.position().y)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (up.position().x+1) + " AND y = " + (up.position().y)).length == 1)) {
                    // Check for:
                    // 1. a power line LEFT of the upper tile
                    // 2. a power line RIGHT of the upper tile

                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (up.position().x) + " AND y = " + (up.position().y-1)).length == 1) {
                        // Check for:
                        // 1. a power line ON TOP of the upper tile

                        // 4-way
                        up.setType(750);
                    }
                    else {

                        // 3-way (down)
                        up.setType(748);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x-1)).length == 1) {
                    // Check for a power line to the LEFT of the upper tile

                    // Check for a power line on TOP of the upper tile
                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (up.position().y-1) + " AND x = " + (up.position().x)).length == 0) {
                        // Bend
                        up.setType(744);
                    }
                    else {
                        // 3 way (left)
                        up.setType(749);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (up.position().y) + " AND x = " + (up.position().x+1)).length == 1) {

                    // Check for a power line to the RIGHT of the upper tile

                    // Check for a power line on TOP of the upper tile
                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (up.position().y-1) + " AND x = " + (up.position().x)).length == 0) {
                        // Bend
                        up.setType(743);
                    }
                    else {
                        // 3 way (right)
                        up.setType(747);
                    }
                }
                else {

                    // Straight (v)
                    if (up.road() == 0) {
                        up.setType(741);
                    }
                }
            }

            if (down != null) {               

                if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (down.position().x-1) + " AND y = " + (down.position().y)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (down.position().x+1) + " AND y = " + (down.position().y)).length == 1)) {
                    // Check for:
                    // 1. a power line LEFT of the lower tile
                    // 2. a power line RIGHT of the lower tile

                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 1) {
                        // Check for:
                        // 1. a power line BELOW the lower tile

                        // 4-way
                        down.setType(750);
                    }
                    else {

                        // 3-way (up)
                        down.setType(746);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (down.position().y) + " AND x = " + (down.position().x-1)).length == 1) {
                    // Check for a power line to the LEFT of the lower tile

                    // Check for a power line BELOW the lower tile
                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (down.position().y-1) + " AND x = " + (down.position().x)).length == 0) {
                        // Bend
                        down.setType(745);
                    }
                    else {
                        // 3 way (left)
                        down.setType(749);
                    }
                }
                else if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (down.position().y) + " AND x = " + (down.position().x+1)).length == 1) {

                    // Check for a power line to the RIGHT of the lower tile

                    // Check for a power line BELOW the lower tile
                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (down.position().y-1) + " AND x = " + (down.position().x)).length == 0) {
                        // Bend
                        down.setType(742);
                    }
                    else {
                        // 3 way (right)
                        down.setType(747);
                    }
                }
                else {
                    // Straight (h)
                    if (down.road() == 0) {
                        down.setType(741);
                    }
                }
            }

            if (tile.type() == 101 || tile.type() == 112) {
                tile.setType(112);
            }
            else {
                // Straight (v)
                tile.setType(741);
            }
        }
        else if (left != null || right != null) {

            if (left != null) {
                if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1)) {
                    // Check for a power line on top and below the left tile

                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1) {
                        // Check for a power line to the left of the left tile

                        // 4-way
                        left.setType(750);
                    }
                    else {
                        // 3-way (right)
                        left.setType(747);
                    }
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1)) {
                    // Check for a power line on top of, and left of, the left tile

                    // 3-way (up)
                    left.setType(746);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x-1) + " AND y = " + (left.position().y)).length == 1)) {
                    // Check for a power line below, and left of, the left tile

                    // 3-way (down)
                    left.setType(748);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (left.position().x) + " AND y = " + (left.position().y+1)).length == 0)) {
                    // Check for a power line on top of, and below, the left tile

                    // Bend
                    left.setType(742);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (left.position().y-1) + " AND x = " + left.position().x).length == 0) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (left.position().y+1) + " AND x = " + left.position().x).length == 1)) {
                    // Check for a power line on top of, and below, the left tile

                    // Bend
                    left.setType(743);
                }
                else {
                    // Straight (h)
                    if (left.road() == 0) {
                        left.setType(740);
                    }
                }
            }

            if (right != null) {

                if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1)) {
                    // Check for a power line on top and below the right tile

                    if (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1) {
                        // Check for a power line to the right of the right tile

                        // 4-way
                        right.setType(750);
                    }
                    else {
                        // 3-way (left)
                        right.setType(749);
                    }
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1)) {
                    // Check for a power line on top of the right tile

                    // 3-way (up)
                    right.setType(746);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x+1) + " AND y = " + (right.position().y)).length == 1)) {
                    // Check for a power line below right tile

                    // 3-way (down)
                    right.setType(748);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y-1)).length == 1) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND x = " + (right.position().x) + " AND y = " + (right.position().y+1)).length == 0)) {
                    // Check for a power line on top of, and below, the right tile

                    // Bend
                    right.setType(745);
                }
                else if ((Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (right.position().y-1) + " AND x = " + right.position().x).length == 0) && (Data.tilesMatchingCriteria("powergrid_type = 1 AND y = " + (right.position().y+1) + " AND x = " + right.position().x).length == 1)) {
                    // Check for a power line on top of, and below, the right tile

                    // Bend
                    right.setType(744);
                }
                else {
                    // Straight (h)
                    if (right.road() == 0) {
                        right.setType(740);
                    }
                }
            }
            if (tile.type() == 102 || tile.type() == 113) {
                tile.setType(113);
            }
            else {
                // Straight (h)
                tile.setType(740);
            }
        }
        else {

            if (tile.type() == 102 || tile.type() == 113) {
                tile.setType(113);
            }
            else {
                // Straight (h)
                tile.setType(740);
            }
        }

        if (left != null) {
            left.setPowerGridType(TYPE_ID);
            Data.updateTile(left);
        }
        if (right != null) {
            right.setPowerGridType(TYPE_ID);
            Data.updateTile(right);
        }
        if (down != null) {
            down.setPowerGridType(TYPE_ID);
            Data.updateTile(down);
        }
        if (up != null) {
            up.setPowerGridType(TYPE_ID);
            Data.updateTile(up);
        }

        tile.setPowerGridType(TYPE_ID);

        PowerGrid.updateTile(tile);
    }
}