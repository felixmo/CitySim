/*
 * Copyright (c) 2012 Felix Mo. All rights reserved.
 * 
 * CitySim is published under the terms of the MIT License. See the LICENSE file for more information.
 * 
 */

import java.util.Random;
import java.lang.Math;
import java.text.DecimalFormat;

/*
 * The terrain generator uses a random displacement fractal to generate noise that resemebles landforms 
 * Decimal values are returned and parsed into tiles
 *
 * Created with the help of Stack Overflow question:
 * http://stackoverflow.com/questions/5531019/perlin-noise-in-java/5532726
 * 
 * Question by James Thornton (http://stackoverflow.com/users/690007/jt78)
 * http://stackoverflow.com/questions/5531019/perlin-noise-in-java/5532726
 * 
 * Answer by Simon G. (http://stackoverflow.com/users/682965/simon-g) & edited by Andrew Thompson (http://stackoverflow.com/users/418556/andrew-thompson)
 * http://stackoverflow.com/questions/5531019/perlin-noise-in-java/5532726#5532726
 */

public class TerrainGenerator {
    /** Source of entropy */
    private Random rand_;

    /** Amount of roughness */
    float roughness_;

    /** Plasma fractal grid */
    private float[][] grid;

    /** Tile grid */
    private int[][] tiles;

    /** Generate a noise source based upon the midpoint displacement fractal.
     * 
     * @param rand The random number generator
     * @param roughness a roughness parameter
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public TerrainGenerator(Random rand, float roughness, int width, int height) {
        roughness_ = roughness / width;
        grid = new float[width][height];
        tiles = new int[width][height];
        rand_ = (rand == null) ? new Random() : rand;
        initialise();
        convertToTiles();
        remove();
        remove();
        remove();
    }

    public void initialise() {

        int xh = grid.length - 1;
        int yh = grid[0].length - 1;

        // set the corner points
        grid[0][0] = rand_.nextFloat() - 0.5f;
        grid[0][yh] = rand_.nextFloat() - 0.5f;
        grid[xh][0] = rand_.nextFloat() - 0.5f;
        grid[xh][yh] = rand_.nextFloat() - 0.5f;

        // generate the fractal
        generate(0, 0, xh, yh);
    }

    // Add a suitable amount of random displacement to a point
    private float roughen(float v, int l, int h) {
        return v + roughness_ * (float) (rand_.nextGaussian() * (h - l));
    }

    // generate the fractal
    private void generate(int xl, int yl, int xh, int yh) {
        int xm = (xl + xh) / 2;
        int ym = (yl + yh) / 2;
        if ((xl == xm) && (yl == ym)) return;

        grid[xm][yl] = 0.5f * (grid[xl][yl] + grid[xh][yl]);
        grid[xm][yh] = 0.5f * (grid[xl][yh] + grid[xh][yh]);
        grid[xl][ym] = 0.5f * (grid[xl][yl] + grid[xl][yh]);
        grid[xh][ym] = 0.5f * (grid[xh][yl] + grid[xh][yh]);

        float v = roughen(0.5f * (grid[xm][yl] + grid[xm][yh]), xl + yl, yh
                + xh);
        grid[xm][ym] = v;
        grid[xm][yl] = roughen(grid[xm][yl], xl, xh);
        grid[xm][yh] = roughen(grid[xm][yh], xl, xh);
        grid[xl][ym] = roughen(grid[xl][ym], yl, yh);
        grid[xh][ym] = roughen(grid[xh][ym], yl, yh);

        generate(xl, yl, xm, ym);
        generate(xm, yl, xh, ym);
        generate(xl, ym, xm, yh);
        generate(xm, ym, xh, yh);
    }

    /**
     * Convert the plasma fractal grid to CitySim tiles
     */
    private void convertToTiles() {

        DecimalFormat df = new DecimalFormat("#.#");

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {

                float value = 0.0f;
                try {
                    value = df.parse(df.format(grid[x][y])).floatValue();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (x < 3 || y < 3 || x > 200-4 || y > 200-4) {
                    // Set the outter tiles to be empty
                    tiles[x][y] = Tile.EMPTY;
                }
                else {
                    if (value <= 0.0f) {
                        // WATER
                        tiles[x][y] = Tile.WATER;
                    }
                    else {
                        // LAND
                        tiles[x][y] = Tile.GROUND;
                    }   
                }
            }
        }
    }

