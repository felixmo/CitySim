import java.lang.Integer;

/**
 * Write a description of class TileSelectorEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TileSelectorEvent extends CSEvent
{
 
    private int type = 0;

    public TileSelectorEvent(String message) {
        super(message);
        
        this.type = Integer.parseInt(message);
    }   
    
    /*
     * ACCESSORS *
     */
    
    public int type() {
        return this.type;
    }
}
