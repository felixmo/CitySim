/**
 * Write a description of class PowerGridEvaluationZoneSearchThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationZoneSearchThread extends CSThread
{
    private Zone zone;

    public PowerGridEvaluationZoneSearchThread(Zone zone) {
        super("");
        this.zone = zone;
    }
        
    public void run() {
        PowerGrid.searchZone(this.zone);
    }
}
