/**
 * Write a description of class Rectangle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rectangle  
{
    private Point origin;
    private int width;
    private int height;
    
    public Rectangle(Point origin, int width, int height) {
        this.origin = origin;
        this.width = width;
        this.height = height;
    }
    
    public Point origin() {
        return origin;
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    public void setOrigin(Point value) {
        origin = value;
    }
    
    public void setWidth(int value) {
        width = value;
    }
    
    public void setHeight(int value) {
        height = value;
    }
}
