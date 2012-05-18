import greenfoot.*;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * 'MenuElement' is an abstract class which provides a partial implementation of a menu UI element
 * 
 * @author Felix Mo
 * @version v0.1
 * @since 2012-05-01
 */

public abstract class MenuElement extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTANTS *
     */
    private static final float FONTSIZE = 14.0f;                // Font size for text
    protected static final Font FONT = CSFont.cabin(FONTSIZE);  // Font used by elements

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * ATTRIBUTES *
     */
    protected String title;                                     // Element's title/text
    protected Rectangle frame;                                  // Frame (dimensions & origin)
    protected GreenfootImage image;                             // Element's view is contained in this GreenfootImage
    protected int index;                                        // Element's index; it's position relative to other elements it is grouped with
    protected boolean active;                                   // Element's state

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    public MenuElement(String title, int index) {
        this.title = title;
        this.index = index;
    }

    // ---------------------------------------------------------------------------------------------------------------------
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