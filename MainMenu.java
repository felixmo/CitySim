/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 'MainMenu' is the view, and view controller for the initial menu in the game.
 * 
 * @author Felix Mo
 * @version v1.0
 * @since 2012-06-13
 */

public class MainMenu extends World
{

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTANTS *
     */
    private static final Overlay overlay = new Overlay();   // overlay to grey-out the view

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * REFERENCES *
     */
    private MouseInfo mouseInfo;                           // Reference to the MouseInfo provided by Greenfoot
    private boolean showingInstructions = false;           // States wether the instructions are being shown

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * CONSTRUCTORS *
     */

    /**
     * Constructs a MainMenu.
     */
    public MainMenu() {
        super(1024, 768, 1, false);   
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * GREENFOOT METHODS *
     */

    /**
     * The main menu's behaviour. 
     */
    public void act() {

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (Greenfoot.mouseClicked(null)) {
            if (mouseInfo != null) {

                // Cursor position
                int x = mouseInfo.getX();
                int y = mouseInfo.getY();

                if (!showingInstructions) {
                    // If not showing instructions, then allow access to the buttons

                    // "New city"
                    if (x >= 420 && x <= 610 && y >= 310 && y <= 360) {

                        showOverlay();

                        // Ask for city name and for directory to save to
                        new Thread() {
                            public void run() {

                                // Text input dialog for city name
                                String response = JOptionPane.showInputDialog(null,
                                        "What will your city be named?",
                                        "Enter the name of your city",
                                        JOptionPane.QUESTION_MESSAGE);

                                // File chooser dialog for directory to save to
                                JFileChooser chooser = new JFileChooser(); 
                                chooser.setCurrentDirectory(new java.io.File("."));
                                chooser.setDialogTitle("Select the folder in which to save the city");
                                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                chooser.setAcceptAllFileFilterUsed(false);

                                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
                                    // IF user has chosen a location

                                    // Remove overlay and show loading screen
                                    hideOverlay();
                                    setBackground("mainmenu_loading.png");

                                    // Create a database at the specified path and change world over to 'City'
                                    City city = new City(response, chooser.getSelectedFile().toURI().toString());
                                    Greenfoot.setWorld(city);
                                }
                                else {
                                    // IF user has NOT chosen a location

                                    hideOverlay();
                                }
                            }
                        }.start();
                    }

                    // "Load city"
                    if (x >= 410 && x <= 610 && y >= 420 && y <= 460) {

                        showOverlay();

                        new Thread() {
                            public void run() {

                                // File chooser for database file
                                JFileChooser chooser = new JFileChooser(); 
                                chooser.setCurrentDirectory(new java.io.File("."));
                                chooser.setDialogTitle("Select the city file");
                                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                chooser.setAcceptAllFileFilterUsed(false);
                                // Filter to only accept ".db" files
                                chooser.setFileFilter(new FileNameExtensionFilter("CitySim data", "db"));

                                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
                                    // IF user has chosen a file

                                    // Remove overlay and show loading screen
                                    hideOverlay();
                                    setBackground("mainmenu_loading.png");

                                    // Load database and change world over to 'City'
                                    City city = new City(null, chooser.getSelectedFile().toURI().toString());
                                    Greenfoot.setWorld(city);
                                }
                                else {
                                    // IF user has NOT chosen a location

                                    hideOverlay();
                                }
                            }
                        }.start();
                    }

                    // Instructions
                    if (x >= 385 && x <= 645 && y >= 525 && y <= 565) {
                        // Show instructions

                        showingInstructions = true;
                        setBackground("instructions.png");
                    }

                }
                else {
                    // Allow user to click anywhere on screen while instructions are being displaying to dismiss it

                    showingInstructions = false;
                    setBackground("mainmenu.png");
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------
    /*
     * OVERLAY * 
     */

    /**
     * Shows an overlay to fade out the screen.
     */
    public void showOverlay() {
        addObject(MainMenu.overlay, 512, 384);
    }

    /**
     * Hides the overlay.
     */
    public void hideOverlay() {
        removeObject(MainMenu.overlay);
    }
}
