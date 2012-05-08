import greenfoot.*;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

/**
 * Write a description of class Hint here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Hint extends Actor
{
    private static final Font FONT = CSFont.cabin((float)25.0f);
    public static final int ORIGIN_X = 512;
    public static final int ORIGIN_Y = 28;

    private GreenfootImage image;
    private GreenfootImage label;
    private String text;
    private boolean fade;

    public Hint(String text) {

        this.image = new GreenfootImage("images/notif.png");
        setImage(this.image);

        this.label = new GreenfootImage(1024, 50);
        this.label.setColor(Color.WHITE);
        this.label.setFont(CSFont.cabin((float)25.0f));
        setText(text);
        this.image.drawImage(this.label, 0, 25);
    }

    public void setText(String text) {

        this.text = text;

        FontMetrics fontMetrics = this.label.getAwtImage().getGraphics().getFontMetrics(Hint.FONT); 
        int width = fontMetrics.stringWidth(this.text);

        this.label.clear();
        this.label.drawString(this.text, (int)width/2, 25);
    }

    public void act() {

        if (fade) this.image.setTransparency(this.image.getTransparency()-1);    
        fade = !fade;

        if (this.image.getTransparency() == 0 || Greenfoot.mousePressed(this)) {
            City.getInstance().removeObject(this);
        }
    }
}
