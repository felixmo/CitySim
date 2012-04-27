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
    private World world;
    private MenuBarItem menuBarItem;
    private ArrayList<String> items;
    private ArrayList<MenuItem> menuItems;
    private Rectangle frame;
    private GreenfootImage image;
    private boolean active;
    private int activeIndex = -1;

    private MouseInfo mouseInfo;

    // ---------------------------------------------------------------------------------------------------------------------

    public Menu(MenuBarItem barItem, ArrayList<String> items) {

        this.menuBarItem = barItem;
        this.menuItems = new ArrayList<MenuItem>();

        // Configure font
        Menu.setFont(loadFont(FONT_CABIN).deriveFont((float)14.0));

        setItems(items);
    }

    protected void addedToWorld(World world) {

        this.world = world;
        if (this.image != null) draw();
    }

    private void draw() {

        Point origin = this.frame.origin();

        this.image.clear();

        this.image.setColor(Color.WHITE);
        this.image.fillRect(0, 0, this.frame.width(), this.frame.height());
        this.image.setColor(Color.BLACK);
        this.image.drawRect(0, 0, this.frame.width(), this.frame.height());

        for (MenuItem menuItem : menuItems) {
            world.addObject(menuItem, menuItem.frame().origin().x()+(int)menuItem.frame().width()/2, menuItem.frame().origin().y());
        }
    }

    public void act() {
        
        Point mouse = null;

        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (active) {

            if (mouseInfo != null) {
                mouse = new Point(mouseInfo.getX(), mouseInfo.getY());
                
//                 System.out.println("MOUSE: " + mouse.toString());

                if (mouse.y() <= this.frame.height() + this.frame.origin().y() && mouse.y() >= menuBarItem.frame().height()) {

                    for (MenuItem item : menuItems) {

                        Rectangle frame = item.frame();
                        Point origin = item.frame().origin();

                        if (mouse.y() >= origin.y() && mouse.y() <= origin.y() + frame.height()) {

                            if (menuItems.indexOf(item) != this.activeIndex) {

                                didHighlightMenuItem(item);
                            }
                        }
                    }
                }
            }
        }
    }

    public void didHighlightMenuItem(MenuItem menuItem) {

        for (MenuItem item : menuItems) {
            item.setActive(item == menuItem ? (active ? true : false) : false);
        }

        this.activeIndex = menuItem.index();
    }
    
    public void didSelectMenuItem(MenuItem menuItem) {
        
//         System.out.println(menuItem.text() + " was selected.");
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

    public Rectangle frame() {
        return this.frame;
    }

    public static Font font() {
        return font;
    }

    public static void setFont(Font value) {
        font = value;
    }

    public ArrayList<MenuItem> menuItems() {
        return this.menuItems;
    }

    public void setItems(ArrayList<String> items) {

        this.items = items;

        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(512, 28).getAwtImage().getGraphics().getFontMetrics(Menu.font()); 
        int width = 0;
        int index = 0;
        // Find the widest menu item and use it's width + padding as the menu width
        for (String item : this.items) {

            if (fontMetrics.stringWidth(item) > width) {
                width = fontMetrics.stringWidth(item)+28;
            }
        }
        
        // Create the 'MenuItem' actors 
        for (String item : this.items) {
            MenuItem menuItem = new MenuItem(item, this, index);
            Rectangle miFrame = new Rectangle(new Point(menuBarItem.frame().origin().x(), (menuBarItem.frame().origin().y()+14)+10+index*24), width, 22);
            menuItem.setFrame(miFrame);
            this.menuItems.add(menuItem);
            index++;
        }
        
        int height = items.size() * 24;     // 14px + 4px padding
        this.frame = new Rectangle(new Point(menuBarItem.frame().origin().x(), menuBarItem.frame().origin().y()+14), width, height);

        this.image = new GreenfootImage(width, height);
        setImage(this.image);
    }
    
    public boolean active() {
        return this.active;
    }
    
    public void setActive(boolean value) {
        this.active = value;
    }
}
