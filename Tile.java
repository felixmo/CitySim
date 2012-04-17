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

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */
    
    private Point position;
    private static int prevType = 1; // prevent crowding of same grass tiles when map is generated
    private int type = 0;   // Type of tile
    private boolean traverseable = false;   // Is tile passable
    private static int prevDbID = -1; // start at -1 so that the first tile will have an ID of 0
    private int dbID; // SQL db id

    /*
     * CONSTANTS *
     */
    
    public static final int size = 64;     // size of square tile; px

    /*
     * TILE TYPES *
     */
    
    public static final int EMPTY = 0;
    public static final int GRASS_1 = 10;
    public static final int GRASS_2 = 11;
    public static final int GRASS_3 = 12;
    public static final int GRASS_4 = 13;
    public static final int SAND = 20;
    public static final int DIRT = 30;
    public static final int STONE = 40;

    /*
     * IMAGE FILES *
     */
    
    private static final String IMG_EMPTY = "tiles/black.png";
    private static final String IMG_GRASS_1 = "tiles/grass_1.png";
    private static final String IMG_GRASS_2 = "tiles/grass_2.png";
    private static final String IMG_GRASS_3 = "tiles/grass_3.png";
    private static final String IMG_GRASS_4 = "tiles/grass_4.png";
    private static final String IMG_SAND = "tiles/tile_sand2.png";
    private static final String IMG_DIRT = "tiles/tile_dirt1.png";
    private static final String IMG_STONE = "tiles/tile_stone1.png";

    // ---------------------------------------------------------------------------------------------------------------------

    public Tile(Point position, int type, boolean traverseable) {
        this.position = position;
        this.prevType = type;
        this.type = type;
        this.traverseable = traverseable;
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

    /*
     * HELPERS *
     */

    // Sets the image of the tile based upon its type
    private GreenfootImage imageForType() {

        //         System.out.println("Initalizing new image for tile");

        switch (type) {

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

    }
}

