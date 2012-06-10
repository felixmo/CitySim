/**
 * Write a description of class Finances here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Finances
{

    private static float rate = 1.10f; // Default rate of 10% tax

    public static void collectTaxesAndDeductExpenses() {

        float tax = 0.0f;

        // pop. * tax rate
        tax += ((Integer)Data.cityStats().get(Data.CITYSTATS_POPULATION)).intValue() * rate;

        // Tax commercial zones
        tax += DataSource.getInstance().totalCommercialCapacity() * rate;        

        // Tax industrial zones
        tax += DataSource.getInstance().totalIndustrialCapacity() * rate;

        // # police stations * 100
        tax -= Data.zonesMatchingCriteria("zone = " + PoliceStation.TYPE_ID).length * 100;

        // # fire statoins * 100
        tax -= Data.zonesMatchingCriteria("zone = " + FireStation.TYPE_ID).length * 100;

        tax -= Data.zonesMatchingCriteria("zone = " + CoalPowerPlant.TYPE_ID).length * 300;

        tax -= Data.zonesMatchingCriteria("zone = " + NuclearPowerPlant.TYPE_ID).length * 500;

        // # roads
        tax -= ((Integer)Data.roadStats().get(Data.ROADSTATS_STREETCOUNT)).intValue();

        CSLogger.sharedLogger().info("Collected $" + (int)tax + " in taxes this year");

        Cash.add((int)tax);
    }
}
