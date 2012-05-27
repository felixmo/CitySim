/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * Minimap
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 03-06-2012
 *
 * Minimap view for HUD
 *
 */

public class Minimap extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    private static Minimap sharedInstance;
    
    /*
     * COLORS
     */
    private final Color TYPE_GROUND = new Color(216, 146, 125);
    private final Color ZONE_RESIDENTIAL = new Color(0, 207, 0);
    private final Color ZONE_INDUSTRIAL = new Color(255, 255, 0);
    private final Color ZONE_COMMERCIAL = new Color(102, 102, 230);
    
    /*
     * MINIMAP PROPERTIES *
     */
    private final Rectangle FRAME = new Rectangle(112, 658, 200, 200);
    private final int tileSize = 1; // px

    /*
     * INSTANCE VARIABLES
     */

    private Point viewportOrigin = new Point(0, 0);     // Viewport origin
    
    private boolean shouldUpdate = false;

    // ---------------------------------------------------------------------------------------------------------------------

    public Minimap() {
       
        Minimap.sharedInstance = this;

        // Inital draw on load
        draw();
    }  

    // Draws the minimap
    public void draw() {
        
        // A new image is created for the minimap, drawn onto, and the applied as the Actor's image
        // Drawing of the minimap is done this way so that there won't be any artifacting when draw() is called by a thread other than the main thread
        GreenfootImage image = new GreenfootImage(FRAME.width, FRAME.height);
        image.setTransparency(150);

        // Get map
        ArrayList<ArrayList<Tile>> map = Data.tiles();

        // Position of the minimap tile being drawn
        int x = 0;
        int y = 0;

        // Iterate through every map tile and draw it onto minimap, adjusting the position for the next tile with each iteration
        // Minimap is drawn column by column
        for (int i = 0; i < Map.getInstance().SIZE_COLUMNS; i++) {
            for (int j = 0; j < Map.getInstance().SIZE_ROWS; j++) {
                
                Tile tile = (Tile)map.get(i).get(j);
                // Get the color to draw based on either the tile's type or zone (if zoned)
                if (tile.zone() > 0) {
                    image.setColor(colorForTileOfZone(tile.zone()));
                }
                else {
                    image.setColor(colorForTileOfType(tile.type())); 
                }
                
                image.fillRect(x, y, tileSize, tileSize); // Minimap tiles are 2px * 2px
                y+=tileSize;
            }
            
            // Reset Y to top of the column 
            y = 0;

            x+=tileSize;
        }
        
        setImage(image);
    }

    /*
     * ACCESSORS *
     */

    public Rectangle frame() {
        return FRAME;
    }
    
    public static Minimap getInstance() {
        return sharedInstance;
    }
    
    public boolean shouldUpdate() {
        return this.shouldUpdate;
    }
    
    public void setShouldUpdate(boolean value) {
        this.shouldUpdate = value;
    }

    /*
     * HELPERS *
     */

    // Returns the color that represents each type of tile
    private Color colorForTileOfType(int type) {
        switch (type) {
            case Tile.EMPTY: return Color.BLACK;
            case Tile.WATER: return Color.BLUE;
            case Tile.GROUND: return TYPE_GROUND;

            default: return Color.BLACK;
        }
    }
    
    private Color colorForTileOfZone(int zone) {
        switch (zone) {
            case ResidentialZone.TYPE_ID: return ZONE_RESIDENTIAL;
            case IndustrialZone.TYPE_ID: return ZONE_INDUSTRIAL;
            case CommercialZone.TYPE_ID: return ZONE_COMMERCIAL;
            
            default: return Color.BLACK;
        }
    }
}
