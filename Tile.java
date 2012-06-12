/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;
import java.util.HashMap;

/**
 * Tile
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * Data structure for map tiles
 *
 */

public class Tile 
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */

    private HashMap properties;

    /*
     * CONSTANTS *
     */

    public static final int SIZE = 32;     // size of square tile; px

    /*
     * TILE TYPES *
     */

    public static final int EMPTY = 0;
    public static final int GROUND = 1;
    public static final int WATER = 3;

    // GRASS
    public static final int GRASS_1 = 38;

    // STREETS
    // H = horizontal
    // V = vertical
    // U = up
    // D = down
    // L = left
    // R = right
    // T = top
    // D = down
    // L = left
    // R = right
    public static final int BRIDGE_H = 65;
    public static final int BRIDGE_V = 66;
    public static final int STREET_H = 67;
    public static final int STREET_V = 68;
    public static final int STREET_B_TR = 69;
    public static final int STREET_B_BR = 70;
    public static final int STREET_B_BL = 71;
    public static final int STREET_B_TL = 72;
    public static final int STREET_H_U = 73;
    public static final int STREET_V_R = 74;
    public static final int STREET_H_D = 75;
    public static final int STREET_V_L = 76;
    public static final int STREET_INTERSECTION = 77;

    // POWER GRID
    public static final int POWERLINE_ROAD_H = 78;
    public static final int POWERLINE_ROAD_V = 79;
    public static final int POWERLINE_H = 211;
    public static final int POWERLINE_V = 212;
    public static final int POWERLINE_B_TR = 213;
    public static final int POWERLINE_B_BR = 214;
    public static final int POWERLINE_B_BL = 215;
    public static final int POWERLINE_B_TL = 216;
    public static final int POWERLINE_H_U = 217;
    public static final int POWERLINE_V_R = 218;
    public static final int POWERLINE_H_D = 219;
    public static final int POWERLINE_V_L = 220;
    public static final int POWERLINE_INTERSECTION = 221;
    
    // RECREATION
    public static final int PARK = 841;

    /*
     * IMAGE FILES *
     */

    // ---------------------------------------------------------------------------------------------------------------------

    public Tile(int dbID, Point position, int type) {
        this.properties = new HashMap();
        this.properties.put(Data.TILES_ID, new Integer(dbID));
        this.properties.put(Data.TILES_X, new Integer(position.x));
        this.properties.put(Data.TILES_Y, new Integer(position.y));
        this.properties.put(Data.TILES_TYPE, new Integer(type));
        this.properties.put(Data.TILES_ZONE, new Integer(0));
        this.properties.put(Data.TILES_ZONEID, new Integer(-1));
        this.properties.put(Data.TILES_ROAD, new Integer(0));
        this.properties.put(Data.TILES_POWERED, new Integer(0));
        this.properties.put(Data.TILES_POWERGRID_TYPE, new Integer(0));
        this.properties.put(Data.TILES_RECREATION_TYPE, new Integer(0));
    }

    public Tile(HashMap properties) {
        this.properties = properties;
    }

    /*
     * ACCESSORS *
     */

    public Object get(String key) {
        return this.properties.get(key);
    }

    public int dbID() {
        return ((Integer)properties.get(Data.TILES_ID)).intValue();
    }

    public Point position() {
        return new Point((Integer)properties.get(Data.TILES_X), (Integer)properties.get(Data.TILES_Y));
    }

    public int type() {
        return ((Integer)properties.get(Data.TILES_TYPE)).intValue();
    }

    public void setType(int value) {
        properties.put(Data.TILES_TYPE, new Integer(value));
    }

    public int zone() {
        return ((Integer)properties.get(Data.TILES_ZONE)).intValue();
    }

    public void setZone(int value) {
        properties.put(Data.TILES_ZONE, new Integer(value));
    }

    public int zoneID() {
        return ((Integer)properties.get(Data.TILES_ZONEID)).intValue();
    }

    public void setZoneID(int id) {
        properties.put(Data.TILES_ZONEID, new Integer(id));
    }

    public int road() {
        return ((Integer)properties.get(Data.TILES_ROAD)).intValue();
    }

    public void setRoad(int value) {
        properties.put(Data.TILES_ROAD, new Integer(value));
    }

    public int powered() {
        return ((Integer)properties.get(Data.TILES_POWERED)).intValue();
    }

    public void setPowered(int value) {
        properties.put(Data.TILES_POWERED, new Integer(value));
    }

    public int powerGrid() {
        return ((Integer)properties.get(Data.TILES_POWERGRID_TYPE)).intValue();
    }

    public void setPowerGrid(int value) {
        properties.put(Data.TILES_POWERGRID_TYPE, new Integer(value));    
    }

    public int recreation() {
        return ((Integer)properties.get(Data.TILES_RECREATION_TYPE)).intValue();
    }

    public void setRecreation(int value) {
        properties.put(Data.TILES_RECREATION_TYPE, new Integer(value));    
    }

    public GreenfootImage image() {
        return ImageCache.get(((Integer)this.properties.get(Data.TILES_TYPE)).intValue());
    }

    /*
     * HELPERS *
     */

    public static GreenfootImage imageFromDiskForType(Integer type) {

        return new GreenfootImage("images/tiles/" + type + ".png");
    }

    public static GreenfootImage imageFromCacheForType(Integer value) {

        return ImageCache.get(value);
    }
}

