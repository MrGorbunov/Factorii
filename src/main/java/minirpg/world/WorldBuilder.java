package minirpg.world;

public class WorldBuilder {
    private Tile[][] terrain;
    private Tile[][] interactables;
    private int width;
    private int height;

    public WorldBuilder (int width, int height) {
        this.width = width;
        this.height = height;
        terrain = new Tile[width][height];
        interactables = new Tile[width][height];
    }

    private void clearWorld () {
        terrain = new Tile[width][height];
        interactables = new Tile[width][height];
    }





    private void randomTerrain () {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (Math.random() < 0.5)
                    terrain[x][y] = Tile.DIRT;
                else
                    terrain[x][y] = Tile.WATER;
            }
        }
    }

    private void smoothTerrain () {
        Tile[][] smootherTiles = new Tile[width][height];
        
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {

                int sumOfNeighbours = 0;
                for (int dx=-1; dx<=1; dx++) {
                    for (int dy=-1; dy<=1; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        if (checkX < 0 || checkX >= width || checkY < 0 || checkY >= height)
                            continue;

                        if (terrain[x+dx][y+dy] == Tile.DIRT)
                            sumOfNeighbours++;
                    }
                }

                // Bias towards dirt
                if (sumOfNeighbours > 4) {
                    smootherTiles[x][y] = Tile.DIRT;
                } else {
                    smootherTiles[x][y] = Tile.WATER;
                }
            }
        }

        terrain = smootherTiles;
    }





    private void fillInteractablesEmpty () {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                interactables[x][y] = Tile.EMPTY;
            }
        }
    }

    /**
     * Places trees randomly into the interactables array,
     * unless there is water there (from terrain array)
     */
    private void randomTrees (double percentFill) {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (terrain[x][y] != Tile.WATER &&
                    Math.random() < percentFill)
                    interactables[x][y] = Tile.TREE;
            }
        }
    }

    private void randomOres (double percentFill) {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (terrain[x][y] != Tile.WATER &&
                    Math.random() < percentFill)
                    interactables[x][y] = Tile.ORE_COAL;
            }
        }
    }





    public World generateDefaultWorld () {
        clearWorld();
        randomTerrain();
        
        int smoothingIterations = 10;
        for (int i=0; i<smoothingIterations; i++)
            smoothTerrain();
        
        fillInteractablesEmpty();
        randomTrees(0.05);
        randomOres(0.1);


        return new World(terrain, interactables);
    }
}