import greenfoot.*;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * Write a description of class MenuElement here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class MenuElement extends Actor
{

    private static final float FONTSIZE = 14.0f;

    protected World world;
    protected String title;
    protected Rectangle frame;
    protected GreenfootImage image;
    protected int index;
    protected boolean active;
    protected Font font;

    public MenuElement(String title, int index) {
        this.title = title;
        this.index = index;
        this.font = CSFont.cabin(FONTSIZE);
    }

    /*
     * ACCESSORS *
     */
    
    public Rectangle frame() {
        return frame;
    }

    public void setFrame(Rectangle frame) {
        this.frame = frame;

        // Set image
        this.image = new GreenfootImage(this.frame.width, this.frame.height);
        setImage(image);
    } 

    public boolean active() {
        return this.active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public int index() {
        return this.index;
    }
}