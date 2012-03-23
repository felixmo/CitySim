import greenfoot.*;
import java.awt.Color;
import java.lang.Math;

/**
 * Write a description of class Minimap_Viewport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
            
//             System.out.println("Minimap was clicked @ " + location.x() + ", " + location.y());
            
            origin = location;
            draw();
            ((City)getWorld()).didMoveViewportTo(location);
        }
    }

    private void draw() {

        image.clear();

        image.setColor(Color.RED);
        image.drawRect(origin.x(), origin.y(), 20, 10);
    }
    
    public void didMoveViewportToCell(Point location) {
        origin.setX(location.x() * scale);
        origin.setY(location.y() * scale);
        
        move = true;
        draw();
    }
}
