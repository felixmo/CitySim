import java.util.ArrayList;

/**
 * Write a description of class SelectionEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionEvent extends CSEvent
{

    public static final String TILES_SELECTED = "TILES_SELECTED";
   
    private ArrayList<ArrayList<Tile>> tiles;
    
    public SelectionEvent(String message) {
        super(message);
    }
    
    public SelectionEvent(String message, ArrayList<ArrayList<Tile>> tiles) {
        super(message);
        this.tiles = tiles;
    }
    
    public ArrayList<ArrayList<Tile>> tiles() {
        return this.tiles;    
    }
}
