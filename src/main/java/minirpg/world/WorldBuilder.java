package minirpg.world;

public class WorldBuilder {
    private int width;
    private int height;

    public WorldBuilder (int width, int height) {
        this.width = width;
        this.height = height;
    }

    public World generateDefaultWorld () {
        boolean[][] terrainMask = randomMask();
        for (int i=0; i<6; i++)
            terrainMask = smoothMask(terrainMask, 2);

        boolean[][] landMask = erodeMask(terrainMask);
        boolean[][] coalMask = randomMaskPercent(0.9);
        coalMask = erodeMask(coalMask);
        coalMask = smoothMask(coalMask, 1);
        coalMask = masksAND(landMask, coalMask);
        
        // The erosion keeps the trees away from the shore
        landMask = erodeMask(landMask);
        boolean[][] treeMask = randomMask();
        treeMask = smoothMask(treeMask, 1);
        treeMask = masksAND(landMask, treeMask);

        Tile[][] terrain = convertMask(terrainMask, Tile.DIRT, Tile.WATER);
        Tile[][] interactables = convertMask(coalMask, Tile.ORE_COAL, Tile.EMPTY);
        interactables = addToLayer(interactables, treeMask, Tile.TREE, false);

        return new World(terrain, interactables);
    }



    //
    // Masks
    //
    /*
    Operations are done on boolean[][] masks, and then
    layer on they're converted to Tile[][] layers.
     */
    private boolean[][] blankMask () {
        // Because these are primitives, they default to false
        return new boolean[width][height];
    }

    private boolean[][] randomMask () {
        boolean[][] mask = new boolean[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                mask[x][y] = Math.random() < 0.5;
            }
        }

        return mask;
    }

    private boolean[][] randomMaskPercent (double percentFill) {
        boolean[][] mask = new boolean[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                mask[x][y] = Math.random() < percentFill;
            }
        }

        return mask;
    }

    private boolean[][] smoothMask (boolean[][] inputMask, int radius) {
        boolean[][] smootherMask = new boolean[width][height];
         
        // radius 1 = 3x3 square,  radius 3 = 5x5 square
        int thresholdNeighbours = (2*radius + 1) * (2*radius + 1) / 2;

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int numTrueNeighbours = 0;

                for (int dx=-radius; dx<=radius; dx++) {
                    for (int dy=-radius; dy<=radius; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        if (checkX < 0 || checkX >= width || checkY < 0 || checkY >= height)
                            continue;

                        if (inputMask[checkX][checkY])
                            numTrueNeighbours++;
                    }
                }
                
                // By checking if it's above a threshold instead of %,
                // the edges of the map become homogenous => this is
                // useful to surround the world with ocean.
                smootherMask[x][y] = numTrueNeighbours > thresholdNeighbours;
            }
        }

        return smootherMask;
    }

    private boolean[][] masksAND (boolean[][] maskA, boolean[][] maskB) {
        boolean[][] finalMask = new boolean[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                finalMask[x][y] = maskA[x][y] && maskB[x][y];
            }
        }
    
        return finalMask;
    }

    private boolean[][] erodeMask (boolean[][] inputMask) {
        // Very similar logic to smoothing
        // Just that radius = 1 and unless completely surrounded,
        // this says nope
        boolean[][] erodedMask = new boolean[width][height];
         
        for (int x=0; x<width; x++) {
            cords : for (int y=0; y<height; y++) {
                for (int dx=-1; dx<=1; dx++) {
                    for (int dy=-1; dy<=1; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        if (checkX < 0 || checkX >= width || checkY < 0 || checkY >= height)
                            continue;

                        if (inputMask[checkX][checkY] == false) {
                            erodedMask[x][y] = false;
                            continue cords;
                        }
                    }
                }
                
                erodedMask[x][y] = true;
            }
        }

        return erodedMask;
    }





    //
    // Mask to Layer conversions
    //

    private Tile[][] convertMask (boolean[][] mask, Tile trueTile, Tile falseTile) {
        Tile[][] worldLayer = new Tile[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (mask[x][y])
                    worldLayer[x][y] = trueTile;
                else
                    worldLayer[x][y] = falseTile;
            }
        }

        return worldLayer;
    }

    /**
     * Adds trueTile to a Tile[][] where mask is true,
     * and does nothing where mask is false.
     *
     * @param overrideTiles if true, this method will replace
     *   tiles in mask even if there was information there.
     *   Otherwise it checks if mask[x][y] is Tile.EMPTY
     */
    private Tile[][] addToLayer (Tile[][] layer, boolean[][] mask, Tile trueTile, boolean overrideTiles) {
        Tile[][] newLayer = new Tile[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (!mask[x][y]) {
                    newLayer[x][y] = layer[x][y];
                    continue;
                }

                if (overrideTiles || layer[x][y] == Tile.EMPTY)
                    newLayer[x][y] = trueTile;
                else 
                    newLayer[x][y] = layer[x][y];
            }
        }

        return newLayer;
    }





}
