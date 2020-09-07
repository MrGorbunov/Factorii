package factorii.world;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

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
                Tile.ITEM_TUBE_GLASS,
                Tile.ORE_COAL,
                Tile.CHEST,
                Tile.WORKBENCH,
                Tile.STEEL_FORGE
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

    private void markIsland (int x, int y) {
        // With only one queue, the search would beelin along a single path & fill up (depth-first)
        // by using two queue & alternating, the search goes in waves and spawns out (bredth-first)
        Queue<int[]> cordsToCheckA = new ArrayBlockingQueue<int[]>(1000, true);
        Queue<int[]> cordsToCheckB = new ArrayBlockingQueue<int[]>(1000, true);
        cordsToCheckA.add(new int[] {x, y});

        while (cordsToCheckA.size() != 0 || cordsToCheckB.size() != 0) {
            while (cordsToCheckA.size() != 0) {
                int[] currentCord = cordsToCheckA.remove();
                int curX = currentCord[0];
                int curY = currentCord[1];

                if (terrain[curX][curY] == Tile.WATER ||
                    exploredYet[curX][curY])
                        continue;

                islandProperties.get(currentGroup).addNewCoordinate(new int[] {curX, curY});
                Tile resource = resources[curX][curY];
                if (resource != null)
                    islandProperties.get(currentGroup).addNewTile(resource);
                exploredYet[curX][curY] = true;

                cordsToCheckB.add(new int[] {curX - 1, curY});
                cordsToCheckB.add(new int[] {curX + 1, curY});
                cordsToCheckB.add(new int[] {curX, curY - 1});
                cordsToCheckB.add(new int[] {curX, curY + 1});
            }

            while (cordsToCheckB.size() != 0) {
                int[] currentCord = cordsToCheckB.remove();
                int curX = currentCord[0];
                int curY = currentCord[1];

                if (terrain[curX][curY] == Tile.WATER ||
                    exploredYet[curX][curY])
                        continue;

                islandProperties.get(currentGroup).addNewCoordinate(new int[] {curX, curY});
                Tile resource = resources[curX][curY];
                if (resource != null)
                    islandProperties.get(currentGroup).addNewTile(resource);
                exploredYet[curX][curY] = true;

                cordsToCheckA.add(new int[] {curX - 1, curY});
                cordsToCheckA.add(new int[] {curX + 1, curY});
                cordsToCheckA.add(new int[] {curX, curY - 1});
                cordsToCheckA.add(new int[] {curX, curY + 1});
            }
        }
    }

}
