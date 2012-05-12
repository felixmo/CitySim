import greenfoot.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.Integer;

/**
 * Write a description of class Selection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Selection extends Actor
{

    //     private Tile initalTile;
    //     private Tile endTile;
    private Tile activeTile;
    private Rectangle viewport;
    private boolean active = true;
    private boolean selectionMode = false;
    private ArrayList<Integer> acceptedTypes = new ArrayList<Integer>();          // accepted types of tiles for selection
    private Dimension size = new Dimension(1, 1);

    private GreenfootImage image;

    public Selection(int width, int height) {

        this.image = new GreenfootImage(width, height);
        //         this.image.setTransparency(255);
        setImage(this.image);
    }

    private void draw() {

        if (activeTile == null || viewport == null || !active) return;

        this.image.setTransparency(255);

        //         if (!selectionMode) {

        // HOVER

        this.image.clear();
        if (selectionMode && !selectionIsValid()) {
            this.image.setTransparency(100);
            this.image.setColor(Color.RED);
            this.image.fillRect((((activeTile.position().x) * Tile.SIZE) - viewport.x), (((activeTile.position().y-2) * Tile.SIZE) - viewport.y), (int)size.getWidth()*Tile.SIZE, (int)size.getHeight()*Tile.SIZE);
        }
        this.image.setColor(Color.WHITE);
        this.image.drawRect((((activeTile.position().x) * Tile.SIZE) - viewport.x), (((activeTile.position().y-2) * Tile.SIZE) - viewport.y), (int)size.getWidth()*Tile.SIZE, (int)size.getHeight()*Tile.SIZE);
        //         }
        //         else {
        // 
        //             // SELECTION
        // 
        //             if (initalTile == null || endTile == null) return;
        // 
        //             this.image.clear();
        //             if (!selectionIsValid()) {
        //                 this.image.setTransparency(100);
        //                 this.image.setColor(Color.RED);
        //                 this.image.fillRect(((initalTile.position().x * Tile.size) - viewport.x), (((initalTile.position().y-2) * Tile.size) - viewport.y), (Math.abs(initalTile.position().x - endTile.position().x) * Tile.size) + Tile.size, (Math.abs((initalTile.position().y-2) - (endTile.position().y-2)) * Tile.size) + Tile.size);
        //             }
        //             this.image.setColor(Color.WHITE);
        //             this.image.drawRect(((initalTile.position().x * Tile.size) - viewport.x), (((initalTile.position().y-2) * Tile.size) - viewport.y), (Math.abs(initalTile.position().x - endTile.position().x) * Tile.size) + Tile.size, (Math.abs((initalTile.position().y-2) - (endTile.position().y-2)) * Tile.size) + Tile.size);
        //         }
    }

    public void act() {

        if (Greenfoot.mouseClicked(this)) {
            // FOR TESTING
            /*
            String type = "";
            switch (activeTile.type()) {
            case Tile.EMPTY: type = "Empty";
            break;
            case Tile.GROUND: type = "Ground";
            break;
            case Tile.WATER: type = "Water";
            break;
            default: break;
            }
            System.out.println(activeTile.dbID() + ": " + type);
             */
            if (selectionIsValid()) {
                if (Zone.pendingOp() > 0) CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED_FOR_ZONING, selectedTiles()));
            }
        }
        //         if (!selectionMode) return;
        //         this.image.clear(); // Clear previous selection
        //         if (Greenfoot.mousePressed(this)) {
        //             this.endTile = null; // Clear previous selection
        //             this.initalTile = this.activeTile;
        //         }

        //         if (Greenfoot.mouseDragged(this)) {
        //             this.endTile = this.activeTile;
        //         }       

        //         if (Greenfoot.mouseDragEnded(this)) {
        //             // SEND MSG
        //             if (selectionIsValid()) {
        //                 CSEventBus.post(new SelectionEvent(SelectionEvent.TILES_SELECTED, selectedTiles()));
        //             }
        // 
        //             // Clear selection
        //             this.initalTile = null;
        //             this.endTile = null;
        // 
        //             //             setSelectionMode(false);
        //         }
        // 
        if (Greenfoot.isKeyDown("escape")) {
            // Clear selection
//             this.initalTile = null;
//             this.endTile = null;

            setSelectionMode(false);

            Zone.setPendingOp(0);
        }

        draw();
    }

    public ArrayList<ArrayList<Tile>> selectedTiles() {

        ArrayList<ArrayList<Tile>> tiles = Data.tiles();

        ArrayList<ArrayList<Tile>> selectedTiles = new ArrayList<ArrayList<Tile>>((int)size.getWidth());
        for (int i = 0; i < size.getWidth(); i++) {
            selectedTiles.add(new ArrayList<Tile>((int)size.getHeight()));
        }

        int i = 0;

        for (int x = activeTile.position().x; x < activeTile.position().x + (int)size.getWidth(); x++) {
            for (int y = activeTile.position().y; y < activeTile.position().y + (int)size.getHeight(); y++) {
                selectedTiles.get(i).add(tiles.get(x).get(y));
            }
            i++;
        }

        return selectedTiles;
    }

    private boolean selectionIsValid() {

        // Check type

        for (int x = activeTile.position().x; x < (activeTile.position().x + size.getWidth()); x++) {
            for (int y = activeTile.position().y; y < (activeTile.position().y + size.getHeight()); y++) {
                if (!acceptedTypes.contains(Data.tiles().get(x).get(y).type())) {
                    //                     System.out.println("Unacceptable type (" + Data.tiles().get(x).get(y).type() + ") found @ (" + x + ", " + y + ") | Active tile @ (" + activeTile.position().x + ", " + activeTile.position().y + ")");
                    return false;
                }
            }
        }

        return true;
    }

    /*
     * ACCESSORS *
     */

    public boolean active() {
        return this.active;
    }

    public void setActive(boolean value) {
        this.active = value;
    }

    public boolean selectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(boolean value) {

        CSLogger.sharedLogger().finer("Setting selection mode: " + (value ? "ON" : "OFF"));

        this.active = !value;
        this.selectionMode = value;
    }

    public Tile activeTile() {
        return this.activeTile;
    }

    public void setActiveTile(Tile tile) {

        if (this.activeTile == tile) return;

        this.activeTile = tile;
        draw();
    }

    /*
    public Tile endTile() {
    return this.endTile;
    }

    public void setEndTile(Tile tile) {

    this.endTile = tile;
    }
     */
    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
        draw();
    }

    public void setAcceptedTypes(int[] types) {
        this.acceptedTypes.clear();
        for (int type : types) {
            this.acceptedTypes.add(new Integer(type));
        }
    }

    public void setSize(int width, int height) {
        this.size.setSize(width, height);
    }

    public void setSize(Dimension size) {
        this.size = size;
    }
}
