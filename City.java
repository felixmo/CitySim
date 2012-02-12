import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * SimWorld
 * v0.1
 *
 * Created by Felix Mo on 01-18-2012
 *
 */

public class City extends World
{

    private Map map;

    public City() {

        super(1024, 768, 1); 
        
        map = new Map();
        addObject(map, 640, 512); // arb. values to place map at origin (bottom-left)
    }

}
