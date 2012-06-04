/**
 * Write a description of class ZonePrimaryAllocationDBUpdateThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZoneDBUpdateThread extends CSThread 
{
    private Zone zone;

    public ZoneDBUpdateThread(Zone zone) {
        super("");
        this.zone = zone;
    }
    
    public void run() {
        DataSource.getInstance().updateZone(this.zone);
    }
}
