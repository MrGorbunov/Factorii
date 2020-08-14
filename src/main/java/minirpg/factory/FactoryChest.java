package minirpg.factory;

import minirpg.inventory.Inventory;
import minirpg.world.Tile;

public class FactoryChest implements FactoryData {

    private Inventory inventory;
    private Tile tile;

    public FactoryChest (Tile tile) {
        this.tile = tile;
        inventory = new Inventory();
    }

    public FactoryChest (Tile tile, Inventory startingInventory) {
        this.tile = tile;
        inventory = startingInventory;
    }

    public Tile getTile () {
        return tile;
    }



    //
    // Chest operations
    //

    public Inventory getInventory () { 
        return inventory;
    }

}