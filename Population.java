/**
 * Write a description of class Population here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Population  
{
    private static int size = 0;
    
    public static int size() {
        return size;
    }
    
    public static void set(int value) {
        size = value;
    }
    
    public static String category() {
        if (size <= 1999) {
            return "Village";
        }
        else if (size <= 9999) {
            return "Town";
        }
        else if (size <= 49999) {
            return "City";
        }
        else if (size <= 99999) {
            return "Capital";
        }
        else if (size <= 499999) {
            return "Metropolis";
        }
        else {
            return "Megalopolis";
        }
    }
}
