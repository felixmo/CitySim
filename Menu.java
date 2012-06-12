/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Menu view and view controller; extends MenuElement.
 * 
 * @author Felix Mo
 * @version v0.1
 * @since 2012-04-19
 */

public class Menu extends MenuElement
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * INSTANCE VARIABLES
     */

    private MenuBarItem menuBarItem;        // The menu bar item the menu belongs to
    private ArrayList<MenuItem> menuItems;  // The MenuItem actors that belong to the Menu
    private int activeIndex = -1;           // The index of the current active menu item (-1 = none)
    private MouseInfo mouseInfo;            // A reference to the MouseInfo object provided by the Greenfoot framework
    
    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Constructs a Menu.
     * 
     * @param menuBarItem The menu bar item that the menu belongs to.
     * @param items The items in the menu (as {@link Strings})
     */
    public Menu(MenuBarItem menuBarItem, ArrayList<String> items) {

        super("", 0);
        this.menuBarItem = menuBarItem;
        setItems(items);
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * GREENFOOT METHODS
     */

    /**
     * Draws, and activates, the menu when it is added to the world.
     * 
     * @param World The {@link World} the object was added to.
     */
    protected void addedToWorld(World world) {
        // Activate this menu
        this.active = true;
        // Draw the menu if it has not already been drawn
        if (this.image != null) draw();
    }

    /**
     * The procedure to draw the menu and its menu items.
     */
    private void draw() {

        Point origin = this.frame.getLocation();

        this.image.clear();

        // Draw menu
        this.image.setColor(Color.WHITE);
        this.image.fillRect(0, 0, this.frame.width, this.frame.height);
        // Draw menu outline
        this.image.setColor(Color.BLACK);
        this.image.drawRect(0, 0, this.frame.width, this.frame.height);

        // Add menu items to menu
        for (MenuItem menuItem : menuItems) {
            City.getInstance().addObject(menuItem, menuItem.frame().x+(int)menuItem.frame().width/2, menuItem.frame().y);
        }
    }

    /**
     * The procedure containing the menu's behaviour. The menu will highlight menu items as the cursor hovers over them.
     */
    public void act() {

        // Get mouse position
        Point mouse = null;

        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        // Get the menu item the cursor is currently hovering over
        if (active) {

            if (mouseInfo != null) {
                mouse = new Point(mouseInfo.getX(), mouseInfo.getY());

                // Check horizontal bounds
                if (mouse.x <= this.frame.width + this.frame.x && mouse.x >= menuBarItem.frame().x) {
                    // Check vertical bounds
                    if (mouse.y <= this.frame.height + this.frame.y && mouse.y >= menuBarItem.frame().height) {

                        // Iterate through all the menu items to check if cursor is within the bounds of their frames
                        for (MenuItem item : menuItems) {

                            Rectangle frame = item.frame();
                            Point origin = item.frame().getLocation();

                            if (mouse.y >= origin.y && mouse.y <= origin.y + frame.height) {

                                // Activate menu item IF it is not the current active item (prevents redundancy)
                                if (menuItems.indexOf(item) != this.activeIndex) {
                                    didHoverOverMenuItem(item);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * MENU *
     */

    /**
     * Menu behaviour for hovering over a menu item.
     * 
     * @param menuItem The menu item that the cursor is currently hovering over.
     */
    public void didHoverOverMenuItem(MenuItem menuItem) {

        // Activate the menu item
        for (MenuItem item : menuItems) {
            item.setActive(item == menuItem ? (active ? true : false) : false);
        }

        // Update index
        this.activeIndex = menuItem.index();
    } 
    
    
    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ACCESSORS *
     */

    /**
     * Returns the {@link MenuItems} that belongs to the menu.
     * 
     * @return The {@link MenuItems} belonging to the menu.
     */
    public ArrayList<MenuItem> menuItems() {
        return this.menuItems;
    }

    /**
     * Sets the items of the menu.
     * 
     * @param items The titles (as {@link Strings}) of the menu's items.
     */
    public void setItems(ArrayList<String> items) {

        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(512, 28).getAwtImage().getGraphics().getFontMetrics(FONT); 
        int width = 0;
        int index = 0;
        
        // Find the widest menu item and use it's width + padding as the menu width
        for (String item : items) {
            if (fontMetrics.stringWidth(item) > width) {
                width = fontMetrics.stringWidth(item)+28;
            }
        }

        // Create the 'MenuItem' actors 

        this.menuItems = new ArrayList<MenuItem>(items.size());

        for (String item : items) {
            MenuItem menuItem = new MenuItem(item, this, index);
            Rectangle miFrame = new Rectangle(menuBarItem.frame().x, menuBarItem.frame().y+14+10+index*24, width, 22);
            menuItem.setFrame(miFrame);
            this.menuItems.add(menuItem);
            index++;
        }

        int height = items.size() * 24;     // 14px + 4px padding
        this.frame = new Rectangle(menuBarItem.frame().x, menuBarItem.frame().y+14, width, height);

        // Recreate the menu's view with the new dimensions
        this.image = new GreenfootImage(width, height);
        setImage(this.image);
    }

    /**
     * Sets the state of the menu.
     * 
     * @param The new state of the menu.
     */
    public void setActive(boolean state) {

        this.active = state;
        
        // If the menu is now inactive
        if (!active) {
            // Reset the active index to -1 (no selection);
            this.activeIndex = -1;
            // Deactivate the menu bar item that this menu belogns to
            this.menuBarItem.menuBar().changeItemStateTo(this.menuBarItem, false);
        }
    }
}
