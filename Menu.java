import greenfoot.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.InputStream;

/**
 * Menu
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-19-2012
 * 
 * Menu view
 * 
 */

public class Menu extends Actor
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
    private MenuBarItem menuBarItem;
    private ArrayList<String> items;
    private ArrayList<MenuItem> menuItems;
    private Rectangle frame;
    private GreenfootImage image;
    private boolean active;
    private int activeIndex;
    
    private MouseInfo mouseInfo;

    // ---------------------------------------------------------------------------------------------------------------------

    public Menu(MenuBarItem barItem, ArrayList<String> items) {

        this.menuBarItem = barItem;
        this.items = items;

        // Configure font
        Menu.setFont(loadFont(FONT_CABIN).deriveFont((float)14.0));
    }
    
    protected void addedToWorld(World world) {
        
        if (this.image != null) draw();
    }
    
    private void draw() {
        
        Point origin = this.frame.origin();
        
        this.image.clear();
        
        this.image.setColor(Color.WHITE);
        this.image.fillRect(0, 0, this.frame.width(), this.frame.height());
        this.image.setColor(Color.BLACK);
        this.image.drawRect(0, 0, this.frame.width(), this.frame.height());
    }

    public void act() {

        Point mouse = null;

        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (active) {

            if (mouseInfo != null) {
                mouse = new Point(mouseInfo.getX(), mouseInfo.getY());

                if (mouse.y() <= this.frame.height()) {

                    for (MenuItem item : menuItems) {

                        Point origin = item.frame().origin();

                        if (mouse.y() >= origin.y() && mouse.y() <= origin.y()) {
                            
                            
                        }
                    }
                }
            }
        }
        else {

        }
    }

    public void setActive(boolean active) {

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
    
    public void setItems(ArrayList<String> items) {
        
        this.items = items;
        
        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(512, 28).getAwtImage().getGraphics().getFontMetrics(Menu.font()); 
        int width = 0;
        for (String item : this.items) {
            if (fontMetrics.stringWidth(item) > width) {
                width = fontMetrics.stringWidth(item);
            }
        }
        int height = items.size() * 18;     // 14px + 4px padding
        this.frame = new Rectangle(new Point(menuBarItem.frame().origin().x(), menuBarItem.frame().origin().y()+14), width, height);
        
        this.image = new GreenfootImage(width, height);
    }
}
