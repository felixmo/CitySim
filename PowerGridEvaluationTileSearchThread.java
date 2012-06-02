/**
 * Write a description of class PowerGridEvaluationTileSearchThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PowerGridEvaluationTileSearchThread extends CSThread
{
    private Tile tile;

    public PowerGridEvaluationTileSearchThread(Tile tile) {
        super("");
        this.tile = tile;
    }
    
    public void run() {
        PowerGrid.searchTile(this.tile);
    }
}
