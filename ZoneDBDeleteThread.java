/**
 * Write a description of class ZoneDBDeleteThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneDBDeleteThread extends CSThread
{

    private int zoneID;

    public ZoneDBDeleteThread(int zoneID) {
        super("");
        this.zoneID = zoneID;
    }

    public void run() {
        DataSource.getInstance().deleteZoneWithID(this.zoneID);
    }
}
