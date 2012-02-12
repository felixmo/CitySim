import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Tile
 * v0.1
 *
 * Created by Felix Mo on 01-18-2012
 *
 */

public class Tile 
{

    private boolean traverseable = false;
    private int type = 0;
    
    private GreenfootImage image = null;
    
    public static final int size = 128; // px
    
    // TILE TYPES
    public static final int TILE_TYPE_GRASS = 1;
    public static final int TILE_TYPE_SAND = 2;
    public static final int TILE_TYPE_DIRT = 3;
    public static final int TILE_TYPE_STONE = 4;
    
    // FILES
    private static final String TILE_IMG_GRASS = "tile_grass2.png";
    private static final String TILE_IMG_SAND = "tile_sand2.png";
    private static final String TILE_IMG_DIRT = "tile_dirt1.png";
    private static final String TILE_IMG_STONE = "tile_stone1.png";
   
    public Tile(int type) {
        this.type = type;
    }
    
    // GETTERS
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
    
    // SETTERS
    public void setType(int value) {
        type = value;
    }
    
    public void setTraverseable(boolean value) {
        traverseable = value;
    }
    
    private GreenfootImage imageForType() {
        
        switch (type) {
            
            case TILE_TYPE_GRASS: return new GreenfootImage(TILE_IMG_GRASS);
            case TILE_TYPE_SAND: return new GreenfootImage(TILE_IMG_SAND);
            case TILE_TYPE_DIRT: return new GreenfootImage(TILE_IMG_DIRT);
            case TILE_TYPE_STONE: return new GreenfootImage(TILE_IMG_STONE);
            
            default: return null;
        }
    }
}

