/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Point;
import java.awt.Color;

/**
 * View for a representation of a tile that can be used by {@link TileSelector}
 * 
 * @author Felix Mo
 * @version v0.1
 * @since 2010-03-14
 * 
 */
public class TileSelectorItem extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * INSTANCE VARIABLES *
     */
    
    /** The type/ID of the tile to be shown. This value is used to retrieve the tile image from ImageCache */
    private int type = 0;       
    
    /** Item's index; it's position relative to other items */
    private int index = 0;     
    
    /** Coordinate pair for the item's display origin */
    private Point origin;       

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Construct a TileSelectorItem.
     * 
     * @param type The type/ID of the tile to be shown. This value is used to retrieve the tile image from {@link ImageCache}
     * @param index The position of the item relative to other items. This value is zero-indexed. (First item would have an index of 0)
     * @param origin A {@link Point} with the item's display origin (top-left corner of the item) 
     */
    public TileSelectorItem(int type, int index, Point origin) {

        this.type = type;
        this.index = index;
        this.origin = origin;

        setImage(Tile.imageFromCacheForType(this.type));
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * GREENFOOT METHODS *
     */

    /**
     * Overrides act() method in 'Actor' from the Greenfoot framework to facilitate custom actions
     */
    public void act() {

        if (Greenfoot.mouseClicked(this)) {

            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(Street.SIZE_WIDTH, Street.SIZE_HEIGHT);
            Road.setActiveType(this.type);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ACCESSORS *
     */

    /**
     * @return A {@link Point} with the item's display origin (top-left corner of the item).
     * @see Point
     */
    public Point origin() {
        return this.origin;
    }
}
