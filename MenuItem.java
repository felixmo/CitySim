import greenfoot.*;
import java.awt.FontMetrics;

/**
 * Write a description of class MenuItem here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MenuItem extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */
    private World world;
    private Menu menu;
    private String title;
    private Rectangle frame;

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuItem(String title, Menu menu, Point origin) {
        
        this.menu = menu;
        this.title = title;

//         FontMetrics fontMetrics = new GreenfootImage(512, 28).getAwtImage().getGraphics().getFontMetrics(font); 
        
//         this.frame = new Rectangle(origin, fontMetrics.stringWidth(title)+36, 18); // width + 18px padding on each side | height (14.0 pt) + 4px padding (top & bottom)
    }

    protected void addedToWorld(World world) {
        this.world = world;   
    }
    
    public void act() {

    }

    public void setActive(boolean active) {

//         world.addObject(menu, (int)menu.frame().width()/2, (int)menu.frame().width()/2+18);
    }
    
    /*
     * ACCESSORS *
     */
    public Rectangle frame() {
        return frame;
    }
}
