import greenfoot.*;
import java.awt.Color;
import java.lang.Math;

/**
 * Minimap_Viewport
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 03-06-2012
 *
 *
 *
 */

public class Minimap_Viewport extends Actor
{
    private GreenfootImage image;

    private Point origin;
    private boolean move;
    private int scale = 2;

    public Minimap_Viewport(Point origin) {
        
        this.origin = origin;

        this.image = new GreenfootImage(200, 200);
        setImage(this.image);

        draw();
    }

    public void act() {
        
        if (Greenfoot.mouseClicked(this)) {
        
            MouseInfo mouseInfo = Greenfoot.getMouseInfo();
            
            // Coordinates of the cursor, relative to the minimap
            Point location = new Point((mouseInfo.getX() - 12), (mouseInfo.getY() - 558));
            // X | Subtract distance from left edge of HUD to left edge of minimap from X
            // Y | Subtract distance from the top of the game to the top of the minimap from Y
            
            origin = location;
            draw();
            ((City)getWorld()).didMoveViewportTo(location);
        }
    }

    private void draw() {

        image.clear();

        image.setColor(Color.RED);
        image.drawRect(origin.x(), origin.y(), 40, 25);
    }
    
    public void didMoveViewportToCell(Point location) {
        origin.setX(location.x() * scale);
        origin.setY(location.y() * scale);
        
        move = true;
        draw();
    }
}
