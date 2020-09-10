package factorii.world;

/*
This class is responsible for generating terrain.
There are two main things to generate 
 - the underlying ground & water
 - the resources that spawn
*/

public class WorldBuilderTerrainGenerator {

    private int width;
    private int height;
    
    /**
     * Endless dirt
     * 
     * Just dirt, no resources
     */
    public Tile[][] generateDirtBase (int width, int height) {
        this.width = width;
        this.height = height;

        Tile[][] terrain = convertMask(blankMask(), Tile.GROUND, Tile.GROUND);
        
        return terrain;
    }

    public Tile[][] generateBlankGrid (int width, int height) {
        Tile[][] resources = convertMask(blankMask(), Tile.EMPTY, Tile.EMPTY);

        return resources;
    }

    /*
     * Pocket World 
     * 
     * Big dirt patch with lots of tiny lakes & resource patches
     */
    public Tile[][] generatePocketWorldBase (int width, int height) {
        this.width = width;
        this.height = height;

        boolean[][] terrainMask = randomMaskPercent(0.6);
        terrainMask = smoothMask(terrainMask, 1);
        terrainMask = smoothMask(terrainMask, 3);
        terrainMask = smoothMask(terrainMask, 1);

        Tile[][] terrain = convertMask(terrainMask, Tile.GROUND, Tile.WATER);

        return terrain;
    }

    public Tile[][] generatePocketWorldResources (int width, int height, Tile[][] terrainBase) {
        this.width = width;
        this.height = height;

        boolean[][] terrainMask = convertToBooleanMask(terrainBase, Tile.GROUND);

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
        boolean[][] stoneMask = generateOreMask(landMask, 0.39);
        
        // Ores
        boolean[][] coalMask = generateOreMask(landMask);
        boolean[][] copperMask = generateOreMask(landMask, 0.39);
        boolean[][] ironMask = generateOreMask(landMask);

        // Trees
        landMask = erodeMask(landMask);
        landMask = erodeMask(landMask);
        boolean[][] treeMask = randomMaskPercent(0.42);
        treeMask = smoothMask(treeMask, 1);
        treeMask = smoothMask(treeMask, 2);
        boolean[][] treeNoise = randomMaskPercent(0.9);
        treeMask = masksAND(treeNoise, treeMask);
        treeMask = masksAND(landMask, treeMask);


        // Final conversions
        Tile[][] resources = convertMask(treeMask, Tile.TREE, Tile.EMPTY);
        resources = addToLayer(resources, coalMask, Tile.ORE_COAL, false);
        resources = addToLayer(resources, copperMask, Tile.ORE_COPPER, false);
        resources = addToLayer(resources, ironMask, Tile.ORE_IRON, false);
        resources = addToLayer(resources, stoneMask, Tile.STONE, false);
        resources = addToLayer(resources, sandMask, Tile.SAND, false);

        return resources;
    }

    /**
     * Archipelago world
     * 
     * Archipelago with resources scattered around
     */
    public Tile[][] generateArchipelagoWorldBase (int width, int height) {
        this.width = width;
        this.height = height;

        boolean[][] terrainMask = randomMask();
        for (int i=0; i<6; i++)
            terrainMask = smoothMask(terrainMask, 3);

        Tile[][] terrain = convertMask(terrainMask, Tile.GROUND, Tile.WATER);

        return terrain;
    }

    public Tile[][] generateArchipelagoWorldResources (int width, int height, Tile[][] terrainBase) {
        this.width = width;
        this.height = height;

        boolean[][] terrainMask = convertToBooleanMask(terrainBase, Tile.GROUND);

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



        Tile[][] resources = convertMask(treeMask, Tile.TREE, Tile.EMPTY);
        resources = addToLayer(resources, coalMask, Tile.ORE_COAL, false);
        resources = addToLayer(resources, copperMask, Tile.ORE_COPPER, false);
        resources = addToLayer(resources, ironMask, Tile.ORE_IRON, false);
        resources = addToLayer(resources, stoneMask, Tile.STONE, false);
        resources = addToLayer(resources, sandMask, Tile.SAND, false);

        return resources;
    }



    //
    // not public calls but still generation

    private boolean[][] generateOreMask (boolean[][] landMask, double percentage) {
        boolean[][] oreMask = randomMaskPercent(percentage);
        oreMask = smoothMask(oreMask, 1);
        oreMask = smoothMask(oreMask, 2);
        oreMask = smoothMask(oreMask, 2);
        for (int i=0; i<3; i++)
            oreMask = smoothMask(oreMask, 1);

        oreMask = masksAND(landMask, oreMask);
        return oreMask;
    }

    private boolean[][] generateOreMask (boolean[][] landMask) {
        return generateOreMask(landMask, 0.38);
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

    // TODO: Seed the random generation
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
    // Mask <-> Layer conversions
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

    /**
     * Will convert a Tile[][] layer into a boolean[][] mask, wherein
     * all tiles that are trueTile become true, and everything else false.
     */
    private boolean[][] convertToBooleanMask (Tile[][] layer, Tile trueTile) {
        boolean[][] mask = new boolean[layer.length][layer[0].length];

        for (int x=0; x<layer.length; x++) {
            for (int y=0; y<layer[0].length; y++) {
                mask[x][y] = layer[x][y] == trueTile;
            }
        }

        return mask;
    }
}
