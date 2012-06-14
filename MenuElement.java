/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * 'MenuElement' is an abstract class which provides a partial implementation of a menu UI element
 * 
 * @author Felix Mo
 * @version v1.0
 * @since 2012-05-01
 */

public abstract class MenuElement extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTANTS *
     */
    private static final float FONTSIZE = 14.0f;                // Font size for text
    /** Font used by all menu elements */
    protected static final Font FONT = CSFont.cabin(FONTSIZE);  // Font used by elements

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ATTRIBUTES *
     */
    /** The tile of the menu element. */
    protected String title;                                     // Element's title/text
    /** The element's frame, which includes its origin (in view) and its dimensions. */
    protected Rectangle frame;                                  // Frame (dimensions & origin)
    /** The container for the element's view. */
    protected GreenfootImage image;                             // Element's view is contained in this GreenfootImage
    /** The element's index; it's position relative to other elements within the same group. */
    protected int index;                                        // Element's index; it's position relative to other elements it is grouped with
    /** The element's state. */
    protected boolean active;                                   // Element's state

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Constructs a MenuElement
     * 
     * @param title The title of the menu element.
     * @param index The position of the menu element, relative to others within the same group.
     */
    public MenuElement(String title, int index) {
        this.title = title;
        this.index = index;
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ACCESSORS *
     */

    /**
     * Returns the element's frame.
     * 
     * @return The element's frame (as a {@link Rectangle}), which includes its origin (in view) and its dimensions.
     * @see Rectangle
     */
    public Rectangle frame() {
        return frame;
    }

    /**
     * Sets the element's frame. By setting the frame, a new container for the menu element's view will be created, thus a re-draw of the element is necessary.
     * 
     * @param frame The {@link Rectangle} that is to be the new frame to be used by the menu element.
     */
    public void setFrame(Rectangle frame) {
        this.frame = frame;

        // Create a new container for the menu element; reset view
        this.image = new GreenfootImage(this.frame.width, this.frame.height);
        setImage(image);
    } 

    /**
     * Returns the element's state (wether it is active or not).
     * 
     * @return True if the element is active
     */
    public boolean active() {
        return this.active;
    }
    
    /**
     * Sets the element's state. The method can be overriden for custom functionality when setting the element's active.
     * 
     * @param state The element's new state.
     */
    public void setActive(boolean state) {
        this.active = state;
    }

    /** 
     * Returns the element's index; it's position relative to other elements within the same group.
     * 
     * @return An integer with the element's index.
     */
    public int index() {
        return this.index;
    }
}