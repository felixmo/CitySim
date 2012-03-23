import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * Write a description of class Minimap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Minimap extends Actor
{

    private final Rectangle FRAME = new Rectangle(new Point(112, 658), 200, 200);

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
//         MouseInfo mouseInfo = Greenfoot.getMouseInfo();
        
//         if (mouseInfo != null && 

        if (Greenfoot.mouseClicked(this)) System.out.println("Clicked");
    }    

    private void draw() {

        image.clear();

        // Draw minimap

        ArrayList<ArrayList<Tile>> map = dataSource.tiles();

        LinkedHashMap mapSize = dataSource.mapSize();
        int cityColumns = (Integer)mapSize.get("columns");
        int cityRows = (Integer)mapSize.get("rows");

        int x = 0;
        int y = 0;
        for (int i = 0; i < cityColumns; i++) {
            for (int j = 0; j < cityRows; j++) {
                image.setColor(colorForTileOfType(((Tile)map.get(i).get(j)).type()));
                image.fillRect(x, y, 2, 2);
                y+=2;
            }
            y = 0;
            x+=2;
        }
    }

    // ACCESSORS

    public Rectangle frame() {
        return FRAME;
    }

    // HELPERS

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
