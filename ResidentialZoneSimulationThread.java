/**
 * Write a description of class ResidentialZoneSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ResidentialZoneSimulationThread extends CSThread  
{
    private ResidentialZone zone;
    
    public ResidentialZoneSimulationThread(ResidentialZone zone) {
        super("");
        this.zone = zone;
    }
    
    public void run() {
        zone.simulate();
    }
}
