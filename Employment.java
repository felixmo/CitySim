import java.util.ArrayList;
import java.util.Arrays;

/**
 * Write a description of class Employment here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Employment  
{
    public static ArrayList<Zone> employers = new ArrayList<Zone>();

    public static void reset() {
        DataSource.getInstance().resetJobAllocations();

        employers.clear();
        employers.addAll(Arrays.asList(Data.industrialZones()));
        employers.addAll(Arrays.asList(Data.commercialZones()));
    }

    public static synchronized int employResidents(ResidentialZone zone) {

        int avaliableWorkers = zone.allocation();

        for (Zone employer : employers) {
            if (employer.allocation() < employer.capacity()) {
                int hired = Math.min(avaliableWorkers, employer.capacity()-employer.allocation());
                DataSource.getInstance().updateJobAllocationForZone(employer.allocation() + hired, employer);
                avaliableWorkers -= hired;
            }
        }

        return avaliableWorkers;
    }
}
