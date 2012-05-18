import java.util.ArrayList;

/**
 * Write a description of class SelectionEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionEvent extends CSEvent
{

    public static final String TILES_SELECTED_FOR_ZONING = "TILES_SELECTED_FOR_ZONING";
    public static final String TILE_SELECTED_FOR_ROAD = "TILE_SELECTED_FOR_ROAD";
    public static final String TILES_SELECTED_FOR_TOOLS = "TILES_SELECTED_FOR_TOOLS";
    public static final String TILE_SELECTED_FOR_TOOL = "TILE_SELECTED_FOR_TOOL";
    
    private ArrayList<ArrayList<Tile>> tiles;
    private Tile tile;

    public SelectionEvent(String message) {
        super(message);
    }

    public SelectionEvent(String message, ArrayList<ArrayList<Tile>> tiles) {
        super(message);
        this.tiles = tiles;
    }

    public SelectionEvent(String message, Tile tile) {
        super(message);
        this.tile = tile;
    }

    /*
     * ACCESSORS *
     */

    public ArrayList<ArrayList<Tile>> tiles() {
        return this.tiles;    
    }

    public Tile tile() {
        return this.tile;
    }
}
