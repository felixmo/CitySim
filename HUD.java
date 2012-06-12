/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * HUD
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-17-2012
 * 
 * 
 * 
 */

public class HUD extends Actor {

    // ---------------------------------------------------------------------------------------------------------------------

    private static HUD instance;
    
    /*
     * INSTANCE VARIABLES
     */

    private HashMap labels = new HashMap(4);                                             // Holds references to all labels for easy access
    private Minimap minimap;                                                                        // The minimap

    /*
     * LABEL RECTS. *
     */

    private static final Rectangle CITYNAME_RECT = new Rectangle(645, 594, 300, 58);
    private static final Rectangle POPNUM_RECT = new Rectangle(415, 725, 135, 48);
    private static final Rectangle DATE_RECT = new Rectangle(415, 665, 135, 48);
    private static final Rectangle CASH_RECT = new Rectangle(830, 665, 135, 48);

    /*
     * IDENTIFIERS *
     */

    public static final String NAME = "name";
    public static final String POPULATION = "population";
    public static final String DATE = "date";
    public static final String CASH = "cash";

    // ---------------------------------------------------------------------------------------------------------------------

    public HUD() {
        
        HUD.instance = this;

        this.minimap = new Minimap();

        setImage("hud.png");

        // Initalize labels and store references in map
        labels.put(NAME, new Label(CITYNAME_RECT));
        labels.put(POPULATION, new Label(POPNUM_RECT));
        labels.put(DATE, new Label(DATE_RECT));
        labels.put(CASH, new Label(CASH_RECT));
    }

    // Override Greenfoot method to specify custom procedure when this 'Actor' is added to the 'World'
    protected void addedToWorld(World world) {

        // Add minimap to HUD
        world.addObject(minimap, minimap.frame().x, minimap.frame().y);   
    }

    // Refreshes the values on the HUD with the values provided
    public void refresh(HashMap values) {

        // Maps the values from the provided LinkedHashMap to the values on the HUD
        Iterator iterator = values.keySet().iterator();
        while (iterator.hasNext()) {

            String key = (String)iterator.next();
            Object object = values.get(key);
            String className = object.getClass().getName();
            String value = null;

            if (className.equals("java.lang.String")) {
                value = (String)object;   
            }
            else if (className.equals("java.lang.Integer")) {
                value = Integer.toString((Integer)object);
            }

            refreshLabel(key, value);
        }
    }

    // Changes the values in individual labels
    private void refreshLabel(String key, String value) {

        CSLogger.sharedLogger().finer("Updating label (\"" + key + "\")" + " in HUD with value: " + value);

        Label label = (Label)labels.get(key);

        if (label.getWorld() == null) {
            getWorld().addObject(label, label.frame().x, label.frame().y);
        }

        label.setText(value);
    }

    /*
     * ACCESSORS *
     */

    public Minimap minimap() {
        return minimap;
    }
    
    public static HUD getInstance() {
        return instance;
    }
}
