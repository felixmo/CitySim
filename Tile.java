import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;

/**
 * Rectangle
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
    private int dbID = 0;       // SQL db id
    private Point position;
    private int type = 0;       // Type of tile (detail)
    private int zone = 0;       // Type of zone
    private int zoneID = -1;    // ID of the zone the tile belongs to; -1 = none
    private int road = 0;       // Type of road
    // Utilites
    private boolean powered = false; 
    private boolean hasWater = false;

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

    public Tile(int dbID, Point position, int type, int zone, int zoneID, int road) {
        this.dbID = dbID;
        this.position = position;
        this.type = type;
        this.zone = zone;
        this.zoneID = zoneID;
        this.road = road;
    }

    /*
     * ACCESSORS *
     */

    public int dbID() {
        return dbID;
    }

    public Point position() {
        return position;
    }

    public int type() {
        return type;
    }

    public void setType(int value) {
        type = value;
    }

    public int zone() {
        return zone;
    }

    public void setZone(int value) {
        zone = value;
    }
    
    public int zoneID() {
        return this.zoneID;
    }
    
    public void setZoneID(int id) {
        zoneID = id;
    }

    public int road() {
        return road;
    }

    public void setRoad(int value) {
        road = value;
    }

    public GreenfootImage image() {
        return ImageCache.get(type);
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

