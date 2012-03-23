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
    private static Hashtable<Integer, GreenfootImage> cache = new Hashtable();
    
    public static void insertImageWithID (GreenfootImage image, int id) {
        cache.put((Integer)id, image);
    }
    
    public static GreenfootImage imageForID(int id) {
        return cache.get((Integer)id);
    }
    
    public static boolean containsImageWithID(int id) {
        return cache.containsKey((Integer)id);
    }
}
