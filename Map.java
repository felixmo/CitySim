/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;

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

    private static Map instance;

    // Map properties
    public final int SIZE_COLUMNS = 200;    // cells; horizontal
    public final int SIZE_ROWS = 200;   // cells; vertical
    private Rectangle cityRect = new Rectangle(0, 0, (SIZE_COLUMNS * Tile.SIZE), (SIZE_ROWS * Tile.SIZE));     // rectangle representing the entirety of the map

    // View configuration
    private final int moveSpeed = 20;       // # of px to move at a time
    private final static int tileBuffer = 4;    // number of additional cells to draw beyond the viewport

    // Map view
    private Rectangle viewport = new Rectangle(0, 0, 1024 + (tileBuffer * Tile.SIZE), 768 + (tileBuffer * Tile.SIZE) - 230);   // rectangle representing the viewport | NOTE: remember to subtract HUD and other OSD elements from height/width
    private GreenfootImage view = new GreenfootImage(viewport.width, viewport.height);  // image layer containting all the visible map tiles; tiles are drawn onto this image instead of being drawn on-screen individually (too resource-intensive)

    // Ref. to mouse info. provided by Greenfoot
    private MouseInfo mouseInfo = null;

    private Selection selection = new Selection(viewport.width, viewport.height);
    private Animation animation = new Animation(viewport.width, viewport.height);

    private boolean shouldUpdate = false;

    // ---------------------------------------------------------------------------------------------------------------------

    public Map() {

        Map.instance = this;

        if (Data.dbIsNew()) {
            // New DB

            CSLogger.sharedLogger().info("New database created; generating map...");

            // Insert specified map properties into DB
            LinkedHashMap mapSize = new LinkedHashMap();
            mapSize.put(Data.MAPSIZE_ROWS, SIZE_ROWS);
            mapSize.put(Data.MAPSIZE_COLUMNS, SIZE_COLUMNS);
            Data.insertMapSize(mapSize);

            // Generate a new map (for testing), set the image representing this 'Actor' to be the map image, and then do the inital draw of map tiles from the origin
            Data.insertTiles(generateCity());
        }
        else {
            // Existing DB

            // Get map data from DB
            CSLogger.sharedLogger().info("Database exists; loading tiles...");
        }

        // Draw map on screen, starting at the origin (0, 0)
        setImage(view);
        Point viewportLoc = viewport.getLocation();
        viewportDidMove(viewportLoc.x, viewportLoc.y);
    }

    // * Greenfoot methods *

    protected void addedToWorld(World world) {
        this.selection.setViewport(this.viewport);
        world.addObject(this.selection, 512, 333);

        this.animation.setViewport(this.viewport);
        world.addObject(this.animation, 512, 333);
    }

    // This method is called at every action step in the environment; frequently
    public void act() 
    {   
        if (this.shouldUpdate) {
            draw();
            this.shouldUpdate = false;
        }

        // Listen for keystrokes and mouse movement and move accordingly, only if viewport is within bounds of map

        int offset_x = 0;
        int offset_y = 0;
        int mouse_x = 0;
        int mouse_y = 0;

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (mouseInfo != null) {

            mouse_x = mouseInfo.getX();
            mouse_y = mouseInfo.getY();

            Point cell = cellForCoordinatePairInView(mouse_x, mouse_y);
            this.selection.setActiveTile(tileForCoordinatePair(cell.x, cell.y));

            // If selection view is NOT in selection mode; deactivate the view when cursor is out of bounds
            if (mouse_y <= 28 || mouse_y >= 538 || mouse_x < 10 || mouse_x >= 1014) {
                // TOP
                if (!this.selection.selectionMode()) this.selection.setActive(false);
            }
            else {
                this.selection.setActive(true);
            }
        }

        // Vertical movement
        if (Greenfoot.isKeyDown("w")) {
            // UP
            if (viewport.y > cityRect.y) {
                offset_y = moveSpeed * -1;
            }
        }
        else if (Greenfoot.isKeyDown("s")) {
            // DOWN
            if (viewport.y + viewport.height < cityRect.width) {
                offset_y = moveSpeed;
            }
        }

        // Horizontal movement
        if (Greenfoot.isKeyDown("a")) {
            // LEFT
            if (viewport.x > cityRect.x) {
                offset_x = moveSpeed;
            }
        }
        else if (Greenfoot.isKeyDown("d")) {
            // RIGHT
            if (viewport.x + viewport.width < cityRect.width) {
                offset_x = moveSpeed * -1;
            }
        }

        // Only re-render map if there was movement
        if (offset_x != 0 || offset_y != 0) {
            viewportDidMove(offset_x, offset_y);
        }
    }    

    // * END of Greenfoot methods *

    // * Helper methods *
    // Translates a given pair of coordinates (for the view, in px) to indices for the representing map tile
    private Point cellForCoordinatePair(int x, int y) {
        return new Point((x / Tile.SIZE), (y / Tile.SIZE));
    }

    // Translate a given pair of coordinates (within the bounds of the view) to indices for the representing map tile 
    private Point cellForCoordinatePairInView(int x, int y) {
        return cellForCoordinatePair((viewport.x + x), (viewport.y + y));   
    }

    private Tile tileForCoordinatePair(int x, int y) {
        return (Tile)Data.tiles().get(Math.min(x+2, SIZE_COLUMNS-1)).get(Math.min(y+2, SIZE_ROWS-1));
    }

    // Translates a given indice to a coordinate for the view, in px
    private int coordinateForCell(int cell) {
        return (cell * Tile.SIZE);
    }

    // Returns the numbers of the tiles that would fit within a given width
    private int numberOfTilesInWidth(int width) {
        return (width / Tile.SIZE);
    }

    // * END of helper methods *

    // Generates a new map into an ArrayList a returns it
    private ArrayList<ArrayList<Tile>> generateCity() {

        CSLogger.sharedLogger().info("Began generating city...");
        long startTime = System.currentTimeMillis();

        int[][] tiles = new TerrainGenerator(null, 1.0f, SIZE_COLUMNS, SIZE_ROWS).tiles();

        // The columns of the map are represnted by the elements of an ArrayList
        ArrayList<ArrayList<Tile>> map = new ArrayList<ArrayList<Tile>>(SIZE_COLUMNS);

        // The rows of the map are represented by the elements of an ArrayList within the ArrayList representing the columns
        for (int i = 0; i < SIZE_COLUMNS; i++) {
            map.add(new ArrayList<Tile>(SIZE_ROWS));
        }

        // Initalize each cell with the default initial type of grass
        /*
         * Iterates each cell (top -> botton) from each column (left -> right)
         * 
         */
        int dbID = 0;
        for (int x = 0; x < SIZE_COLUMNS; x++) {
            for (int y = 0; y < SIZE_ROWS; y++) {

                map.get(x).add(new Tile(dbID, new Point(x, y), tiles[x][y]));
                dbID++;
                //                 System.out.println("(" + x + ", " + y + ")" + " | " + "Value: " + value + " | Tile: " + ((Tile)map.get(x).get(y)).type() + " | Value below: " + df.format(grid[x][Math.min(Math.max(0, y-1), SIZE_ROWS-1)]));
            }
        }

        CSLogger.sharedLogger().info("Finished generating city in " + (System.currentTimeMillis() - startTime) + " ms");

        return map;
    }

    // Re-renders the map image layer upon movement to account for the viewport's new offset
    private void viewportDidMove(int dx, int dy) {

        // Shift the viewport's origin from movement offset
        viewport.setLocation((viewport.x - dx), (viewport.y + dy));

        Point viewportLoc = viewport.getLocation();
        Point cell = cellForCoordinatePair(viewportLoc.x, viewportLoc.y);

        if (getWorld() != null) ((City)getWorld()).didMoveMapTo(cell.x, cell.y);

        this.selection.setViewport(this.viewport);
        this.animation.setViewport(this.viewport);

        draw();
    }

    public void viewportDidMoveTo(int x, int y) {

        // Shift the viewport's origin from movement offset
        viewport.setLocation(coordinateForCell(x), coordinateForCell(y));

        this.selection.setViewport(this.viewport);
        this.animation.setViewport(this.viewport);

        draw();
    }

    protected void draw() {

        // Get cached map data
        ArrayList<ArrayList<Tile>> map = Data.tiles();

        // Coordinates for the tile being drawn; set at the origin of the shifted viewport
        int tile_x = viewport.x;
        int tile_y = viewport.y;

        Point vpLoc = viewport.getLocation();                   // Viewport location
        Point cell = cellForCoordinatePair(vpLoc.x, vpLoc.y);   // Cell @ viewport origin

        // Clear the whole map image
        view.clear();

        // Draw tiles onto the map image layer for the shifted viewport
        for (int col = cell.x; col < numberOfTilesInWidth(viewport.width + viewport.x); col++) {
            for (int row = cell.y; row < numberOfTilesInWidth(viewport.height + viewport.y); row++) {

                //                 System.out.println(Math.min(col, SIZE_COLUMNS-1) + ", " + Math.min(row, SIZE_ROWS-1));

                // Get the tile (if it is within the bounds of the map)
                Tile tile = map.get(Math.min(col, SIZE_COLUMNS-1)).get(Math.min(row, SIZE_ROWS-1));

                // Draw the tile image onto the whole map image
                view.drawImage(tile.image(), tile_x, tile_y-Tile.SIZE);

                // Update the coordinates for the next tile to be drawn
                tile_x = coordinateForCell(col) - viewport.x;
                tile_y = coordinateForCell(row) - viewport.y;
            }
        }
    }

    /*
     * ACCESSORS *
     */

    public static Map getInstance() {
        return instance;
    }

    public Selection selection() {
        return this.selection;
    }

    public void setShouldUpdate(boolean value) {
        this.shouldUpdate = value;
    }
}
