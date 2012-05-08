import greenfoot.*;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * Label
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 02-17-2012
 * 
 * A 'label' UI element
 * 
 */

public class Label extends Actor
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES & CONSTANTS *
     */

    // Font and font properties
    private static final Color FONTCOLOR = new Color(55, 55, 55);                  // Default font colour

    // Label properties
    private Rectangle frame;    // Location and dimensions of the label
    private String text;        // Label text

    private GreenfootImage image;   // Label view

    // ---------------------------------------------------------------------------------------------------------------------

    public Label(Rectangle frame) {

        this.frame = frame;

        image = new GreenfootImage(frame.width, frame.height);
        image.setFont(CSFont.cabin((float)frame.height/2));
        image.setColor(FONTCOLOR);
        setImage(image);
    }

    /*
     * ACCESSORS *
     */

    public void setText(String value) {

        this.text = value;

        image.clear();
        image.drawString(this.text, 1, frame.height/2);
    }

    public void setColor(Color value) {

        this.image.setColor(value);
        this.image.clear();
        image.drawString(this.text, 1, frame.height/2);
    }

    public Rectangle frame() {
        return frame;
    }
}