    private void remove() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                if (tiles[x][y] == Tile.WATER) {

                    // Check if tile is water

                    int sides = 0;  // # of sides touching land

                    // check UP for land
                    if (y+1 <= tiles[x].length-1) {
                        if (tiles[x][y+1] == Tile.GROUND) {
                            sides++;
                        }
                    }
                    // check DOWN for land
                    if (y-1 >= 0) {
                        if (tiles[x][y-1] == Tile.GROUND) {
                            sides++;
                        }
                    }

                    // check LEFT for land
                    if (x-1 >= 0) {
                        if (tiles[x-1][y] == Tile.GROUND) {
                            sides++;
                        }
                    }

                    // check RIGHT for land
                    if (x+1 <= tiles.length-1) {
                        if (tiles[x+1][y] == Tile.GROUND) {
                            sides++;
                        }
                    }

                    // Change tile to ground if the body of water is 1 tile wide
                    if (sides >= 3) {
                        tiles[x][y] = Tile.GROUND;
                    }
                }
                else if (tiles[x][y] == Tile.GROUND) {

                    int sides = 0;  // # of sides touching water

                    // check UP for water
                    if (y+1 <= tiles[x].length-1) {
                        if (tiles[x][y+1] == Tile.WATER) {
                            sides++;
                        }
                    }
                    // check DOWN for water
                    if (y-1 >= 0) {
                        if (tiles[x][y-1] == Tile.WATER) {
                            sides++;
                        }
                    }

                    // check LEFT for water
                    if (x-1 >= 0) {
                        if (tiles[x-1][y] == Tile.WATER) {
                            sides++;
                        }
                    }

                    // check RIGHT for water
                    if (x+1 <= tiles.length-1) {
                        if (tiles[x+1][y] == Tile.WATER) {
                            sides++;
                        }
                    }

                    // Change tile to ground if the landmass is 1 tile wide
                    if (sides >= 3) {
                        tiles[x][y] = Tile.WATER;
                    }
                }
            }
        }
    }

    // NOT IN USE
    /*
    private void smooth() {

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {

                if (tiles[x][y] == Tile.GROUND) {

                    boolean up = false, down = false, left = false, right = false;  // side touching water
                    //                     int sides = 0;  // # of sides touching water

                    // check UP for water
                    if (y+1 <= tiles[x].length-1) {
                        if (tiles[x][y+1] == Tile.WATER) {
                            up = true;   
                            //                             sides++;
                        }
                    }
                    // check DOWN for water
                    if (y-1 >= 0) {
                        if (tiles[x][y-1] == Tile.WATER) {
                            down = true;
                            //                             sides++;
                        }
                    }

                    // check LEFT for water
                    if (x-1 >= 0) {
                        if (tiles[x-1][y] == Tile.WATER) {
                            left = true; 
                            //                             sides++;
                        }
                    }

                    if (up && left) {
                        // top left corner

                        // beach_bottom_right
                        //                         tiles[x][y] = Tile.BEACH_TOP_LEFT;
                    }
                    else if (up && right) {
                        // top right corner

                        // beach_bottom_left
                        //                         tiles[x][y] = Tile.BEACH_TOP_LEFT;
                    }
                    else if (up) {
                        // top    

                        // bottom
                        tiles[x][y] = Tile.BEACH_TOP;
                    }
                    else if (down && left) {
                        // bottom left corner

                        // top_right
                        //                         tiles[x][y] = Tile.BEACH_BOTTOM_LEFT;
                    }
                    else if (down && right) {
                        // bottom right corner

                        // top_left
                        //                         tiles[x][y] = Tile.BEACH_BOTTOM_RIGHT;
                    }
                    else if (down) {
                        // bottom

                        // top
                        tiles[x][y] = Tile.BEACH_BOTTOM;
                    }
                    else if (left) {
                        // left

                        // right
                        tiles[x][y] = Tile.BEACH_RIGHT;
                    }
                    else if (right) {
                        // right

                        // left
                        tiles[x][y] = Tile.BEACH_LEFT;
                    }

                }
            }
        }
    }
    */
   
    /**
     * Dump out as a CSV
     */
    public void printAsCSV() {
        for(int i = 0;i < grid.length;i++) {
            for(int j = 0;j < grid[0].length;j++) {
                System.out.print(grid[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }

    /**
     * Convert to a Boolean array
     * @return the boolean array
     */
    public boolean[][] toBooleans() {
        int w = grid.length;
        int h = grid[0].length;
        boolean[][] ret = new boolean[w][h];
        for(int i = 0;i < w;i++) {
            for(int j = 0;j < h;j++) {
                ret[i][j] = grid[i][j] < 0;
            }
        }
        return ret;
    }

    /*
     * ACCESSORS
     */
    public int[][] tiles() {
        return this.tiles;
    }
}