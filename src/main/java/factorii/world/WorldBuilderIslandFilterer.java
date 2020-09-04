package factorii.world;

import java.util.ArrayList;

/*
This class is responsible for selecting a spawning location.

*/

public class WorldBuilderIslandFilterer {

    private int width;
    private int height;
    private Tile[][] terrain;
    private Tile[][] resources;
    private boolean[][] exploredYet;

    private ArrayList<IslandStats> islandProperties; 
    private int currentGroup;
   
    public Tile[][] getTileGroupColoring () {
        Tile[][] returnTerrain = new Tile[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                returnTerrain[x][y] = Tile.WATER;
            }
        }

        Tile[] drawingTiles = new Tile[] {
                Tile.GROUND, 
                Tile.SAND, 
                Tile.STONE, 
                Tile.ORE_COPPER, 
                Tile.ORE_IRON, 
                Tile.MINING_DRILL,
                Tile.AUTO_MINING_UPGRADE,
                Tile.ITEM_TUBE_STONE,
                Tile.ITEM_TUBE_GLASS
        };

        for (int i=0; i<islandProperties.size(); i++) {
            IslandStats cord_group = islandProperties.get(i);
            Tile thisTile = drawingTiles[i];
            for (int[] cord : cord_group.getCords()) {
                returnTerrain[cord[0]][cord[1]] = thisTile;
            }
        }

        return returnTerrain;
    }

    public ArrayList<IslandStats> getIslandProperties () {
        if (islandProperties == null)
            throw new NullPointerException("Make sure to call .filterIslands(...) before .getIslandProperties()");

        return islandProperties;
    }

    public ArrayList<IslandStats> filterIslands (Tile[][] terrain, Tile[][] resources) {
        this.terrain = terrain;
        this.resources = resources;

        // Debug # of islands
        width = terrain.length;
        height = terrain[0].length;

        islandProperties = new ArrayList<IslandStats>();

        exploredYet = new boolean[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                exploredYet[x][y] = false;
            }
        }

        currentGroup = 0;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (terrain[x][y] == Tile.GROUND &&
                    exploredYet[x][y] == false) {
                        islandProperties.add(new IslandStats());
                        markIsland(x, y);
                        currentGroup++;
                }
            }
        }

        return islandProperties;
    }

    // TODO: BUG: This leads to a stack overflow with very large islands
    private void markIsland (int x, int y) {
        // It would be cleaner to check for out of bounds, but
        // it's almost a guarantee that the edge will be water
        // and thus not reached by this call.
        // if (x < 0 || x >= width || y < 0 || y >= height)
        //     return;

        if (exploredYet[x][y] == true ||
            terrain[x][y] == Tile.WATER)
                return;

        islandProperties.get(currentGroup).addNewCoordinate(new int[] {x, y});
        Tile resource = resources[x][y];
        if (resource != null)
            islandProperties.get(currentGroup).addNewTile(resource);
        exploredYet[x][y] = true;
        markIsland(x-1, y);
        markIsland(x+1, y);
        markIsland(x, y-1);
        markIsland(x, y+1);
    }

}
