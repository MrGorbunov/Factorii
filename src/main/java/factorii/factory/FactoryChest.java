package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.SizedInventory;
import factorii.world.Tile;

public class FactoryChest implements FacData, FacInventory {

    private Inventory inventory;
    private final Tile tile;

    public FactoryChest () {
        tile = Tile.CHEST;
        inventory = new SizedInventory(100);
    }

    public FactoryChest (SizedInventory startingInventory) {
        tile = Tile.CHEST;
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