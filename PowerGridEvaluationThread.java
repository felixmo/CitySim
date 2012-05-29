/**
 * Write a description of class PowerGridEvaluationThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationThread extends CSThread
{
    public PowerGridEvaluationThread()
    {
        super("");
    }
    
    public void run() {
        PowerGrid.evaluate();
    }
}
