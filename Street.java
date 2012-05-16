import java.awt.Dimension;

/**
 * Write a description of class Street here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Street extends Road
{
    public static final int ID = 1;
    public static final Dimension SIZE = new Dimension(1, 1);

    public static void buildStreet(Tile tile, int type) {

        // System.out.println("Building street");
        
        tile.setType(type);

        Road.updateTile(tile);
    }

    /*
     * ACCESSORS *
     */

    public static int count() {
        Integer count = Zone.counts.get((Integer)ID);
        return count == null ? 0 : count;
    }
}
