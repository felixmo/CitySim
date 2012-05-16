import java.util.LinkedHashMap;

/**
 * Write a description of class CityStatsDBUpdateThrad here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CityStatsDBUpdateThread extends CSThread 
{
    private static int count = 0;
    private LinkedHashMap cityStats;

    public CityStatsDBUpdateThread(LinkedHashMap cityStats) {
        super("CityStatsDBUpdateThread#" + (count+=1));
        this.cityStats = cityStats;
    }

    public void run() {
        DataSource.getInstance().updateCityStats(cityStats);
    }
}
