import greenfoot.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;

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

public class Menu extends MenuElement
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES
     */

    private MenuBarItem menuBarItem;
    private ArrayList<String> items;
    private ArrayList<MenuItem> menuItems;
    private int activeIndex = -1;

    private MouseInfo mouseInfo;

    // ---------------------------------------------------------------------------------------------------------------------

    public Menu(MenuBarItem barItem, ArrayList<String> items) {

        super("", 0);

        this.menuBarItem = barItem;
        this.menuItems = new ArrayList<MenuItem>();

        setItems(items);
    }

    protected void addedToWorld(World world) {
        this.world = world;   
        this.active = true;
        if (this.image != null) draw();
    }

    private void draw() {

        Point origin = this.frame.getLocation();

        this.image.clear();

        this.image.setColor(Color.WHITE);
        this.image.fillRect(0, 0, this.frame.width, this.frame.height);
        this.image.setColor(Color.BLACK);
        this.image.drawRect(0, 0, this.frame.width, this.frame.height);

        for (MenuItem menuItem : menuItems) {
            this.world.addObject(menuItem, menuItem.frame().x+(int)menuItem.frame().width/2, menuItem.frame().y);
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

                if (mouse.x <= this.frame.width + this.frame.x && mouse.x >= menuBarItem.frame().x) {
                    if (mouse.y <= this.frame.height + this.frame.y && mouse.y >= menuBarItem.frame().height) {

                        for (MenuItem item : menuItems) {

                            Rectangle frame = item.frame();
                            Point origin = item.frame().getLocation();

                            if (mouse.y >= origin.y && mouse.y <= origin.y + frame.height) {

                                if (menuItems.indexOf(item) != this.activeIndex) {

                                    didHighlightMenuItem(item);
                                }
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

    /*
     * ACCESSORS *
     */

    public ArrayList<MenuItem> menuItems() {
        return this.menuItems;
    }

    public void setItems(ArrayList<String> items) {

        this.items = items;

        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(512, 28).getAwtImage().getGraphics().getFontMetrics(font); 
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
            Rectangle miFrame = new Rectangle(menuBarItem.frame().x, menuBarItem.frame().y+14+10+index*24, width, 22);
            menuItem.setFrame(miFrame);
            this.menuItems.add(menuItem);
            index++;
        }

        int height = items.size() * 24;     // 14px + 4px padding
        this.frame = new Rectangle(menuBarItem.frame().x, menuBarItem.frame().y+14, width, height);

        this.image = new GreenfootImage(width, height);
        setImage(this.image);
    }

    public void setActive(boolean value) {

        this.active = value;
        if (!active) {
            this.activeIndex = -1;
            this.menuBarItem.menuBar().changeItemStateTo(this.menuBarItem, false);
//             this.world.removeObjects(menuItems);
//             this.world.removeObject(this);
        }
    }
}
