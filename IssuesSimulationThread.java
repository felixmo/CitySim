/**
 * Write a description of class IssuesSimulationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IssuesSimulationThread extends CSThread
{

    /**
     * Constructor for objects of class IssuesSimulationThread
     */
    public IssuesSimulationThread()
    {
        super("IssuesSimulationThread");
    }

    public void run() {
        Issues.simulate();
    }
}
