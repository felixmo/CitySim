import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.lang.Math;

/**
 * Map
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * Map view, map view controller, and map data model
 *
 */

public class Map extends Actor
{
	
	// * Constants and class & instance variables *

    // map of 100x100 tiles
    private final int cityColumns = 100;	// cells; horizontal
    private final int cityRows = 100;	// cells; vertical
    private Rectangle cityRect = new Rectangle(new Point(0, 0), (cityColumns * Tile.size), (cityRows * Tile.size));		// rectangle representing the entirety of the map

    private ArrayList<ArrayList<Tile>> map;		// holds map data

	// view settings
	private final int moveSpeed = 10;		// # of px to move at a time
    private final static int tileBuffer = 2;	// number of additional cells to draw beyond the viewport

	// view
    private Rectangle viewport = new Rectangle(new Point(0, 0), 1024 + (tileBuffer * Tile.size), 768 + (tileBuffer * Tile.size));	// rectangle representing the viewport
    private GreenfootImage view = new GreenfootImage(viewport.width(), viewport.height());	// image layer containting all the visible map tiles; tiles are drawn onto this image instead of being drawn on-screen individually (too resource-intensive)

	// * END of constants and class & instance variables *


    public Map() {
		
		// Generate a new map (for testing), set the image representing this 'Actor' to be the map image, and then do the inital draw of map tiles from the origin
        generateCity();
        setImage(view);
		viewportDidMove(viewport.origin());
    }


	// * Greenfoot methods *

	// This method is called at every action step in the environment; frequently
    public void act() 
    {	
		// Listen for keystrokes and move accordingly, only if viewport is within bounds of map
		 
        if (Greenfoot.isKeyDown("w")) {
			// UP
			
            if (viewport.origin().y() > cityRect.origin().y()) {
                viewportDidMove(new Point(0, (moveSpeed * -1)));
            }
        }
        else if (Greenfoot.isKeyDown("s")) {
            // DOWN
			
			if (viewport.origin().y() + viewport.height() < cityRect.width()) {
                viewportDidMove(new Point(0, moveSpeed));
            }
        }
        else if (Greenfoot.isKeyDown("a")) {
			// LEFT
		
            if (viewport.origin().x() > cityRect.origin().x()) {
                viewportDidMove(new Point(moveSpeed, 0));
            }
        }
        else if (Greenfoot.isKeyDown("d")) {
			// RIGHT
	
            if (viewport.origin().x() + viewport.width() < cityRect.width()) {
                viewportDidMove(new Point((moveSpeed * -1), 0));
            }
        }
    }    

	// * END of Greenfoot methods *


    // * Helper methods *

	// Translates a given pair of coordinates (for the view, in px) to indices for the representing map tile in the ArrayLists
    private Point cellForCoordinatePair(Point coord) {
        return new Point(((coord.x() - (coord.x() % Tile.size)) / Tile.size), ((coord.y() - (coord.y() % Tile.size)) / Tile.size));
    }

	// Translates a given indice to a coordinate for the view, in px
	private int coordinateForCell(int cell) {
		return (cell * Tile.size);
	}
	
	// Returns the numbers of the tiles that would fit within a given width
	private int numberOfTilesInWidth(int width) {
		return (width / Tile.size);
	}

    // * END of helper methods *
    

	// Generates a new map into the ivar 'map'
    private void generateCity() {

        // System.out.print("Began generating city...");
        // long startTime = System.currentTimeMillis();

        // The columns of the map are represnted by the elements of an ArrayList
        map = new ArrayList<ArrayList<Tile>>(cityColumns);

        // The rows of the map are represented by the elements of an ArrayList within the ArrayList representing the columns
        for (int i = 0; i < cityColumns; i++) {
            map.add(new ArrayList<Tile>(cityRows));
        }

		// Initalize each cell with a tile of randomly generated type
        for (int x = 0; x < cityColumns; x++) {
            for (int y = 0; y < cityRows; y++) {
                map.get(x).add(new Tile(Greenfoot.getRandomNumber(4)+1));
            }
        }

        // System.out.println("completed in " + (System.currentTimeMillis() - startTime) + " ms");
    }

	// Re-renders the map image layer upon movement to account for the viewport's new offset
    private void viewportDidMove(Point offset) {

		// Shift the viewport's origin from movement offset
        viewport.origin().setX(viewport.origin().x() - offset.x());
        viewport.origin().setY(viewport.origin().y() + offset.y());
		
		Point tilePt = new Point(viewport.origin().x(), viewport.origin().y());		// Coordinate pair for the tile being drawn; set at the origin of the shifted viewport
		
		// Clear the map image layer
        view.clear();

		// Draw tiles onto the map image layer for the shifted viewport
        for (int col = cellForCoordinatePair(viewport.origin()).x(); col < numberOfTilesInWidth(viewport.width() + viewport.origin().x()); col++) {
            for (int row = cellForCoordinatePair(viewport.origin()).y(); row < numberOfTilesInWidth(viewport.height() + viewport.origin().y())+1; row++) {

                view.drawImage(map.get(col).get(row).image(), tilePt.x(), tilePt.y());	
				
				// Update the coordinates for the next tile to be drawn
                tilePt.setX(coordinateForCell(col) - viewport.origin().x());
                tilePt.setY(coordinateForCell(row) - viewport.origin().y());
            }
        }
    }

}
