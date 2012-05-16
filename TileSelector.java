import greenfoot.*;
import java.awt.Point;

/**
 * Write a description of class Selector here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelector extends Actor
{
    public static final int ORIGIN_X = 512;
    public static final int ORIGIN_Y = 30;

    private TileSelectorItem[] items;

    public TileSelector(int[] tiles) {

        int space = (1024 - (tiles.length+1 * Tile.SIZE)) / tiles.length+1;

        this.items = new TileSelectorItem[tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            this.items[i] = new TileSelectorItem(tiles[i], i, new Point(space*(i+1)-Tile.SIZE, ORIGIN_Y));

            TileSelectorItem item = this.items[i];
            City.getInstance().addObject(item, item.origin().x, item.origin().y);
        }

        setImage(new GreenfootImage("images/notif.png"));
    }

    public void act() {

        if (Greenfoot.isKeyDown("escape")) {
            City.getInstance().removeTileSelector();
            return;
        }
    }

    /*
     * ACCESSORS *
     */
    public TileSelectorItem[] items() {
        return this.items;
    }
}
