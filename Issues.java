import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 * Write a description of class Issues here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Issues  
{
    private static int ratioImpact = 0;
    private static int powerImpact = 0;
    private static int unemploymentImpact = 0;
    private static int taxImpact = 0;

    public static void simulate() {

        int score = City.getInstance().score();

        // Ratio of I to C
        int iCount = Data.industrialZones().length;
        int cCount = Data.commercialZones().length;
        int ratio = (int)(((float)iCount / cCount) * 100);
        if (ratio <= 75) {
            // Industrial zones > commercial zones
            if (ratioImpact == -10) {
                ratioImpact = 0;
            }
            else {
                ratioImpact = -10;
            }
        }
        else if (ratio >= 125) {
            // Commerical zones > Industrial zones

            if (ratioImpact == -10) {
                ratioImpact = 0;
            }
            else {
                ratioImpact = -10;
            }
        }
        else {

            if (ratioImpact == 10) {
                ratioImpact = 0;
            }
            else {
                ratioImpact = 10;
            }
        }

        score += ratioImpact;

        // No power
        //         int tlZones = Data.zones().length - Data.powerPlants().length;
        int unpowered = Data.zonesMatchingCriteria("powered = -1").length;
        if (unpowered > 0) {
            if (powerImpact == -10) {
                powerImpact = 0;
            }
            else {
                powerImpact = -10;
            }
        }
        else {
            if (powerImpact == 10) {
                powerImpact = 0;
            }
            else {
                powerImpact = 10;
            }
        }

        score += powerImpact;

        // Unemployment
        int unemployed = Population.size() - (DataSource.getInstance().totalIndustrialCapacity() + DataSource.getInstance().totalCommercialCapacity());
        int unemploymentRate = (int)((unemployed / Population.size()) * 100);
        if (unemploymentRate >= 10) {

            if (unemploymentImpact == -20) {
                unemploymentImpact = 0;
            }
            else {
                unemploymentImpact = -20;
            }
        }
        else {

            if (unemploymentImpact == 10) {
                unemploymentImpact = 0;
            }
            else {
                unemploymentImpact = 10;
            }
        }

        score += unemploymentImpact;

        // High taxes (20% + )
        if (Finances.taxRate() >= 20) {

            if (taxImpact == -20) {
                taxImpact = 0;
            }
            else {
                taxImpact = -20;
            }
        }
        else {
            if (taxImpact == 10) {
                taxImpact = 0;
            }
            else {
                taxImpact = 10;
            }
        }

        score += taxImpact;

        City.getInstance().setScore(score);
    }

    public static void dialog() {
        City.getInstance().enableOverlay();

        SwingUtilities.invokeLater( new Runnable() {
                public void run() {

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.add(new JLabel("Industrial / commercial impact: " + ratioImpact));
                    panel.add(new JLabel("Power impact: " + powerImpact));
                    panel.add(new JLabel("Unemployment impact: " + unemploymentImpact));
                    panel.add(new JLabel("Tax impact: " + taxImpact));
                    panel.add(new JLabel("Score: " + City.getInstance().score()));

                    int selection = JOptionPane.showConfirmDialog(new Frame(), panel, "Scoring", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (selection == JOptionPane.OK_OPTION) {

                        City.getInstance().removeOverlay();
                    }
                    else {
                        City.getInstance().removeOverlay();
                    }
                }
            });
    }

    /*
     * ACCESSORS *
     */

    public static int ratioImpact() {
        return ratioImpact();
    }

    public static int powerImpact() {
        return powerImpact;
    }

    public static int unemploymentImpact() {
        return unemploymentImpact;
    }

    public static int taxImpact() {
        return taxImpact;
    }
}
