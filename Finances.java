import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.text.DecimalFormat;

/**
 * Write a description of class Finances here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Finances 
{

    private static float rate = 1.10f; // Default rate of 10% tax
    private static int lastTaxCollection = 0;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0");

    private static int taxCollectionAtRate(float aRate) {

        float tax = 0.0f;

        // pop. * tax rate
        tax += ((Integer)Data.cityStats().get(Data.CITYSTATS_POPULATION)).intValue() * aRate;

        // Tax commercial zones
        tax += DataSource.getInstance().totalCommercialCapacity() * aRate;        

        // Tax industrial zones
        tax += DataSource.getInstance().totalIndustrialCapacity() * aRate;

        // # police stations * 100
        tax -= Data.zonesMatchingCriteria("zone = " + PoliceStation.TYPE_ID).length * 100;

        // # fire statoins * 100
        tax -= Data.zonesMatchingCriteria("zone = " + FireStation.TYPE_ID).length * 100;

        tax -= Data.zonesMatchingCriteria("zone = " + CoalPowerPlant.TYPE_ID).length * 300;

        tax -= Data.zonesMatchingCriteria("zone = " + NuclearPowerPlant.TYPE_ID).length * 500;

        // # roads
        tax -= ((Integer)Data.roadStats().get(Data.ROADSTATS_STREETCOUNT)).intValue();

        return (int)tax;
    }

    public static void collectTaxesAndDeductExpenses() {

        int collected = taxCollectionAtRate(rate);

        CSLogger.sharedLogger().info("Collected $" + collected + " in taxes this year");

        Cash.add(collected);

        lastTaxCollection = collected;
    }

    public static void showTaxRateDialog() {

        City.getInstance().enableOverlay();

        SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    SpinnerModel model = new SpinnerNumberModel((int)((rate-1) * 100), 1, 100, 1);
                    JSpinner spinner = new JSpinner(model);
                    Dimension d = spinner.getSize();
                    d.width = 20;
                    spinner.setSize(d);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.add(new JLabel("Tax collected last year: $" + (lastTaxCollection > 0 ? DECIMAL_FORMAT.format(lastTaxCollection).toString() : "n/a")));
                    panel.add(new JLabel(" "));
                    panel.add(new JLabel("Choose the new tax rate (%):"));
                    panel.add(spinner);

                    final JLabel projection = new JLabel("Projected collection next year: $" + DECIMAL_FORMAT.format(taxCollectionAtRate(rate)).toString());
                    panel.add(projection);

                    spinner.addChangeListener(new ChangeListener() {
                            public void stateChanged(ChangeEvent e) {
                                float value = (new Integer(((JSpinner)e.getSource()).getValue().toString()).floatValue() / 100.0f) + 1.0f;
                                projection.setText("Projected collection next year: $" + DECIMAL_FORMAT.format(taxCollectionAtRate(value)).toString());
                            }
                        });

                    int selection = JOptionPane.showConfirmDialog(new Frame(), panel, "Tax rate", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (selection == JOptionPane.OK_OPTION) {

                        City.getInstance().removeOverlay();

                        float value = new Integer(model.getValue().toString()).floatValue() + 0.01f;
                        rate = (value / 100.0f) + 1.0f;
                        CSLogger.sharedLogger().info("Changed global tax rate to " + (int)((rate-1)*100) + "%");
                    }
                    else if (selection == JOptionPane.CANCEL_OPTION) {
                        City.getInstance().removeOverlay();
                    }
                }
            });
    }

    public static int taxRate() {
        return (int)((rate-1)*100);
    }

    public static String taxRateString() {
        return (int)((rate-1)*100) + "%";
    }

    public static void setTaxRate(int value) {
        rate = (((float)value + 0.01f) / 100.0f) + 1.0f;
    }

    public static int lastTaxCollection() {
        return lastTaxCollection;
    }

    public static void setLastTaxCollection(int amount) {
        lastTaxCollection = amount;
    }
}
