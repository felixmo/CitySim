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
    public static final int GROUND = 100;
    public static final int WATER = 200;
    // BEACH
    public static final int BEACH_TOP = 201;
    public static final int BEACH_TOP_LEFT = 202;
    public static final int BEACH_TOP_RIGHT = 203;
    public static final int BEACH_BOTTOM = 204;
    public static final int BEACH_BOTTOM_LEFT = 205;
    public static final int BEACH_BOTTOM_RIGHT = 206;
    public static final int BEACH_LEFT = 207;
    public static final int BEACH_RIGHT = 208;
    // GRASS
    public static final int GRASS_1 = 301;
    public static final int GRASS_2 = 302;
    public static final int GRASS_3 = 303;
    public static final int GRASS_4 = 304;
    public static final int GRASS_5 = 305;
    public static final int GRASS_TOP_LEFT = 306;
    public static final int GRASS_TOP = 307;
    public static final int GRASS_TOP_RIGHT = 308;
    public static final int GRASS_LEFT = 309;
    // RES. ZONE
    public static final int RESIDENTIAL_TOP_LEFT = 400;
    public static final int RESIDENTIAL_TOP = 401;
    public static final int RESIDENTIAL_TOP_RIGHT = 402;
    public static final int RESIDENTIAL_LEFT = 403;
    public static final int RESIDENTIAL = 404;
    public static final int RESIDENTIAL_RIGHT = 405;
    public static final int RESIDENTIAL_BOTTOM_LEFT = 406;
    public static final int RESIDENTIAL_BOTTOM = 407;
    public static final int RESIDENTIAL_BOTTOM_RIGHT = 408;

    // STREETS
    // U = up
    // D = down
    // L = left
    // R = right
    public static final int STREET_HORIZONTAL = 101;
    public static final int STREET_VERTICAL = 102;
    public static final int STREET_BEND_TOPRIGHT = 103;
    public static final int STREET_BEND_BOTTOMRIGHT = 104;
    public static final int STREET_BEND_BOTTOMLEFT = 105;
    public static final int STREET_BEND_TOPLEFT = 106;
    public static final int STREET_HORIZONTAL_D_VERTICAL_U = 107;
    public static final int STREET_VERTICAL_L_HORIZTONAL_R = 108;
    public static final int STREET_HORIZONTAL_U_VERTICAL_D = 109;
    public static final int STREET_VERTICAL_R_HORIZTONAL_L = 110;
    public static final int STREET_INTERSECTION = 111;

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

    public int recreationType() {
        return ((Integer)properties.get(Data.TILES_RECREATION_TYPE)).intValue();
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

