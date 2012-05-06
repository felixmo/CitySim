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

    private Tile selectedTile;
    private Rectangle viewport;
    private boolean active = true;
    
    private GreenfootImage image;

    public Selection(Point viewportSize) {
        this.image = new GreenfootImage(viewportSize.x, viewportSize.y);
        this.image.setTransparency(255);
        setImage(this.image);
    }

    private void draw() {
        
        if (selectedTile == null || viewport == null || !active) return;
        
        this.image.clear();
        this.image.setColor(Color.WHITE);
        this.image.drawRect(((selectedTile.position().x * Tile.size) - viewport.x), ((selectedTile.position().y * Tile.size) - viewport.y), Tile.size, Tile.size);
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
    
    public Tile selectedTile() {
        return this.selectedTile;
    }

    public void setSelectedTile(Tile tile) {
        
        if (this.selectedTile == tile) return;
        
        this.selectedTile = tile;
        draw();
    }

    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
        draw();
    }
}
