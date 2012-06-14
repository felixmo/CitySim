/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;
import java.awt.Rectangle;

/**
 * Write a description of class Query here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Query extends Tool
{
    public static final int TYPE_ID = 2;
    public static final int SIZE_WIDTH = 1;
    public static final int SIZE_HEIGHT = 1;
    public static final String NAME = "Query";
    public static final int PRICE = 0;
    
    public static void query(Zone zone) {
        new QueryModalWindow(zone);
    }
}
