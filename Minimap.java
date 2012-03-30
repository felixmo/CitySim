import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.ArrayList;

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

    private final Rectangle FRAME = new Rectangle(new Point(112, 658), 200, 200);
    private final int tileSize = 2; // px
    
    private GreenfootImage image;
    private DataSource dataSource;
    private Point viewportOrigin = new Point(0, 0);

    public Minimap(DataSource dataSource) {

        this.dataSource = dataSource;

        this.image = new GreenfootImage(FRAME.width(), FRAME.height());
        this.image.setTransparency(150);
        setImage(this.image);

        draw();
    }

    public void act() 
    {
        // Do nothing
    }    

    private void draw() {
        
        // Clear minimap
        image.clear();

        // Draw minimap

        // Get all tiles
        ArrayList<ArrayList<Tile>> map = dataSource.tiles();

        // Determine the size of the map
        LinkedHashMap mapSize = dataSource.mapSize();
        int cityColumns = (Integer)mapSize.get("columns");
        int cityRows = (Integer)mapSize.get("rows");

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

    // ACCESSORS

    public Rectangle frame() {
        return FRAME;
    }

    // HELPERS

    // Returns the color that represents each type of tile
    private Color colorForTileOfType(int type) {
        switch (type) {
            case Tile.EMPTY: return Color.BLACK;
            case Tile.GRASS: return new Color(76, 114, 62);
            case Tile.SAND: return new Color(199, 188, 146);
            case Tile.DIRT: return new Color(112, 73, 54);
            case Tile.STONE: return new Color(164, 155, 155);

            default: return Color.BLACK;
        }
    }
}
