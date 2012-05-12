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
     * COLORS
     */
    private final Color GROUND = new Color(216, 146, 125);
    private final Color RES = new Color(0, 207, 0);
    
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
    public void draw() {

        // Clear minimap
        image.clear();

        // Draw minimap

        // Get all tiles
        ArrayList<ArrayList<Tile>> map = Data.tiles();

        // Position of the minimap tile being drawn
        int x = 0;
        int y = 0;

        // Iterate through every map tile and draw it onto minimap, adjusting the position for the next tile with each iteration
        // Minimap is drawn column by column
        for (int i = 0; i < Map.getInstance().SIZE_COLUMNS; i++) {
            for (int j = 0; j < Map.getInstance().SIZE_ROWS; j++) {
                
                Tile tile = (Tile)map.get(i).get(j);
                if (tile.zone() > 0) {
                    image.setColor(colorForTileOfZone(tile.zone()));
                }
                else {
                    image.setColor(colorForTileOfType(tile.type())); 
                }
                
//                 image.setColor(colorForTileOfType(((Tile)map.get(i).get(j)).type()));
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
            case Tile.GROUND: return GROUND;

            default: return Color.BLACK;
        }
    }
    
    private Color colorForTileOfZone(int zone) {
        switch (zone) {
            case ResidentialZone.ID: return RES;
            
            default: return Color.BLACK;
        }
    }
}
