import java.text.DecimalFormat;

/**
 * Write a description of class Population here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Population  
{
    private static int size = 0;
    private static int change = 0;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0");

    public static int size() {
        return size;
    }

    public static void initialSet(int value) {
        size = value;
    }

    public static void set(int value) {
        change = (value - size);
        size = value;
    }

    public static String asString() {
        return DECIMAL_FORMAT.format(size).toString() + (" (" + (change < 0 ? "" : "+") + DECIMAL_FORMAT.format(change) + ")");
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
