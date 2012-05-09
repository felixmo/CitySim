import java.util.ArrayList;

/**
 * Write a description of class ResidentialZone here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZone extends Zone
{
    public static final int ID = 1;
    public static final int[] MARKERS = { 400, 401, 402, 403, 404, 405, 406, 407, 408 };

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
