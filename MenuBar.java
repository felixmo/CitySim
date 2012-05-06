import greenfoot.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * MenuBar
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-12-2012
 * 
 * Menu bar view and view controller
 * 
 */

public class MenuBar extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */

    private LinkedHashMap<String, MenuBarItem> menuBarItems;
    private ArrayList<MenuBarItem> items;
    private LinkedHashMap<String, Menu> menus;
    private int activeIndex = -1;

    private MouseInfo mouseInfo;

    private World world;

    private final Rectangle frame = new Rectangle(512, 14, 1024, 28);

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuBar()
    {
        setImage("menu.png");   
        this.menuBarItems = new LinkedHashMap<String, MenuBarItem>();
        this.menus = new LinkedHashMap<String, Menu>();
    }

    protected void addedToWorld(World world) {

        this.world = world;
        if (menuBarItems.size() > 0) draw();
    }

    private void draw() {

        if (world == null) return;

        // Populate menu bar
        // NOTE: space btw. elements is 18px

        int x = 18;

        for (Object object : menuBarItems.values().toArray()) {
            
            MenuBarItem menuBarItem = (MenuBarItem)object;
            
            menuBarItem.setOrigin(new Point(x, 11));
            world.addObject(menuBarItem, menuBarItem.frame().x+(int)menuBarItem.frame().width/2, menuBarItem.frame().y);
            x += menuBarItem.frame().width + 18;
        }
    }

    public void act() {

        if (activeIndex < 0) return;

        Point mouse = null;

        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (mouseInfo != null) {
            mouse = new Point(mouseInfo.getX(), mouseInfo.getY());

            if (mouse.y <= this.frame.height) {

                for (MenuBarItem menuBarItem : items) {

                    Point origin = menuBarItem.frame().getLocation();

                    if (mouse.x >= origin.x && mouse.x <= origin.x + menuBarItem.frame().width) {

                        if (items.indexOf(menuBarItem) != this.activeIndex) {

                            changeItemStateTo(menuBarItem, !menuBarItem.active());
                        }
                    }
                }
            }
        }
    }

    public void changeItemStateTo(MenuBarItem menuBarItem, boolean state) {

        for (MenuBarItem item : items) {
            item.setActive(item == menuBarItem ? (state ? true : false) : false);
        }

        this.activeIndex = state ? menuBarItem.index() : -1;
    }
    
    public void setMenuItemsForItem(String item, ArrayList<String> items) {

        MenuBarItem menuBarItem = menuBarItems.get(item);
        
        Menu menu = new Menu(menuBarItem, items);
        menus.put(item, menu);
        menuBarItem.setMenu(menu);
    }

    /*
     * ACCESSORS *
     */
    public void setItems(ArrayList<String> values) {
        
        items = new ArrayList<MenuBarItem>();
        int index = 0;
        
        for (String item : values) {
            MenuBarItem menuBarItem = new MenuBarItem(item, index, this);
            menuBarItems.put(item, menuBarItem);
            items.add(menuBarItem);
            index++;
        }
        
        draw();
    }
}
