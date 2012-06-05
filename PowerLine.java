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

        Tile[] nearby = Data.tilesMatchingCriteriaTouchingTile(tile, "(powergrid_type = 1 OR powergrid_type = 2)");
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
            up.setType(740);

            // Straight (v)
            left.setType(741);

            // Bend
            tile.setType(745);
        }
        if (up != null && right != null) {

            // Straight (h)
            up.setType(740);

            // Straight (v)
            right.setType(741);

            // Bend
            tile.setType(742);
        }
        if (down != null && left != null) {

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
                if (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND y = " + (up.position().y) + " AND x = " + (up.position().x-1)).length == 1) {
                    
                    // Check for a road to the LEFT of the upper tile
                    up.setType(744);
                }
                else if (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND y = " + (up.position().y) + " AND x = " + (up.position().x+1)).length == 1) {
                    
                    // Check for a road to the RIGHT of the upper tile
                    up.setType(743);
                }
                else {
                    
                    // Straight (v)
                    up.setType(741);
                }
            }

            if (down != null) {
                if ((Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (down.position().x-1) + " AND y = " + down.position().y).length == 0) && (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 0)) {
                    // Check for:
                    // 1. a road LEFT of lower tile
                    // 2. a road BELOW the lower tile

                    // Bend
                    down.setType(742);
                }
                else if ((Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (down.position().x) + " AND y = " + (down.position().y+1)).length == 0) && (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (down.position().x+1) + " AND y = " + (down.position().y)).length == 0)) {
                    // Check for:
                    // 1. a road BELOW the lower tile
                    // 2. a road RIGHT of the lower tile

                    // Bend
                    down.setType(745);
                }
                else {
                    // Straight (v)
                    down.setType(741);
                }
            }

            // Straight (v)
            tile.setType(741);
        }
        else if (left != null || right != null) {

            if (left != null) {
                if (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (left.position().x-1) + " AND y = " + left.position().y).length == 0) {
                    // Check for a road to the left of the left tile

                    // Bend
                    left.setType(742);
                }
                else {
                    // Straight (h)
                    left.setType(740);
                }
            }

            if (right != null) {
                if (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND x = " + (right.position().x+1) + " AND y = " + right.position().y).length == 0) {
                    // Check for a road to the right of the right tile

                    // Bend
                    right.setType(745);
                }
                else if (Data.tilesMatchingCriteria("(powergrid_type = 1 OR powergrid_type = 2) AND y = " + (right.position().y+1) + " AND x = " + right.position().x).length == 1) {
                    // Check for a road above the right tile

                    // Bend
                    right.setType(744);
                }
                else {
                    // Straight (h)
                    right.setType(740);
                }
            }

            // Straight (h)
            tile.setType(740);
        }
        else {
            
            // Straight (h)
            tile.setType(740);
        }

        tile.setPowerGridType(TYPE_ID);

        Data.updateTile(left);
        Data.updateTile(right);
        Data.updateTile(down);
        Data.updateTile(up);
        
        PowerGrid.updateTile(tile);
    }
/*
    public static void buildPowerLine(Tile tile, int type) {
        Cash.subtract(PRICE);
        tile.setType(type);
        tile.setPowerGridType(TYPE_ID);

        PowerGrid.updateTile(tile);
    }
    */
}