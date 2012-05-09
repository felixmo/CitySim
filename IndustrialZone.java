import java.util.ArrayList;

/**
 * Write a description of class IndustrialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZone extends Zone
{

    public static final int ID = 2;
    public static final int[] MARKERS = { 500 };

    private static int tiles = 0;

    public static void zoneTiles(ArrayList<ArrayList<Tile>> selectedTiles) {
        tiles += selectedTiles.size() * ((ArrayList)selectedTiles.get(0)).size();
        Zone.zoneTiles(selectedTiles);
    }

    /*
     * ACCESSORS *
     */

    public static int tiles() {
        return tiles;
    }
}
