import java.util.HashMap;

/**
 * Write a description of class CityStatsDBUpdateThrad here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CityStatsDBUpdateThread extends CSThread 
{
    private static int count = 0;
    private HashMap cityStats;

    public CityStatsDBUpdateThread(HashMap cityStats) {
        super("CityStatsDBUpdateThread#" + (count+=1));
        this.cityStats = cityStats;
    }

    public void run() {
        DataSource.getInstance().updateCityStats(cityStats);
    }
}
