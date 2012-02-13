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
	
    private boolean traverseable = false;	// Is tile passable
    private int type = 0;	// Type of tile
    
    private GreenfootImage image = null;	// Tile image
    
    public static final int size = 128;		// size of square tile; px

    // TILE TYPES
    public static final int TILE_TYPE_BLACK = 0;
    public static final int TILE_TYPE_GRASS = 1;
    public static final int TILE_TYPE_SAND = 2;
    public static final int TILE_TYPE_DIRT = 3;
    public static final int TILE_TYPE_STONE = 4;
    
    // FILES
    private static final String TILE_IMG_BLACK = "tile_black.png";
    private static final String TILE_IMG_GRASS = "tile_grass2.png";
    private static final String TILE_IMG_SAND = "tile_sand2.png";
    private static final String TILE_IMG_DIRT = "tile_dirt1.png";
    private static final String TILE_IMG_STONE = "tile_stone1.png";
   
    
	// * END of constants and class & instance variables *


    public Tile(int type) {
        this.type = type;
    }
    

    // * Accessors * 
    public int type() {
        return type;
    }
    
    public boolean traverseable() {
        return traverseable;
    }
    
    public GreenfootImage image() {

        // Only initalize image if needed to conserve memory usage
        if (image != null) return image;
        image = imageForType();
        return image;
    }
    
    public void setType(int value) {
        type = value;
    }
    
    public void setTraverseable(boolean value) {
        traverseable = value;
    }

	// * END of accessors *
    
	
	// Sets the image of the tile based upon its type
    private GreenfootImage imageForType() {
        
        switch (type) {
            
            case TILE_TYPE_BLACK: return new GreenfootImage(TILE_IMG_BLACK);
            case TILE_TYPE_GRASS: return new GreenfootImage(TILE_IMG_GRASS);
            case TILE_TYPE_SAND: return new GreenfootImage(TILE_IMG_SAND);
            case TILE_TYPE_DIRT: return new GreenfootImage(TILE_IMG_DIRT);
            case TILE_TYPE_STONE: return new GreenfootImage(TILE_IMG_STONE);
            
            default: return null;
        }
    }
}

