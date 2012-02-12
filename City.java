import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * City
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * 'City' acts as a container for the map
 *
 */

public class City extends World
{

    private Map map;	// Map of the city

    public City() {

        super(1024, 768, 1, false);		// Create a 1024 x 768 'World' with a cell size of 1px that does not restrict actors to the world boundary
        
		// Create and add a new map for the city
        map = new Map();
        addObject(map, 640, 512);	// Arbitrary values to place map at origin (top-left corner)
    }

}
