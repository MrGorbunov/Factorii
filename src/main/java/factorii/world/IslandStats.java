package factorii.world;

import java.util.ArrayList;

public class IslandStats {
    private ArrayList<int[]> coordinates;

    private int amountWood;
    private int amountStone;
    private int amountSand;

    private int amountOreCoal;
    private int amountOreCopper;
    private int amountOreIron;
    
    public IslandStats () {
        coordinates = new ArrayList<int[]>();

        amountWood = 0;
        amountStone = 0;
        amountSand = 0;

        amountOreCoal = 0;
        amountOreCopper = 0;
        amountOreIron = 0;
    }

    public void addNewCoordinate (int[] coordinate) {
        coordinates.add(coordinate);
    }

    /**
     * There are internal counters for different quantities
     */
    public void addNewTile (Tile tile) {
        switch (tile) {
            case TREE:  amountWood++; break;
            case STONE: amountStone++; break;
            case SAND:  amountSand++; break;

            case ORE_COAL:   amountOreCoal++; break;
            case ORE_COPPER: amountOreCopper++; break;
            case ORE_IRON:   amountOreIron++; break;
        }
    }

    public ArrayList<int[]> getCords () {
        return coordinates;
    }

    public int getIslandSize () {
        return coordinates.size();
    }

    public int getTotalOreAmount () {
        return amountOreCoal + amountOreCopper + amountOreIron;
    }

}
