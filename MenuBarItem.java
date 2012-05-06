import greenfoot.*;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

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

public class MenuBarItem extends MenuElement
{

    // ---------------------------------------------------------------------------------------------------------------------
    
    /*
     * INSTANCE VARIABLES
     */
    private MenuBar menuBar;
    private Menu menu;

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuBarItem(String title, int index, MenuBar menuBar) {

        super(title, index);
        
        this.menuBar = menuBar;
    }

    protected void addedToWorld(World world) {
        this.world = world;
    }
    
    private void draw() {

        // Create the image and draw title within it
        this.image.clear();
        this.image.setColor(Color.BLACK);
        this.image.setFont(font);
        this.image.drawString(this.title, 8, 16);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {

            menuBar.changeItemStateTo(this, !active);
        }
    }

    public void setActive(boolean active) {

        this.active = active;

        this.image.clear();

        if (active) {

            this.image.setColor(new Color(55, 106, 233));
            this.image.fill();
            this.image.setColor(Color.WHITE);
            this.image.drawString(this.title, 8, 16);

            if (this.menu != null) {
                this.menu.setActive(true);
                this.world.addObject(menu, this.frame.x+(int)this.menu.frame().width/2, this.frame.y+(int)this.menu.frame().height/2+(int)this.frame.height/2);
            }
        }
        else {

            this.image.setColor(Color.BLACK);
            this.image.drawString(this.title, 8, 16);

            if (this.menu != null) {
                this.menu.setActive(false);
                world.removeObjects(menu.menuItems());
                world.removeObject(menu);
            }
        }
    }

    public void setOrigin(Point value) {

        // Create frame based on dimensions derived from font metrics
        FontMetrics fontMetrics = new GreenfootImage(1024, 28).getAwtImage().getGraphics().getFontMetrics(font); 
        int width = fontMetrics.stringWidth(title);
        this.frame = new Rectangle(value.x, value.y, width+16, 22);

        // Set image
        this.image = new GreenfootImage(this.frame.width, this.frame.height);
        setImage(image);

        draw();
    }

    /*
     * ACCESSORS
     */

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
