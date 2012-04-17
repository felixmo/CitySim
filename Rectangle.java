/**
 * Rectangle
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 02-11-2012
 *
 * Data structure for rectangles
 *
 */

public class Rectangle  
{

    // ---------------------------------------------------------------------------------------------------------------------

    /*
     * INSTANCE VARIABLES
     */
    
    // Rectangle properties
    private Point origin;	// Point of origin for the rectangle
    private int width;		// Width of the rectangle
    private int height;		// Height of the rectangle

    // ---------------------------------------------------------------------------------------------------------------------

    public Rectangle(Point origin, int width, int height) {
        this.origin = origin;
        this.width = width;
        this.height = height;
    }

    /*
     * ACCESSORS *
     */
    
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
