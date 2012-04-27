import greenfoot.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.InputStream;

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
     * FONT *
     */
    private static final Color FONT_COLOR = new Color(55, 55, 55);                  // Default font colour
    private static final String FONT_CABIN = "fonts/cabin/Cabin-Regular-TTF.ttf";   // Default font
    private static Font font;

    /*
     * INSTANCE VARIABLES *
     */
    private World world;
    private Menu menu;
    private String text;
    private Rectangle frame;
    private GreenfootImage image;
    private int index;
    private boolean active;

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuItem(String text, Menu menu, int index) {

        this.menu = menu;
        this.text = text;
        this.index = index;

        MenuItem.setFont(loadFont(FONT_CABIN).deriveFont((float)14.0));
    }

    protected void addedToWorld(World world) {
        this.world = world;   
        draw();
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

            menu.didSelectMenuItem(this);
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
     * ACCESSORS *
     */

    public static Font font() {
        return font;
    }

    public static void setFont(Font value) {
        font = value;
    }

    public Rectangle frame() {
        return frame;
    }

    public void setFrame(Rectangle frame) {
        this.frame = frame;

        // Set image
        this.image = new GreenfootImage(this.frame.width(), this.frame.height());
        setImage(image);
    }
    
    public boolean active() {
        return this.active;
    }
    
    public int index() {
        return this.index;
    }
    
    public String text() {
        return this.text;
    }
}
