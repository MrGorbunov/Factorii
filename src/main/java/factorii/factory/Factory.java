package factorii.factory;

import factorii.GameState;
import factorii.inventory.CraftingGlobals;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.world.Tile;

/*
Storage:
Have a list for every kind of structure
- Networks
- Chests
- Crafting Inventories
- Etc
*/

public class Factory {
    
    private int width;
    private int height;
    private final int UPDATES_PER_TICK;
    private int currentUpdates;

    private FacData[][] factory;

    public Factory (int width, int height) {
        this.width = width;
        this.height = height;
        factory = new FacData[width][height];

        UPDATES_PER_TICK = 4;
        currentUpdates = 0;
    }

    public Tile[][] getFactoryLayer () {
        Tile[][] factoryTiles = new Tile[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (factory[x][y] == null)
                    factoryTiles[x][y] = Tile.EMPTY;
                else
                    factoryTiles[x][y] = factory[x][y].getTile();
            }
        }

        return factoryTiles;
    }

    public FacData getAdjacentFacData (int x, int y) {
        FacData standingOver = factory[x][y];

        if (standingOver != null) {
            if (Tile.interactabilityPriotiy(standingOver.getTile()) != -1) {
                return factory[x][y];
            }
        }

        int maxPriority = -1;
        FacData maxFacData = null;

        for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = x + dx;
                int testY = y + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                FacData testData = factory[testX][testY];
                if (testData == null) continue;

                Tile testTile = factory[testX][testY].getTile();
                int priority = Tile.interactabilityPriotiy(testTile);
                if (priority > maxPriority) {
                    maxPriority = priority;
                    maxFacData = factory[testX][testY];
                }
            }
        }

        return maxFacData;
    }

    public FacData getFacDataAt (int x, int y) {
        return factory[x][y];
    }

    public void update () {

        // Checking to make sure processing only happens every n update calls
        currentUpdates++;
        if (currentUpdates < UPDATES_PER_TICK)
            return;
        currentUpdates = 0;


        //
        // Actual Factory Update

        // First production happens
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (factory[x][y] instanceof FacProducer) {
                    ((FacProducer) factory[x][y]).tickUpdate();
                }
            }
        }

        // For item tubes, we have a distributed double buffer
        // hence two for loops just for them.
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (factory[x][y] instanceof FacItemTube) {
                    ((FacItemTube) factory[x][y]).movementTick();
                }
            }
        }

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if (factory[x][y] instanceof FacItemTube) {
                    ((FacItemTube) factory[x][y]).bufferTick();
                }
            }
        }
    }



    /**
     * Will create a FacData at the coordinates specified of 
     * the type corresponding to the tile. Assumes that the placement
     * is legal.
     */
    public void placeFactoryTile (Tile tile, int x, int y) {
        boolean isUpgradingDrill = tile == Tile.AUTO_MINING_UPGRADE && factory[x][y] instanceof FactoryMiningDrill;
        if (factory[x][y] != null &&
            isUpgradingDrill == false)
                throw new Error("Attempted to place a factory tile at illegal location");
        
        switch (tile) {
            case WORKBENCH:
            case KILN:
            case FORGE:
                factory[x][y] = new FactoryCrafter(tile);
                break;
            
            case COPPER_WORKBENCH:
            case IRON_KILN:
            case STEEL_FORGE:
                factory[x][y] = new FactoryAutoCrafter(tile);
                break;
            
            case MINING_DRILL:
                ItemIndex miningResource = GameState.world.harvestSpecific(x, y);
                factory[x][y] = new FactoryMiningDrill(miningResource);
                break;

            case AUTO_MINING_UPGRADE:
                FacData existingDrill = factory[x][y];
                if (existingDrill instanceof FactoryMiningDrill == false)
                    throw new Error("Trying to place auto-mining upgrade where there is no drill");
                ItemIndex autoMiningResource =  ((FactoryMiningDrill) existingDrill).getResource();
                factory[x][y] = new FactoryAutoMiningDrill(autoMiningResource);
                break;



            case CHEST:
                factory[x][y] = new FactoryChest();
                refreshAdjacent(x, y);
                break;
            
            case ITEM_TUBE_STONE:
                FactoryItemTubeStone stoneTube = new FactoryItemTubeStone(factory, x, y);
                factory[x][y] = stoneTube;
                stoneTube.refresh(factory, x, y);
                refreshAdjacent(x, y);
                break;
            
            case ITEM_TUBE_GLASS:
                FactoryItemTubeGlass glassTube = new FactoryItemTubeGlass(factory, x, y);
                factory[x][y] = glassTube;
                glassTube.refresh(factory, x, y);
                refreshAdjacent(x, y);
                break;
                
            case ITEM_TUBE_STEEL:
                FactoryItemTubeSteel steelTube = new FactoryItemTubeSteel(factory, x, y);
                factory[x][y] = steelTube;
                steelTube.refresh(factory, x, y);
                refreshAdjacent(x, y);
                break;


            default:
                throw new Error("Attempted to place factory tile with no placement imlpementation");
        }
    }

    private void refreshAdjacent (int x, int y) {
        int[][] cardinalDirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
        for (int[] offset : cardinalDirs) {
            int testX = x + offset[0];
            int testY = y + offset[1];

            if (testX < 0 || testX >= width || testY < 0 || testY >= height)
                continue;
            
            FacData testData = factory[testX][testY];
            if (testData instanceof FacItemTube)
                ((FacItemTube) testData).refresh(factory, testX, testY);
        }
    }

}