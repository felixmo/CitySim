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
 * MainMenu
 * CitySim
 * v0.1
 *
 * Created by Felix Mo on 03-06-2012
 *
 * Main menu
 *
 */

public class MainMenu extends World
{

    private MouseInfo mouseInfo;

    private static final Overlay overlay = new Overlay();
    private static boolean showingInstructions = false;

    public MainMenu() {

        super(1024, 768, 1, false);   
    }

    public void act() {

        // Update mouse info if mouse has moved
        if (Greenfoot.getMouseInfo() != null) {
            mouseInfo = Greenfoot.getMouseInfo();
        }

        if (Greenfoot.mouseClicked(null)) {
            if (mouseInfo != null) {
                int x = mouseInfo.getX();
                int y = mouseInfo.getY();

                if (!showingInstructions) {

                    // New city
                    if (x >= 420 && x <= 610 && y >= 310 && y <= 360) {

                        enableOverlay();

                        new Thread() {
                            public void run() {
                                String response = JOptionPane.showInputDialog(null,
                                        "What will your city be named?",
                                        "Enter the name of your city",
                                        JOptionPane.QUESTION_MESSAGE);

                                JFileChooser chooser = new JFileChooser(); 
                                chooser.setCurrentDirectory(new java.io.File("."));
                                chooser.setDialogTitle("Select the folder in which to save the city");
                                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                chooser.setAcceptAllFileFilterUsed(false);
                                //    
                                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 

                                    removeOverlay();
                                    setBackground("mainmenu_loading.png");

                                    City city = new City(response, chooser.getSelectedFile().toURI().toString());
                                    Greenfoot.setWorld(city);
                                }
                                else {
                                    removeOverlay();
                                    System.out.println("No selection ");
                                }
                            }
                        }.start();
                    }
                    // Load city
                    if (x >= 410 && x <= 610 && y >= 420 && y <= 460) {
                        enableOverlay();

                        new Thread() {
                            public void run() {

                                JFileChooser chooser = new JFileChooser(); 
                                chooser.setCurrentDirectory(new java.io.File("."));
                                chooser.setDialogTitle("Select the city file");
                                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                chooser.setAcceptAllFileFilterUsed(false);
                                chooser.setFileFilter(new FileNameExtensionFilter("CitySim data", "db"));
                                //    
                                if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 

                                    removeOverlay();
                                    setBackground("mainmenu_loading.png");

                                    City city = new City(null, chooser.getSelectedFile().toURI().toString());
                                    Greenfoot.setWorld(city);
                                }
                                else {
                                    removeOverlay();
                                    System.out.println("No selection ");
                                }
                            }
                        }.start();
                    }

                    // Instructions
                    if (x >= 385 && x <= 645 && y >= 525 && y <= 565) {
                        showingInstructions = true;
                        setBackground("instructions.png");
                    }

                }
                else {
                    showingInstructions = false;
                    setBackground("mainmenu.png");
                }
            }
        }
    }

    public void enableOverlay() {
        addObject(MainMenu.overlay, 512, 384);
    }

    public void removeOverlay() {
        removeObject(MainMenu.overlay);
    }
}
