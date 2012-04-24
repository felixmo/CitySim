import greenfoot.*;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Color;
import java.io.InputStream;

/**
 * MenuBarItem
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-18-2012
 * 
 * Menu item view
 * 
 */

public class MenuBarItem extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * FONT *
     */
    private static final Color FONT_COLOR = new Color(55, 55, 55);                  // Default font colour
    private static final String FONT_CABIN = "fonts/cabin/Cabin-Regular-TTF.ttf";   // Default font
    private static Font font;
    
    /*
     * INSTANCE VARIABLES
     */
    private GreenfootImage image;
    private String text;
    private Rectangle frame;
    private MenuBar menuBar;
    private int index;
    private boolean active;

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuBarItem(String text, MenuBar menuBar) {

        this.text = text;
        this.menuBar = menuBar;
        
        MenuBarItem.setFont(loadFont(FONT_CABIN).deriveFont((float)14.0));
    }

    private void draw() {

        // Create the image and draw text within it
        this.image.clear();
        this.image.setColor(Color.BLACK);
        this.image.setFont(MenuBarItem.font());
        this.image.drawString(this.text, 8, 16);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {

            menuBar.setActive(this, !active);
        }
    }

    public void setActive(boolean active) { 

        this.active = active;

        this.image.clear();

        if (active) {

            this.image.setColor(new Color(55, 106, 233));
            this.image.fill();
            this.image.setColor(Color.WHITE);
            this.image.drawString(this.text, 8, 16);

        }
        else {

            this.image.setColor(Color.BLACK);
            this.image.drawString(this.text, 8, 16);
        }
    }

    /*
     * HELPERS *
     */

    // Loads a TTF font from the specified path and creates a 'Font' object from it
    private Font loadFont(String path) {

        try {
            InputStream is = Label.class.getResourceAsStream(path);
            return Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * ACCESSORS
     */

    public Rectangle frame() {
        return frame;
    }

    public void setOrigin(Point value) {

        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(1024, 28).getAwtImage().getGraphics().getFontMetrics(MenuBarItem.font()); 
        int width = fontMetrics.stringWidth(text);
        this.frame = new Rectangle(value, width+16, 22);
        
        // Set image
        this.image = new GreenfootImage(this.frame.width(), this.frame.height());
        setImage(image);

        draw();
    }

    public int index() {
        return index;
    }

    public void setIndex(int value) {
        this.index = value;
    }

    public boolean active() {
        return active;
    }
    
    public static Font font() {
        return font;
    }
    
    public static void setFont(Font value) {
        font = value;
    }
}
