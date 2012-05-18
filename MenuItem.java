import greenfoot.*;
import java.awt.Color;

/**
 * MenuItem
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 04-20-2012
 * 
 * Menu item view; extends MenuElement
 * 
 */

public class MenuItem extends MenuElement
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES *
     */
    private Menu menu;

    // ---------------------------------------------------------------------------------------------------------------------

    public MenuItem(String title, Menu menu, int index) {

        super(title, index);
        
        this.menu = menu;
    }

    protected void addedToWorld(World world) {
        draw();
    }

    private void draw() {

        // Create the image and draw text within it
        this.image.clear();
        this.image.setColor(Color.BLACK);
        this.image.setFont(FONT);
        this.image.drawString(this.title, 8, 16);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            
            CSEventBus.post(new MenuItemEvent(this.title));
            menu.setActive(false);
        }
    }

    public void setActive(boolean active) {

        this.image.clear();

        if (active) {

            this.image.setColor(new Color(55, 106, 233));
            this.image.fill();
            this.image.setColor(Color.WHITE);
            this.image.drawString(this.title, 8, 16);
        }
        else {

            this.image.setColor(Color.BLACK);
            this.image.drawString(this.title, 8, 16);
        }
    }
}
