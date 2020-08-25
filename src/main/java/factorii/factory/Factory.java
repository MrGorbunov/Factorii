package factorii.factory;

import java.util.HashMap;

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

    public FacData getFacData (int x, int y) {
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
                    ((FacProducer) factory[x][y]).update();
                }
            }
        }

        // For item tubes, we essentially have a distributed double buffer
        // hence two for loops
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
        if (factory[x][y] != null)
            throw new Error("Attempted to place a factory tile at illegal location");
        
        switch (tile) {
            case WORKBENCH:
            case KILN:
            case FORGE:
                factory[x][y] = new FactoryCrafter(tile);
                break;
            
            case COPPER_WORKBENCH:
                factory[x][y] = new FactoryAutoCrafter(Tile.WORKBENCH);
                break;
            
            case MINING_DRILL:
                ItemIndex resource = GameState.world.harvestSpecific(x, y);
                factory[x][y] = new FactoryMiningDrill(resource);
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