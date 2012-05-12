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

    private Point position;
    private int type = 0;   // Type of tile
    private int zone = 0;
    private int dbID = 0; // SQL db id

    /*
     * CONSTANTS *
     */

    public static final int size = 32;     // size of square tile; px

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

    /*
     * IMAGE FILES *
     */

    // ---------------------------------------------------------------------------------------------------------------------

    public Tile(int dbID, Point position, int type, int zone) {
        this.dbID = dbID;
        this.position = position;
        this.type = type;
        this.zone = zone;
    }

    /*
     * ACCESSORS *
     */

    public Point position() {
        return position;
    }

    public int type() {
        return type;
    }

    public void setType(int value) {
        type = value;

        // Update value in DB
        Data.updateTile(this);
    }

    public int zone() {
        return zone;
    }

    public void setZone(int value) {
        zone = value;

//         Data.updateTile(this);
    }

    public int dbID() {
        return dbID;
    }

    public GreenfootImage image() {
        return ImageCache.get(type);
    }

    /*
     * HELPERS *
     */

    public static GreenfootImage imageForType(Integer type) {

        return new GreenfootImage("images/tiles/" + type + ".png");
    }
}

