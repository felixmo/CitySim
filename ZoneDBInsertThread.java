import java.util.HashMap;

/**
 * Write a description of class ZoneDBInsertThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneDBInsertThread extends CSThread
{

    private HashMap[] zoneTiles;    

    public ZoneDBInsertThread(HashMap[] zoneTiles) {
        super("");
        this.zoneTiles = zoneTiles;
    }

    public void run() {
        DataSource.getInstance().insertZoneWithTiles(this.zoneTiles);
    }
}
