/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Point;

/**
 * View & view controller for a dialog in which users can select a {@link TileSelctorItem}
 * 
 * @author Felix Mo
 * @version v0.1
 * @since 2010-03-14
 * 
 */
public class TileSelector extends Actor
{
    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTANTS *
     */

    /* The origin is set to the top-left corner of the view */
    /** X coordinate of the default origin */
    public static final int ORIGIN_X = 512;
    /** Y coordinate of the default origin */
    public static final int ORIGIN_Y = 30;

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * INSTANCE VARIABLES *
     */
 
    /** Holds all of the TileSelector to be displayed */
    private TileSelectorItem[] items;    

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Construct a TileSelector
     * 
     * @param tiles The ids of the types of tiles to be displayed; the images will be automatically loaded from the image cache
     */
    public TileSelector(int[] tiles) {

        int space = (1024 - (tiles.length+1 * Tile.SIZE)) / tiles.length+1;

        this.items = new TileSelectorItem[tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            this.items[i] = new TileSelectorItem(tiles[i], i, new Point(space*(i+1)-Tile.SIZE, ORIGIN_Y));

            TileSelectorItem item = this.items[i];
            City.getInstance().addObject(item, item.origin().x, item.origin().y);
        }

        setImage(new GreenfootImage("images/notif.png"));
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * GREENFOOT METHODS *
     */

    /**
     * Overrides act() method in 'Actor' from the Greenfoot framework to facilitate custom actions
     */
    public void act() {

        if (Greenfoot.isKeyDown("escape")) {
            City.getInstance().removeTileSelector();
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ACCESSORS *
     */

    /**
     * @return An array of the {@link TileSelectorItems} being shown by the TileSelector. 
     * @see TileSelectorItem
     */
    public TileSelectorItem[] items() {
        return this.items;
    }
}
