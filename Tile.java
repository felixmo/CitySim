import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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

    // * Constants and class & instance variables *
    private Point position;
    private int type = 0;   // Type of tile
    private boolean traverseable = false;   // Is tile passable
    private static int prevDbID = -1; // start at -1 so that the first tile will have an ID of 0
    private int dbID; // SQL db id

    public static final int size = 64;     // size of square tile; px

    // TILE TYPES
    public static final int EMPTY = 0;
    public static final int GRASS = 1;
    public static final int SAND = 2;
    public static final int DIRT = 3;
    public static final int STONE = 4;

    // FILES
    private static final String IMG_EMPTY = "tile_black.png";
    private static final String IMG_GRASS = "tile_grass1.png";
    private static final String IMG_SAND = "tile_sand2.png";
    private static final String IMG_DIRT = "tile_dirt1.png";
    private static final String IMG_STONE = "tile_stone1.png";

    // * END of constants and class & instance variables *

    public Tile(Point position, int type, boolean traverseable) {
        this.position = position;
        this.type = type;
        this.traverseable = traverseable;
        this.dbID = prevDbID += 1;
    }

    // * Accessors * 
    public Point position() {
        return position;
    }

    public int type() {
        return type;
    }

    public boolean traverseable() {
        return traverseable;
    }
    
    public int dbID() {
        return dbID;
    }

    public GreenfootImage image() {

        if (ImageCache.containsImageWithID(type)) return ImageCache.imageForID(type);
        
        GreenfootImage image = imageForType();
        ImageCache.insertImageWithID(image, type);
        return image;
    }

    public void setType(int value) {
        type = value;
    }

    public void setTraverseable(boolean value) {
        traverseable = value;
    }
    
    public void setDbID(int value) {
        dbID = value;
    }

    // * END of accessors *

    // Sets the image of the tile based upon its type
    private GreenfootImage imageForType() {
        
//         System.out.println("Initalizing new image for tile");
        
        switch (type) {

            case EMPTY: return new GreenfootImage("64_0.png");
            case GRASS: return new GreenfootImage("64.png");

            default: return new GreenfootImage("64_2.png");
        }
/*
        switch (type) {

            case EMPTY: return new GreenfootImage(IMG_EMPTY);
            case GRASS: return new GreenfootImage(IMG_GRASS);
            case SAND: return new GreenfootImage(IMG_SAND);
            case DIRT: return new GreenfootImage(IMG_DIRT);
            case STONE: return new GreenfootImage(IMG_STONE);

            default: return null;
        }
        */
    }
}

