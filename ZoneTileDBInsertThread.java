import java.util.HashMap;

/**
 * Write a description of class ZoneTilesDBInsertThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneTileDBInsertThread extends CSThread
{

    private HashMap[] zoneTiles;    

    public ZoneTileDBInsertThread(HashMap[] zoneTiles) {
        super("");
        this.zoneTiles = zoneTiles;
    }

    public void run() {
        DataSource.getInstance().insertZoneTiles(this.zoneTiles);
    }
}
