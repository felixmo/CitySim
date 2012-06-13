import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class Overlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Overlay extends Actor  
{
    public Overlay() {
        GreenfootImage image = new GreenfootImage(1024, 768);
        image.setTransparency(175);
        image.setColor(Color.BLACK);
        image.fillRect(0, 0, 1024, 768);
        setImage(image);
    }
}
