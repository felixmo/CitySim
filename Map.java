import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES & CONSTANTS *
     */

    // Shared logger
    private Logger logger = LogManager.getLogManager().getLogger("com.felixmo.CitySim.logger");

    // Map properties
    private final int cityColumns = 100;    // cells; horizontal
    private final int cityRows = 100;   // cells; vertical
    private Rectangle cityRect = new Rectangle(new Point(0, 0), (cityColumns * Tile.size), (cityRows * Tile.size));     // rectangle representing the entirety of the map

    private ArrayList<ArrayList<Tile>> map;     // Holds map data

    // View configuration
    private final int moveSpeed = 15;       // # of px to move at a time
    private final static int tileBuffer = 4;    // number of additional cells to draw beyond the viewport

    // Map view
    private Rectangle viewport = new Rectangle(new Point(0, 0), 1024 + (tileBuffer * Tile.size), 768 + (tileBuffer * Tile.size) - 230);   // rectangle representing the viewport | NOTE: remember to subtract HUD and other OSD elements from height/width
    private GreenfootImage view = new GreenfootImage(viewport.width(), viewport.height());  // image layer containting all the visible map tiles; tiles are drawn onto this image instead of being drawn on-screen individually (too resource-intensive)

    // Ref. to mouse info. provided by Greenfoot
    private MouseInfo mouseInfo = null;

    // ---------------------------------------------------------------------------------------------------------------------

    public Map() {

        if (Data.dbIsNew()) {
            // New DB

            logger.info("New database created; generating map...");

            // Insert specified map properties into DB
            LinkedHashMap mapSize = new LinkedHashMap();
            mapSize.put(Data.MAPSIZE_ROWS, cityRows);
            mapSize.put(Data.MAPSIZE_COLUMNS, cityColumns);
            Data.insertMapSize(mapSize);

            // Generate a new map (for testing), set the image representing this 'Actor' to be the map image, and then do the inital draw of map tiles from the origin
            map = generateCity();
            Data.insertTiles(map);
        }
        else {
            // Existing DB

            // Get map data from DB
            logger.info("Database exists; loading tiles...");
            map = Data.tiles();
        }

        // Draw map on screen
        setImage(view);
        viewportDidMove(viewport.origin());
    }

    // * Greenfoot methods *
    // This method is called at every action step in the environment; frequently
    public void act() 
    {   
        // Listen for keystrokes and mouse movement and move accordingly, only if viewport is within bounds of map

        Point offset = new Point(0, 0); // movement offset
        /*
        Point mouse = null; // cursor position

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
        mouseInfo = Greenfoot.getMouseInfo();
        }

        if (mouseInfo != null) {

        mouse = new Point(mouseInfo.getX(), mouseInfo.getY());

        // Vertical movement
        if (mouse.y() >= 0 && mouse.y() <= 30) {
        // TOP
        if (viewport.origin().y() > cityRect.origin().y()) {
        offset.setY(moveSpeed * -1);
        }
        }
        else if (mouse.y() <= 548 && mouse.y() >= 518) {
        // BOTTOM
        if (viewport.origin().y() + viewport.height() < cityRect.width()) {
        offset.setY(moveSpeed);
        }
        }

        // Horizontal movement
        if (mouse.x() >= 0 && mouse.x() < 30) {
        // LEFT
        if (viewport.origin().x() > cityRect.origin().x()) {
        offset.setX(moveSpeed);
        }
        }
        else if (mouse.x() <= 1024 && mouse.x() >= 994) {
        // RIGHT
        if (viewport.origin().x() + viewport.width() < cityRect.width()) {
        offset.setX(moveSpeed * -1);
        }
        }
        }
         */

        // Only get keyboard input if there was no moues input
        if (offset.x() == 0 && offset.y() == 0) {

            // Vertical movement
            if (Greenfoot.isKeyDown("w")) {
                // UP
                if (viewport.origin().y() > cityRect.origin().y()) {
                    offset.setY(moveSpeed * -1);
                }
            }
            else if (Greenfoot.isKeyDown("s")) {
                // DOWN
                if (viewport.origin().y() + viewport.height() < cityRect.width()) {
                    offset.setY(moveSpeed);
                }
            }

            // Horizontal movement
            if (Greenfoot.isKeyDown("a")) {
                // LEFT
                if (viewport.origin().x() > cityRect.origin().x()) {
                    offset.setX(moveSpeed);
                }
            }
            else if (Greenfoot.isKeyDown("d")) {
                // RIGHT
                if (viewport.origin().x() + viewport.width() < cityRect.width()) {
                    offset.setX(moveSpeed * -1);
                }
            }
        }

        // Only re-render map if there was movement
        if (offset.x() != 0 || offset.y() != 0) {
            viewportDidMove(offset);
        }
    }    

    // * END of Greenfoot methods *

    // * Helper methods *
    // Translates a given pair of coordinates (for the view, in px) to indices for the representing map tile in the ArrayLists
    private Point cellForCoordinatePair(Point coord) {
        //         return new Point(((coord.x() - (coord.x() % Tile.size)) / Tile.size), ((coord.y() - (coord.y() % Tile.size)) / Tile.size));
        return new Point((coord.x() / Tile.size), (coord.y() / Tile.size));
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

    // Generates a new map into an ArrayList a returns it
    private ArrayList<ArrayList<Tile>> generateCity() {

        logger.info("Began generating city...");
        // long startTime = System.currentTimeMillis();

        // The columns of the map are represnted by the elements of an ArrayList
        ArrayList<ArrayList<Tile>> map = new ArrayList<ArrayList<Tile>>(cityColumns);

        // The rows of the map are represented by the elements of an ArrayList within the ArrayList representing the columns
        for (int i = 0; i < cityColumns; i++) {
            map.add(new ArrayList<Tile>(cityRows));
        }

        // Initalize each cell with the default initial type of grass
        for (int x = 0; x < cityColumns; x++) {
            for (int y = 0; y < cityRows; y++) {
                if (x < 3 || y < 3 || x > cityColumns-4 || y > cityRows-4) {
                    // Set the outter tiles to be empty
                    map.get(x).add(new Tile(new Point(x, y), Tile.EMPTY, true));
                }
                else {

                    // Initally fill map with grass
                    int randomizer = Greenfoot.getRandomNumber(4)+10;
                    while (randomizer == Tile.prevType()) {
                        randomizer = Greenfoot.getRandomNumber(4)+10;
                    }

                    map.get(x).add(new Tile(new Point(x, y), randomizer, true));
                }
            }
        }

        // System.out.println("completed in " + (System.currentTimeMillis() - startTime) + " ms");
        logger.info("Finished generating city.");

        return map;
    }

    // Re-renders the map image layer upon movement to account for the viewport's new offset
    private void viewportDidMove(Point offset) {

        // Shift the viewport's origin from movement offset
        viewport.origin().setX(viewport.origin().x() - offset.x());
        viewport.origin().setY(viewport.origin().y() + offset.y());

        Point cell = cellForCoordinatePair(viewport.origin());

        if (getWorld() != null) ((City)getWorld()).didMoveMapTo(new Point(cellForCoordinatePair(viewport.origin()).x(), cellForCoordinatePair(viewport.origin()).y()));

        Point tilePt = new Point(viewport.origin().x(), viewport.origin().y());     // Coordinate pair for the tile being drawn; set at the origin of the shifted viewport

        // Clear the map image layer
        view.clear();

        // Draw tiles onto the map image layer for the shifted viewport
        for (int col = cellForCoordinatePair(viewport.origin()).x(); col < numberOfTilesInWidth(viewport.width() + viewport.origin().x()); col++) {
            for (int row = cellForCoordinatePair(viewport.origin()).y(); row < numberOfTilesInWidth(viewport.height() + viewport.origin().y()); row++) {

                view.drawImage(map.get(Math.min(col, cityColumns-1)).get(Math.min(row, cityRows-1)).image(), tilePt.x(), tilePt.y());  

                // Update the coordinates for the next tile to be drawn
                tilePt.setX(coordinateForCell(col) - viewport.origin().x());
                tilePt.setY(coordinateForCell(row) - viewport.origin().y());
            }
        }
    }

    public void viewportDidMoveTo(Point location) {

        // Shift the viewport's origin from movement offset
        viewport.origin().setX(coordinateForCell((location.x() / 2)));
        viewport.origin().setY(coordinateForCell((location.y() / 2)));

        Point tilePt = new Point(viewport.origin().x(), viewport.origin().y());     // Coordinate pair for the tile being drawn; set at the origin of the shifted viewport

        // Clear the map image layer
        view.clear();

        // Draw tiles onto the map image layer for the shifted viewport
        for (int col = cellForCoordinatePair(viewport.origin()).x(); col < numberOfTilesInWidth(viewport.width() + viewport.origin().x()); col++) {
            for (int row = cellForCoordinatePair(viewport.origin()).y(); row < numberOfTilesInWidth(viewport.height() + viewport.origin().y()); row++) {

                view.drawImage(map.get(Math.min(col, cityColumns-1)).get(Math.min(row, cityRows-1)).image(), tilePt.x(), tilePt.y());  

                // Update the coordinates for the next tile to be drawn
                tilePt.setX(coordinateForCell(col) - viewport.origin().x());
                tilePt.setY(coordinateForCell(row) - viewport.origin().y());
            }
        }
    }

}
