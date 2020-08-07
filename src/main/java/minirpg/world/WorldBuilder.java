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
            terrainMask = smoothMask(terrainMask, 3);

        // Sand
        /*
        The outer edge is almost completely filled
        The inner edge is ~50% sand
        */
        // outer edge
        boolean[][] landMask = terrainMask;
        boolean[][] innerFill = erodeMask(landMask);
        boolean[][] sandNoise = randomMaskPercent(0.94);
        boolean[][] sandMask = masksXOR(terrainMask, innerFill);
        sandMask = masksAND(sandNoise, sandMask);

        innerFill = erodeMask(innerFill);
        sandNoise = randomMaskPercent(0.5);
        boolean[][] innerEdge = masksXOR(terrainMask, innerFill);
        innerEdge = masksAND(sandNoise, innerEdge);
        sandMask = masksOR(sandMask, innerEdge);

        // Stone
        boolean[][] stoneNoise = randomMaskPercent(0.5);
        stoneNoise = smoothMask(stoneNoise, 3);

        boolean[][] stoneMask = randomMaskPercent(0.9);
        stoneMask = erodeMask(stoneMask);
        stoneMask = smoothMask(stoneMask, 1);
        stoneMask = masksAND(landMask, stoneMask);
        stoneMask = masksAND(stoneNoise, stoneMask);
        
        // Ores
        boolean[][] coalMask = generateOreMask(landMask);
        boolean[][] copperMask = generateOreMask(landMask);
        boolean[][] ironMask = generateOreMask(landMask);

        // Trees
        landMask = erodeMask(landMask);
        landMask = erodeMask(landMask);
        boolean[][] treeMask = randomMaskPercent(0.4);
        treeMask = smoothMask(treeMask, 1);
        treeMask = smoothMask(treeMask, 1);
        boolean[][] treeNoise = randomMaskPercent(0.9);
        treeMask = masksAND(treeNoise, treeMask);
        treeMask = masksAND(landMask, treeMask);



        // Final conversions
        Tile[][] terrain = convertMask(terrainMask, Tile.DIRT, Tile.WATER);

        Tile[][] resource = convertMask(treeMask, Tile.TREE, Tile.EMPTY);
        resource = addToLayer(resource, coalMask, Tile.ORE_COAL, false);
        resource = addToLayer(resource, copperMask, Tile.ORE_COPPER, false);
        resource = addToLayer(resource, ironMask, Tile.ORE_IRON, false);
        resource = addToLayer(resource, stoneMask, Tile.STONE, false);
        resource = addToLayer(resource, sandMask, Tile.SAND, false);

        // Create a blank tile map for the factory (avoids nulls)
        Tile[][] factoryMap = convertMask(blankMask(), Tile.CHEST, Tile.EMPTY);

        return new World(terrain, resource, factoryMap);
    }

    private boolean[][] generateOreMask (boolean[][] landMask) {
        boolean[][] oreMask = randomMaskPercent(0.38);
        oreMask = smoothMask(oreMask, 1);
        oreMask = smoothMask(oreMask, 2);
        oreMask = smoothMask(oreMask, 2);
        for (int i=0; i<3; i++)
            oreMask = smoothMask(oreMask, 1);

        oreMask = masksAND(landMask, oreMask);
        return oreMask;
    }



    //
    // Mask Operations
    //
    /*
    Operations are done on boolean[][] masks, and then
    layer on they're converted to Tile[][] layers.
     */

    /**
     * Generates a blank mask of false
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

    private boolean[][] masksOR (boolean[][] maskA, boolean[][] maskB) {
        boolean[][] finalMask = new boolean[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                finalMask[x][y] = maskA[x][y] || maskB[x][y];
            }
        }
    
        return finalMask;
    }

    private boolean[][] masksXOR (boolean[][] maskA, boolean[][] maskB) { 
        boolean[][] finalMask = new boolean[width][height];

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                boolean a = maskA[x][y];
                boolean b = maskB[x][y];
                finalMask[x][y] = (a && !b) || (!a && b);
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
