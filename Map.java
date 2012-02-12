import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.lang.Math;

/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map extends Actor
{

    private int moveSpeed = 10;

    // map of 100x100 tiles
    private final static int tileBuffer = 2; // cells | UNIMPLEMENTED
    private final int cityColumns = 100; // cells | X
    private final int cityRows = 100; // cells | Y
    private Rectangle cityRect = new Rectangle(new Point(0, 0), (cityColumns * Tile.size), (cityRows * Tile.size));

    private ArrayList<ArrayList<Tile>> map; 

    private Rectangle viewRect = new Rectangle(new Point(0, 0), 1024 + (tileBuffer * Tile.size), 768 + (tileBuffer * Tile.size));
    private GreenfootImage view = new GreenfootImage(viewRect.width(), viewRect.height());

    public Map() {

        generateCity();
        setImage(view);
        initalDraw();
    }

    public void act() 
    {

        if (Greenfoot.isKeyDown("w")) {
            if (viewRect.origin().y() > cityRect.origin().y()) {
                mapDidMove(new Point(0, (moveSpeed * -1)));
            }
        }
        else if (Greenfoot.isKeyDown("s")) {
            if (viewRect.origin().y() + viewRect.height() < cityRect.width()) {
                mapDidMove(new Point(0, moveSpeed));
            }
        }
        else if (Greenfoot.isKeyDown("a")) {
            if (viewRect.origin().x() > cityRect.origin().x()) {
                mapDidMove(new Point(moveSpeed, 0));
            }
        }
        else if (Greenfoot.isKeyDown("d")) {
            if (viewRect.origin().x() + viewRect.width() < cityRect.width()) {
                mapDidMove(new Point((moveSpeed * -1), 0));
            }
        }
    }    

    // * Helper methods *

    private Point cellForCoordinate(Point coord) {
        return new Point(((coord.x() - (coord.x() % Tile.size)) / Tile.size), ((coord.y() - (coord.y() % Tile.size)) / Tile.size));
    }

    // * END of helper methods *

    private void generateCity() {

        System.out.print("Began generating city...");
        long startTime = System.currentTimeMillis();

        // init. X 
        map = new ArrayList<ArrayList<Tile>>(cityColumns);

        // init. Y for each X
        for (int i = 0; i < cityColumns; i++) {
            map.add(new ArrayList<Tile>(cityRows));
        }

        for (int x = 0; x < cityColumns; x++) {
            for (int y = 0; y < cityRows; y++) {
                map.get(x).add(new Tile(Greenfoot.getRandomNumber(4)+1));
            }
        }

        System.out.println("completed in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private void initalDraw() {

        System.out.print("Began inital draw...");
        long startTime = System.currentTimeMillis();

        int x = 0;
        int y = 0;

        for (int col = viewRect.origin().x(); col < (viewRect.width() / Tile.size); col++) {

            x = (col * Tile.size);

            for (int row = viewRect.origin().y(); row < (viewRect.height() / Tile.size)+1; row++) {

                view.drawImage(map.get(col).get(row).image(), x, y);
                y = (row * Tile.size);
            }
        }

        System.out.println("completed in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private void mapDidMove(Point offset) {

        viewRect.origin().setX(viewRect.origin().x() - offset.x());
        viewRect.origin().setY(viewRect.origin().y() + offset.y());

        int x = viewRect.origin().x();
        int y = viewRect.origin().y();

        view.clear();

        for (int col = cellForCoordinate(viewRect.origin()).x(); col < ((viewRect.width() + viewRect.origin().x()) / Tile.size); col++) {
            for (int row = cellForCoordinate(viewRect.origin()).y(); row < ((viewRect.height() + viewRect.origin().y()) / Tile.size)+1; row++) {

                //                 System.out.println("Drawing tile of type " + map.get(col).get(row).type() + " at (" + col + ", " + row + ") on map, (" + x + ", " + y + ") in view");

                view.drawImage(map.get(col).get(row).image(), x, y);

                x = (col * Tile.size) - viewRect.origin().x();
                y = (row * Tile.size) - viewRect.origin().y();

//                 System.out.print(viewRect.origin().toString());
//                 System.out.print(" | " + col + ", " + row + " | ");
//                 System.out.println(x + ", " + y);
            }
        }
    }
}
