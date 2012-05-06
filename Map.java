import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.awt.Rectangle;
import java.awt.Point;

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

    // Map properties
    private final int cityColumns = 200;    // cells; horizontal
    private final int cityRows = 200;   // cells; vertical
    private Rectangle cityRect = new Rectangle(0, 0, (cityColumns * Tile.size), (cityRows * Tile.size));     // rectangle representing the entirety of the map

    private ArrayList<ArrayList<Tile>> map;     // Holds map data

    // View configuration
    private final int moveSpeed = 20;       // # of px to move at a time
    private final static int tileBuffer = 4;    // number of additional cells to draw beyond the viewport

    // Map view
    private Rectangle viewport = new Rectangle(0, 0, 1024 + (tileBuffer * Tile.size), 768 + (tileBuffer * Tile.size) - 230);   // rectangle representing the viewport | NOTE: remember to subtract HUD and other OSD elements from height/width
    private GreenfootImage view = new GreenfootImage(viewport.width, viewport.height);  // image layer containting all the visible map tiles; tiles are drawn onto this image instead of being drawn on-screen individually (too resource-intensive)

    // Ref. to mouse info. provided by Greenfoot
    private MouseInfo mouseInfo = null;

    private Selection selection = new Selection(new Point(viewport.width, viewport.height));

    // ---------------------------------------------------------------------------------------------------------------------

    public Map() {

        if (Data.dbIsNew()) {
            // New DB

            CSLogger.sharedLogger().info("New database created; generating map...");

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
            CSLogger.sharedLogger().info("Database exists; loading tiles...");
            map = Data.tiles();
        }

        // Draw map on screen
        setImage(view);
        viewportDidMove(viewport.getLocation());
    }

    // * Greenfoot methods *

    protected void addedToWorld(World world) {
        this.selection.setViewport(this.viewport);
        world.addObject(this.selection, 512, 333);
    }

    // This method is called at every action step in the environment; frequently
    public void act() 
    {   

        // Listen for keystrokes and mouse movement and move accordingly, only if viewport is within bounds of map

        Point offset = new Point(0, 0); // movement offset

        Point mouse = null; // cursor position

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (mouseInfo != null) {

            mouse = new Point(mouseInfo.getX(), mouseInfo.getY());

            this.selection.setSelectedTile(tileForCoordinatePair(cellForCoordinatePairInView(mouse)));

            // FOR TESTING
            if (Greenfoot.mouseClicked(this)) {

                System.out.println(cellForCoordinatePairInView(mouse).toString());
            }

            // Vertical movement
            if (mouse.y <= 28 || mouse.y >= 538 || mouse.x < 10 || mouse.x >= 1014) {
                // TOP
                this.selection.setActive(false);
            }
            else {
                this.selection.setActive(true);
            }
        }

        // Only get keyboard input if there was no moues input
        if (offset.x == 0 && offset.y == 0) {

            // Vertical movement
            if (Greenfoot.isKeyDown("w")) {
                // UP
                if (viewport.y > cityRect.y) {
                    offset.y = moveSpeed * -1;
                }
            }
            else if (Greenfoot.isKeyDown("s")) {
                // DOWN
                if (viewport.y + viewport.height < cityRect.width) {
                    offset.y = moveSpeed;
                }
            }

            // Horizontal movement
            if (Greenfoot.isKeyDown("a")) {
                // LEFT
                if (viewport.x > cityRect.x) {
                    offset.x = moveSpeed;
                }
            }
            else if (Greenfoot.isKeyDown("d")) {
                // RIGHT
                if (viewport.x + viewport.width < cityRect.width) {
                    offset.x = moveSpeed * -1;
                }
            }
        }

        // Only re-render map if there was movement
        if (offset.x != 0 || offset.y != 0) {
            viewportDidMove(offset);
        }
    }    

    // * END of Greenfoot methods *

    // * Helper methods *
    // Translates a given pair of coordinates (for the view, in px) to indices for the representing map tile
    private Point cellForCoordinatePair(Point coord) {
        return new Point((coord.x / Tile.size), (coord.y / Tile.size));
    }

    // Translate a given pair of coordinates (within the bounds of the view) to indices for the representing map tile 
    private Point cellForCoordinatePairInView(Point coord) {
        return cellForCoordinatePair(new Point((viewport.x + coord.x), (viewport.y + coord.y)));   
    }

    private Tile tileForCoordinatePair(Point coord) {
        return (Tile)map.get(Math.min(coord.x+2, cityColumns-1)).get(Math.min(coord.y, cityRows-1));
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

        CSLogger.sharedLogger().info("Began generating city...");
        long startTime = System.currentTimeMillis();

        int[][] tiles = new TerrainGenerator(null, 1.0f, cityColumns, cityRows).tiles();

        // The columns of the map are represnted by the elements of an ArrayList
        ArrayList<ArrayList<Tile>> map = new ArrayList<ArrayList<Tile>>(cityColumns);

        // The rows of the map are represented by the elements of an ArrayList within the ArrayList representing the columns
        for (int i = 0; i < cityColumns; i++) {
            map.add(new ArrayList<Tile>(cityRows));
        }

        // Initalize each cell with the default initial type of grass
        /*
         * Iterates each cell (top -> botton) from each column (left -> right)
         * 
         */
        for (int x = 0; x < cityColumns; x++) {
            for (int y = 0; y < cityRows; y++) {

                map.get(x).add(new Tile(new Point(x, y), tiles[x][y]));
                //                 System.out.println("(" + x + ", " + y + ")" + " | " + "Value: " + value + " | Tile: " + ((Tile)map.get(x).get(y)).type() + " | Value below: " + df.format(grid[x][Math.min(Math.max(0, y-1), cityRows-1)]));
            }
        }

        CSLogger.sharedLogger().info("Finished generating city in " + (System.currentTimeMillis() - startTime) + " ms");

        return map;
    }

    // Re-renders the map image layer upon movement to account for the viewport's new offset
    private void viewportDidMove(Point offset) {

        // Shift the viewport's origin from movement offset
        viewport.setLocation((viewport.x - offset.x), (viewport.y + offset.y));

        Point cell = cellForCoordinatePair(viewport.getLocation());

        if (getWorld() != null) ((City)getWorld()).didMoveMapTo(new Point(cellForCoordinatePair(viewport.getLocation()).x, cellForCoordinatePair(viewport.getLocation()).y));

        this.selection.setViewport(this.viewport);

        draw();
    }

    public void viewportDidMoveTo(Point location) {

        // Shift the viewport's origin from movement offset
        viewport.setLocation(coordinateForCell(location.x), coordinateForCell(location.y));

        this.selection.setViewport(this.viewport);

        draw();
    }

    private void draw() {

        Point tilePt = new Point(viewport.x, viewport.y);     // Coordinate pair for the tile being drawn; set at the origin of the shifted viewport

        // Clear the map image layer
        view.clear();

        // Draw tiles onto the map image layer for the shifted viewport
        for (int col = cellForCoordinatePair(viewport.getLocation()).x; col < numberOfTilesInWidth(viewport.width + viewport.x); col++) {
            for (int row = cellForCoordinatePair(viewport.getLocation()).y; row < numberOfTilesInWidth(viewport.height + viewport.y); row++) {

                view.drawImage(map.get(Math.min(col, cityColumns-1)).get(Math.min(row, cityRows-1)).image(), tilePt.x, tilePt.y);  

                // Update the coordinates for the next tile to be drawn
                tilePt.setLocation((coordinateForCell(col) - viewport.x), (coordinateForCell(row) - viewport.y));
            }
        }
    }
}
