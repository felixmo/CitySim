/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;

/**
 * Write a description of class SoundManager here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SoundManager  
{
    private static GreenfootSound activeSound = null;

    public static void playBackgroundMusic() {
        
        if (activeSound == null) {
            activeSound = new GreenfootSound("bkg_music/1.mp3");
        }
        
        activeSound.setVolume(100);
        activeSound.playLoop();
    }
    
    public static void pauseBackgroundMusic() {
        if (activeSound != null) activeSound.pause();
    }
}
