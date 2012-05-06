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
    private static int prevType = 1; // prevent crowding of same grass tiles when map is generated
    private Integer type = 0;   // Type of tile
    private static int prevDbID = -1; // start at -1 so that the first tile will have an ID of 0
    private int dbID; // SQL db id

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
    public static final int BEACH_TOP = 201;
    public static final int BEACH_TOP_LEFT = 202;
    public static final int BEACH_TOP_RIGHT = 203;
    public static final int BEACH_BOTTOM = 204;
    public static final int BEACH_BOTTOM_LEFT = 205;
    public static final int BEACH_BOTTOM_RIGHT = 206;
    public static final int BEACH_LEFT = 207;
    public static final int BEACH_RIGHT = 208;
    public static final int GRASS_1 = 301;
    public static final int GRASS_2 = 302;
    public static final int GRASS_3 = 303;
    public static final int GRASS_4 = 304;
    public static final int GRASS_5 = 305;
    public static final int GRASS_TOP_LEFT = 306;
    public static final int GRASS_TOP = 307;
    public static final int GRASS_TOP_RIGHT = 308;
    public static final int GRASS_LEFT = 309;

    /*
     * IMAGE FILES *
     */

    // ---------------------------------------------------------------------------------------------------------------------

    public Tile(Point position, int type) {
        this.position = position;
        this.prevType = type;
        this.type = new Integer(type);
        this.dbID = prevDbID += 1;
    }

    /*
     * ACCESSORS *
     */

    public Point position() {
        return position;
    }

    public static int prevType() {
        return prevType;
    }

    public Integer type() {
        return type;
    }

    public void setType(Integer value) {
        type = value;
    }

    public int dbID() {
        return dbID;
    }

    public void setDbID(int value) {
        dbID = value;
    }

    public GreenfootImage image() {
        return ImageCache.get(type);
    }

    /*
     * HELPERS *
     */

    public static GreenfootImage imageForType(Integer type) {

        //         System.out.println("Initalizing new image for tile");

        return new GreenfootImage("images/tiles/" + type + ".png");
        /*
        switch (type.intValue()) {

        case EMPTY: return new GreenfootImage(IMG_EMPTY);

        // Grass
        case GRASS_1 : return new GreenfootImage(IMG_GRASS_1);
        case GRASS_2 : return new GreenfootImage(IMG_GRASS_2);
        case GRASS_3 : return new GreenfootImage(IMG_GRASS_3);
        case GRASS_4 : return new GreenfootImage(IMG_GRASS_4);

        // Unimplemented
        case SAND: return new GreenfootImage(IMG_SAND);
        case DIRT: return new GreenfootImage(IMG_DIRT);
        case STONE: return new GreenfootImage(IMG_STONE);

        default: return null;
        }
         */
    }
}

