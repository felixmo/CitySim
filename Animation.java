import greenfoot.*;
import java.util.HashMap;
import java.awt.Rectangle;
import java.util.Iterator;

/**
 * Write a description of class Animation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Animation extends Actor
{

    private static Animation instance;

    //     public static final String NOPOWER = "NOPOWER";
    public static final GreenfootImage NOPOWER_IMG = new GreenfootImage("images/tiles/828.png");

    //     public static final String TRAFFIC = "TRAFFIC";

    private Zone[] zones;
    private Tile[] roads;
    private Rectangle viewport;
    private GreenfootImage image;
    private boolean needsUpdate = true;

    public Animation(int width, int height) {

        Animation.instance = this;

        this.image = new GreenfootImage(width, height);
        setImage(this.image);
    }

    public void act() {

        if (needsUpdate) {
            draw();
        }
    }

    private void draw() {

        this.image.clear();

        if (this.viewport == null || zones == null) return;

        for (Zone zone : zones) {

            // Zones only have ONE animation currently so no need to check the type

            int diff = 1;
            if (zone.zone() == Stadium.TYPE_ID) {
                diff = 2;
            }
            this.image.drawImage(Animation.NOPOWER_IMG, (((zone.origin().x + diff) * Tile.SIZE) - viewport.x),  (((zone.origin().y - diff) * Tile.SIZE) - viewport.y));
        }

        /*
        for (Iterator it = tiles.keySet().iterator(); it.hasNext();) {
        Tile tile = (Tile)it.next();
        this.image.drawImage(tile.nextFrame(), ((tile.position().x * Tile.SIZE) - viewport.x), ((tile.position().y * Tile.SIZE) - viewport.y));
        }
         */

        needsUpdate = false;
    }

    //     public void roadNeedsAnimationOfType(Tile tile, String type) {
    //         this.roads.put(tile, type);    
    //     }

    /*
     * ACCESSORS *
     */

    public static Animation getInstance() {
        return instance;
    }

    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
        draw();
    }

    public void setZones(Zone[] zones) {

        if (zones == null) {
            this.zones = null;
        }
        else {
            this.zones = zones;
        }
        this.needsUpdate = true;
    }
}
