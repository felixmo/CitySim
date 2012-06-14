/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Frame;
import javax.swing.JOptionPane;
import java.util.ArrayList;

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
    private static ArrayList<String> messages = null;
    private static boolean cycle = false;

    public static void simulate() {

        int score = City.getInstance().score();

        if (Date.months() <= 6 && Date.years() == 0 && Population.size() == 0) return;

        if (messages == null) {
            messages = new ArrayList();
        }

        if (!cycle) {

            messages.clear();
            ratioImpact = 0;
            powerImpact = 0;
            unemploymentImpact = 0;
            taxImpact = 0;

            // Ratio of I to C
            int iCount = Data.industrialZones().length;
            int cCount = Data.commercialZones().length;
            if (iCount == 0) {
                messages.add("There aren't any commercial zones!");
            }
            if (cCount == 0) {
                messages.add("There aren't any industrial zones!");
            }

            if (iCount > 0 && cCount > 0) {
                
                int ratio = (int)(((float)iCount / cCount) * 100);
                if (ratio <= 75) {
                    // Industrial zones > commercial zones
                    ratioImpact = -15;
                    messages.add("There aren't enough commercial zones for the industrial zones to supply.");
                }
                else if (ratio >= 125) {
                    // Commerical zones > Industrial zones
                    ratioImpact = -15;
                    messages.add("There aren't enough industrial zones to supply the commerical zones.");
                }
                else {
                    ratioImpact = 10;
                }

                score += ratioImpact;
            }

            // No power
            int unpowered = Data.zonesMatchingCriteria("powered = -1").length;
            if (unpowered > 0) {
                powerImpact = -20;
                messages.add("Some areas in your city do not have power. Check the power plants and power lines.");
            }
            else {
                powerImpact = 15;
            }

            score += powerImpact;

            // Unemployment
            int unemployed = Population.size() - (DataSource.getInstance().totalIndustrialCapacity() + DataSource.getInstance().totalCommercialCapacity());
            if (unemployed > 0) {
                int unemploymentRate = (int)((unemployed / Population.size()) * 100);
                if (unemploymentRate >= 10) {

                    unemploymentImpact = -25;
                    messages.add("Many citizens are unemployed. Consider building more industrial or commerical zones.");
                }
                else {
                    unemploymentImpact = 20;
                }

                score += unemploymentImpact;
            }

            // High taxes (20% + )
            if (Taxation.rate() >= 20) {

                taxImpact = -20;
                messages.add("Citizens find the tax rate too high!");
            }
            else {
                taxImpact = 10;
            }

            score += taxImpact;

            City.getInstance().setScore(score);
        }

        cycle = !cycle;
    }

    public static void dialog() {

        City.getInstance().enableOverlay();

        SwingUtilities.invokeLater( new Runnable() {
                public void run() {

                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    if (messages != null) {
                        for (String message : messages) {
                            panel.add(new JLabel(message));
                            panel.add(new JLabel(" "));
                        }
                    }

                    panel.add(new JLabel("Score: " + City.getInstance().score() + "%"));

                    int selection = JOptionPane.showConfirmDialog(new Frame(), panel, "Score", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (selection == JOptionPane.OK_OPTION) {
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
