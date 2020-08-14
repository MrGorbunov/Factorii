package minirpg.factory;

import java.util.HashMap;

import minirpg.inventory.Inventory;
import minirpg.inventory.ItemIndex;
import minirpg.world.Tile;

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

    private FactoryData[][] factory;

    public Factory (int width, int height) {
        this.width = width;
        this.height = height;
        factory = new FactoryData[width][height];

        // Testing chest inventories
        // Inventory inv1 = new Inventory();
        // inv1.addItemMulti(ItemIndex.BAR_COPPER, 10);
        // inv1.addItem(ItemIndex.KILN);

        // Inventory inv2 = new Inventory();
        // inv2.addItem(ItemIndex.FORGE);

        // FactoryChest chest1 = new FactoryChest(Tile.CHEST, inv1);
        // FactoryChest chest2 = new FactoryChest(Tile.CHEST, inv2);

        // factory[10][10] = chest1;
        // factory[11][10] = chest2;
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

    public FactoryData getFactoryData (int x, int y) {
        return factory[x][y];
    }

    /**
     * Will create a FactoryData at the coordinates specified of 
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
                factory[x][y] = new FactoryStatic(tile);
                break;
            
            case CHEST:
                factory[x][y] = new FactoryChest(Tile.CHEST);
                break;
        }
    }

}