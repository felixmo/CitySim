import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * Minimap
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 03-06-2012
 *
 * Minimap view for HUD
 *
 */

public class Minimap extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * MINIMAP PROPERTIES *
     */
    private final Rectangle FRAME = new Rectangle(112, 658, 200, 200);
    private final int tileSize = 1; // px

    /*
     * INSTANCE VARIABLES
     */

    private GreenfootImage image;                       // Minimap view
    private Point viewportOrigin = new Point(0, 0);     // Viewport origin

    // ---------------------------------------------------------------------------------------------------------------------

    public Minimap() {

        this.image = new GreenfootImage(FRAME.width, FRAME.height);
        this.image.setTransparency(150);
        setImage(this.image);

        draw();
    }

    public void act() 
    {
        // Do nothing
    }    

    // Draws the minimap
    private void draw() {

        // Clear minimap
        image.clear();

        // Draw minimap

        // Get all tiles
        ArrayList<ArrayList<Tile>> map = Data.tiles();

        // Determine the size of the map
        LinkedHashMap mapSize = Data.mapSize();
        int cityColumns = (Integer)mapSize.get(Data.MAPSIZE_COLUMNS);
        int cityRows = (Integer)mapSize.get(Data.MAPSIZE_ROWS);

        // Position of the minimap tile being drawn
        int x = 0;
        int y = 0;

        // Iterate through every map tile and draw it onto minimap, adjusting the position for the next tile with each iteration
        // Minimap is drawn column by column
        for (int i = 0; i < cityColumns; i++) {
            for (int j = 0; j < cityRows; j++) {
                image.setColor(colorForTileOfType(((Tile)map.get(i).get(j)).type()));
                image.fillRect(x, y, tileSize, tileSize); // Minimap tiles are 2px * 2px
                y+=tileSize;
            }
            // Reset Y to top of the column 
            y = 0;

            x+=tileSize;
        }
    }

    /*
     * ACCESSORS *
     */

    public Rectangle frame() {
        return FRAME;
    }

    /*
     * HELPERS *
     */

    // Returns the color that represents each type of tile
    private Color colorForTileOfType(int type) {
        switch (type) {
            case Tile.EMPTY: return Color.BLACK;
            case Tile.WATER: return Color.BLUE;
            case Tile.GROUND: return new Color(216, 146, 125);
            case Tile.GRASS_1: return new Color(76, 114, 62);
            case Tile.GRASS_2: return new Color(76, 114, 62);
            case Tile.GRASS_3: return new Color(76, 114, 62);
            case Tile.GRASS_4: return new Color(76, 114, 62);
            case Tile.GRASS_5: return new Color(76, 114, 62);

            default: return Color.BLACK;
        }
    }
}
