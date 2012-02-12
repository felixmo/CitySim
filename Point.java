/**
 * Point
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * Data structure for coordiante pairs / points
 *
 */

public class Point  
{
	// * Instance variables *
	
	// Coordinates
    private int x;
    private int y;

	// * END of instance variables *
	
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
	// * Accessors *

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

	// * END of accessors *
    

	// Returns a string representation of the point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
