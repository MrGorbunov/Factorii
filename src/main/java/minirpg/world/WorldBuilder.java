package minirpg.world;

public class WorldBuilder {
    private Tile[][] tiles;
    private int width;
    private int height;

    public WorldBuilder (int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }

    private void clearWorld () {
        tiles = new Tile[width][height];
    }

    private void populateRandomly () {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (Math.random() < 0.5) {
                    tiles[x][y] = Tile.FLOOR;
                } else {
                    tiles[x][y] = Tile.WALL;
                }
            }
        }
    }

    private void smoothWorld () {
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

                        if (tiles[x+dx][y+dy] == Tile.FLOOR)
                            sumOfNeighbours++;
                    }
                }

                if (sumOfNeighbours > 4) {
                    smootherTiles[x][y] = Tile.FLOOR;
                } else {
                    smootherTiles[x][y] = Tile.WALL;
                }
            }
        }

        tiles = smootherTiles;
    }



    //
    // World Types
    //
    public World generateCaveWorld () {
        clearWorld();
        populateRandomly();
        
        int smoothingIterations = 10;
        for (int i=0; i<smoothingIterations; i++)
            smoothWorld();
        
        return new World(tiles);
    }
}