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
    private int score = -1;
    private MouseInfo mouseInfo;

    /*
     * LABEL RECTS. *
     */

    private static final Rectangle CITYNAME_RECT = new Rectangle(632, 590, 300, 58);
    private static final Rectangle POPNUM_RECT = new Rectangle(449, 744, 200, 50);
    private static final Rectangle DATE_RECT = new Rectangle(415, 685, 135, 48);
    private static final Rectangle CASH_RECT = new Rectangle(860, 685, 200, 48);
    private static final Rectangle TAX_RECT = new Rectangle(930, 743, 200, 48);
    private static final Rectangle RATING_RECT = new Rectangle(515, 610, 150, 20);
    
    /*
     * IDENTIFIERS *
     */

    public static final String NAME = "name";
    public static final String POPULATION = "population";
    public static final String DATE = "date";
    public static final String CASH = "cash";
    public static final String TAXRATE = "taxrate";
    public static final String SCORE = "score";

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
        labels.put(TAXRATE, new Label(TAX_RECT));
    }

    // Override Greenfoot method to specify custom procedure when this 'Actor' is added to the 'World'
    protected void addedToWorld(World world) {

        // Add minimap to HUD
        world.addObject(minimap, minimap.frame().x, minimap.frame().y);   
    }

    public void act() {

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (Greenfoot.mouseClicked(null)) {
            if (mouseInfo != null) {
                Point pos = new Point(mouseInfo.getX(), mouseInfo.getY());
                
                // Rating
                if (RATING_RECT.contains(pos)) {
                    Issues.dialog();
                }
                // Tax rate
                else if (pos.x >= 740 && pos.x <= 880 && pos.y >= 725 && pos.y <= 745) {
                    Taxation.showDialog();
                }
            }
        }
    }

    // Refreshes the values on the HUD with the values provided
    public void refresh(HashMap values) {

        // Maps the values from the provided LinkedHashMap to the values on the HUD
        Iterator iterator = values.keySet().iterator();
        while (iterator.hasNext()) {

            String key = (String)iterator.next();
            Object object = values.get(key);
            if (object != null) {

                if (key.equals(HUD.SCORE)) {
                    int newScore = ((Integer)object).intValue();

                    if (!((int)(this.score/20) == (int)(newScore/20))) {

                        this.score = newScore;

                        int x = 527;
                        int y = 70;

                        GreenfootImage image = this.getImage();
                        image.clear();
                        image.drawImage(new GreenfootImage("hud.png"), 0, 0);

                        for (int i = 1; i <= (int)(score/20); i++) {
                            image.drawImage(new GreenfootImage("star.png"), x+(25*(i-1)), y);
                        }
                    }
                }
                else {

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
