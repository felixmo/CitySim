import greenfoot.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Font;

/**
 * MenuBar
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-12-2012
 * 
 * Menu bar view and view controller
 * 
 */

public class MenuBar extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */
    
    // TO DO:
    // font
    // font metrics

    private GreenfootImage base; 
    private ArrayList<String> items;
    private ArrayList<GreenfootImage> itemImgs;
    private ArrayList<Rectangle> itemRects;
    
    private MouseInfo mouseInfo;
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    public MenuBar(ArrayList<String> items)
    {
        setImage("menu.png");   
        this.items = items;
    }
    
    private void populate() {
        // Populate menu bar
    }
}
