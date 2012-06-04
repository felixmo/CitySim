/**
 * Write a description of class IndustrialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IndustrialZoneSimulationThread extends CSThread  
{
    private IndustrialZone zone;
    
    public IndustrialZoneSimulationThread(IndustrialZone zone) {
        super("");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}
