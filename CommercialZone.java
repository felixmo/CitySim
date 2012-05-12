/**
 * Write a description of class CommercialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CommercialZone extends Zone
{
    public static final int ID = 2;
    public static final int[] MARKERS = { 500 };

    /*
     * ACCESSORS *
     */
    
    public static int count() {
        Integer count = Zone.counts.get((Integer)ID);
        return count == null ? 0 : count;
    }
}
