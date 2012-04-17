import greenfoot.*;
import java.util.Hashtable;

/**
 * ImageCache
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * Provides caching for images; allowing for quick access and reduced memory consumption
 *
 */

public class ImageCache  
{
    // ---------------------------------------------------------------------------------------------------------------------
    
    /*
     * INSTANCE VARIABLES *
     */
    private static Hashtable<Integer, GreenfootImage> cache = new Hashtable();  // Image cache
    
    // ---------------------------------------------------------------------------------------------------------------------

    // Inserts an image into cache
    public static void insertImageWithID (GreenfootImage image, int id) {
        cache.put((Integer)id, image);
    }
    
    // Returns an cached image
    public static GreenfootImage imageForID(int id) {
        return cache.get((Integer)id);
    }

    // Checks if an image is in the cache
    public static boolean containsImageWithID(int id) {
        return cache.containsKey((Integer)id);
    }
}
