/**
 * Write a description of class TaxCollectionThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FinancesThread extends CSThread  
{
    public FinancesThread() {
        super("FinancesThread");
    }
    
    public void run() {
        Finances.collectTaxesAndDeductExpenses();
    }
}
