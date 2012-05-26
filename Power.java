/**
 * Write a description of class Power here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Power  
{
    private static int pendingOp = 0;
    private static int activeType = 0;

    /*
     * ACCESSORS *
     */

    public static int pendingOp() {
        return pendingOp;
    }

    public static void setPendingOp(int value) {
        pendingOp = value;
    }

    public static int activeType() {
        return activeType;
    }

    public static void setActiveType(int value) {
        activeType = value;
    }
}
