/**
 * Write a description of class CommercialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CommercialZoneSimulationThread extends CSThread  
{
    private CommercialZone zone;
    
    public CommercialZoneSimulationThread(CommercialZone zone) {
        super("");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}

