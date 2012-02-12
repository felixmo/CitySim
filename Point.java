/**
 * Write a description of class Point here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Point  
{
    private int x;
    private int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() {
        return x;
    }
    
    public int y() {
        return y;
    }
    
    public void setX(int value) {
        x = value;
    }
    
    public void setY(int value) {
        y = value;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
