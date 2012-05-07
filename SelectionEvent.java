/**
 * Write a description of class SelectionEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionEvent extends CSEvent
{

    public static final String TILE_SELECTED = "TILE_SELECTED";
   
    private Tile tile;
    
    public SelectionEvent(String message, Tile tile) {
        super(message);
        this.tile = tile;
    }
    
    public Tile tile() {
        return this.tile;
    }
}
