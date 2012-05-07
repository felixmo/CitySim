import greenfoot.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * Write a description of class Selection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Selection extends Actor
{

    private Tile initalTile;
    private Tile endTile;
    private Tile activeTile;
    private Rectangle viewport;
    private boolean active = true;
    private boolean selectionMode = false;

    private GreenfootImage image;

    public Selection(Point viewportSize) {

        this.image = new GreenfootImage(viewportSize.x, viewportSize.y);
        //         this.image.setTransparency(255);
        setImage(this.image);
    }

    private void draw() {

        if (activeTile == null || viewport == null || !active) return;

        if (!selectionMode) {

            // HOVER

            this.image.clear();
            this.image.setColor(Color.WHITE);
            this.image.drawRect(((activeTile.position().x * Tile.size) - viewport.x), ((activeTile.position().y * Tile.size) - viewport.y), Tile.size, Tile.size);
        }
        else {

            // SELECTION

            if (initalTile == null || endTile == null) return;

            this.image.clear();
            this.image.setColor(Color.WHITE);
            this.image.drawRect(((initalTile.position().x * Tile.size) - viewport.x), ((initalTile.position().y * Tile.size) - viewport.y), (Math.abs(initalTile.position().x - endTile.position().x) * Tile.size) + Tile.size, (Math.abs(initalTile.position().y - endTile.position().y) * Tile.size) + Tile.size);
        }
    }

    public void act() {

        if (!selectionMode) return;

        this.image.clear(); // Clear previous selection

        if (Greenfoot.mousePressed(this)) {
            this.endTile = null; // Clear previous selection
            this.initalTile = this.activeTile;
        }

        if (Greenfoot.mouseDragged(this)) {
            this.endTile = this.activeTile;
        }       

        if (Greenfoot.mouseDragEnded(this)) {
            //             System.out.println("done");
        }

        if (Greenfoot.isKeyDown("escape")) {
            // Clear selection
            this.initalTile = null;
            this.endTile = null;
            
            setSelectionMode(false);
        }

        draw();
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

    public Tile endTile() {
        return this.endTile;
    }

    public void setEndTile(Tile tile) {

        this.endTile = tile;
    }

    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
        draw();
    }
}
