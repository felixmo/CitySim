/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Color;
import java.lang.Math;
import java.awt.Point;

/**
 * Minimap_Viewport
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 03-06-2012
 *
 * Minimap viewport view and view controller
 *
 */

public class MinimapViewport extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    private static MinimapViewport instance;
    
    /*
     * INSTANCE VARIABLES *
     */

    private GreenfootImage image;   // GreenfootImage containing the minimap viewport view

    private Point origin;   // Origin of the viewport
    private boolean move;   // Allows the viewport to move; used to regulate movement 
    //     private float scale = 1.0f;  // Scale of the minimap; default: 2 (100x100 cells * 2 = 200x200 px)

    // ---------------------------------------------------------------------------------------------------------------------

    public MinimapViewport(Point origin) {
        
        MinimapViewport.instance = this;

        this.origin = origin;

        this.image = new GreenfootImage(200, 200);
        setImage(this.image);

        draw();
    }

    public void act() {

        if (Greenfoot.mouseClicked(this) || Greenfoot.mouseDragged(this)) {
            // Moves the minimap viewport and map to the cell that was clicked on

            MouseInfo mouseInfo = Greenfoot.getMouseInfo();

            // Coordinates of the cursor, relative to the minimap
            int x = mouseInfo.getX() - 12 - 18; // subtract 18 to centre viewport
            int y = mouseInfo.getY() - 558 - 9; // subtract 9 to centre viewport
            // X | Subtract distance from left edge of HUD to left edge of minimap from X
            // Y | Subtract distance from the top of the game to the top of the minimap from Y
            
            x = Math.max(0, x);
            x = Math.min(200-18, x);
            
            y = Math.max(0, y);
            y = Math.min(200-18, y);
            
            origin.setLocation(x, y);
            draw();
            ((City)getWorld()).didMoveViewportTo(x, y);
        }
    }

    // Moves the minimap viewport to the specified location and redraws it there
    public void didMoveViewportToCell(int x, int y) {
        origin.setLocation(x, y);

        move = true;
        draw();
    }

    // Draws the viewport
    private void draw() {

        image.clear();

        image.setColor(Color.RED);
        image.drawRect(origin.x, origin.y, 35, 18);
    }
    
    public static MinimapViewport getInstance() {
        return instance;
    }
}
