package factorii.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.spi.ResourceBundleControlProvider;

import factorii.GameState;

public class WorldBuilder {
    private int width;
    private int height;

    private WorldBuilderTerrainGenerator terrainGen;
    private WorldBuilderIslandFilterer islandProcesser;

    public WorldBuilder (int width, int height) {
        this.width = width;
        this.height = height;

        terrainGen = new WorldBuilderTerrainGenerator();
        islandProcesser = new WorldBuilderIslandFilterer();
    }

    public World generateDefaultWorld () {
        Tile[][] terrain;
        Tile[][] resources;

        terrain = terrainGen.generateDefaultWorldBase(width, height);
        resources = terrainGen.generateDefaultWorldResources(width, height, terrain);
        ArrayList<IslandStats> islandComposition = new ArrayList<IslandStats>();

        // First we select for a certain combinatino of islands
        while (true) {
            terrain = terrainGen.generateDefaultWorldBase(width, height);
            resources = terrainGen.generateDefaultWorldResources(width, height, terrain);

            islandComposition = islandProcesser.filterIslands(terrain, resources);
            Collections.sort(islandComposition, new Comparator<IslandStats>(){
                public int compare (IslandStats a, IslandStats b) {
                    return b.getIslandSize() - a.getIslandSize();
                }
            });

            if (islandComposition.size() >= 6 &&
                islandComposition.get(0).getIslandSize() > 5000 &&
                islandComposition.get(1).getIslandSize() > 2000 &&
                islandComposition.get(2).getIslandSize() > 1000 &&
                islandComposition.get(3).getIslandSize() <= 1000) {
                    break;
            }
        }


        // Then we adjust resources so there's a progression
        /*
            Starting Island should have stone, tree & sand
            Next biggest island should be just stone & ores (except coal)
            Next biggest island should be mainly coal

            Then the tiny island all specialize, cycling to be the major resources
        */
        IslandStats startingIsland = islandComposition.get(0);
        for (int[] cord : startingIsland.getCords()) {
            int x = cord[0];
            int y = cord[1];

            switch (resources[x][y]) {
                case ORE_COAL:
                    resources[x][y] = Tile.TREE;
                    break;

                case ORE_COPPER:
                case ORE_IRON:
                    resources[x][y] = Tile.STONE;
                    break;

                case SAND:
                    resources[x][y] = Tile.EMPTY;
                    break;
            }
        }

        // Copper and Coal
        IslandStats secondIsland = islandComposition.get(1);
        for (int[] cord : secondIsland.getCords()) {
            int x = cord[0];
            int y = cord[1];

            switch (resources[x][y]) {
                case TREE:
                    resources[x][y] = Tile.ORE_COAL;
                    break;

                case ORE_IRON:
                case STONE:
                    resources[x][y] = Tile.ORE_COPPER;
                    break;

                case SAND:
                    resources[x][y] = Tile.EMPTY;
                    break;
            }
        }

        // Iron & Sand
        IslandStats thirdIsland = islandComposition.get(2);
        for (int[] cord : thirdIsland.getCords()) {
            int x = cord[0];
            int y = cord[1];

            switch (resources[x][y]) {
                case STONE:
                case ORE_COPPER:
                    resources[x][y] = Tile.ORE_IRON;
                    break;

                case TREE:
                    resources[x][y] = Tile.EMPTY;
                    break;
            }
        }

        // Everything else is sand, tree, and stone
        int resourceInd = 0;
        Tile[] resourceCycle = new Tile[] {Tile.SAND, Tile.TREE, Tile.STONE};
        for (int i=3; i<islandComposition.size(); i++) {
            Tile replacementTile = resourceCycle[resourceInd];

            for (int[] cord : islandComposition.get(i).getCords()) {
                int x = cord[0];
                int y = cord[1];

                switch (resources[x][y]) {
                    case EMPTY:
                        break;

                    default:
                        resources[x][y] = replacementTile;
                        break;
                }
            }

            resourceInd++;
            resourceInd %= 3;
        }


        // As a last aside, it is necessary to find a spawn location
        ArrayList<int[]> possibleStarts = startingIsland.getCords();
        int maxIndex = possibleStarts.size();
        int[] startCord = possibleStarts.get((int) (Math.random() * maxIndex * 0.9) + 1);
        resources[startCord[0]][startCord[1]] = Tile.EMPTY;
        GameState.player.setSpawn(startCord[0], startCord[1]);

        return new World(terrain, resources);
    }

    public World generateDirtWorld () {
        Tile[][] terrain = terrainGen.generateDirtBase(width, height);
        Tile[][] resources = terrainGen.generateBlankGrid(width, height);

        return new World(terrain, resources);
    }

    public World generatePocketWorld () {
        Tile[][] terrain = terrainGen.generatePocketWorldBase(width, height);
        Tile[][] resources = terrainGen.generatePocketWorldResources(width, height, terrain);

        return new World(terrain, resources);
    }

}
