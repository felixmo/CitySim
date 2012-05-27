/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.GreenfootImage;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

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

    private static LoadingCache<Integer, GreenfootImage> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<Integer, GreenfootImage>() {
                public GreenfootImage load(Integer key) throws Exception {
                    CSLogger.sharedLogger().info("Caching image (" + key.intValue() + ")");
                    return Tile.imageFromDiskForType(key);
                }
            }
        );

    // ---------------------------------------------------------------------------------------------------------------------

    public static GreenfootImage get(Integer key) {
        try {
            return cache.get(key);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
