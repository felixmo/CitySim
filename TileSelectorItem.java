import greenfoot.*;
import java.awt.Point;
import java.awt.Color;

/**
 * Write a description of class TileSelectorItem here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelectorItem extends Actor
{

    private int type = 0;
    private int index = 0;
    private Point origin;

    public TileSelectorItem(int type, int index, Point origin) {

        this.type = type;
        this.index = index;
        this.origin = origin;

        setImage(Tile.imageFromCacheForType(this.type));
    }

    public void act() {

        if (Greenfoot.mouseClicked(this)) {

            Map.getInstance().selection().setSelectionMode(true);
            Map.getInstance().selection().setAcceptedTypes( new int[]{ Tile.GROUND } );
            Map.getInstance().selection().setSize(Street.SIZE);
            Road.setActiveType(this.type);
        }
    }
    
    /*
     * ACCESSORS *
     */
    public Point origin() {
        return this.origin;
    }
}
