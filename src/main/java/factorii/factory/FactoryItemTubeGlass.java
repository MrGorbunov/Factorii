package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.world.Tile;

/*
The rules of transport are as follows.

An item tube will always try to put its current transporting 
item into an adjacent item tube or inventory.

Glass item tubes can never recieve items from other tubes, and
only get items by pulling from adjacent inventories.
*/

public class FactoryItemTubeGlass extends FacItemTube implements FacData {
    
    private final Tile tile;

    private final int TICKS_PER_PULL; // Glass only pulls an item out every n ticks
    private int currentTicks;

    public FactoryItemTubeGlass (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_GLASS;
        transportingItem = null;

        TICKS_PER_PULL = 6;
        currentTicks = 0;
    }

    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }




    //
    // Interacting with others
    //

    @Override
    public boolean canMoveInto (ItemIndex testItem) { return false; }

    @Override
    public void moveInto (FacItemTube fromTube, TubeDirection fromDir, ItemIndex newItem) { 
        throw new Error ("Attempted to move item into glass item tube, make sure canMoveInto() is checked");
    }






    //
    // Bulk of self updating
    //

    /**
     * Populates the buffer
     */
    public void movementTick () {
        currentTicks++;

        if (currentTicks <= TICKS_PER_PULL) {
            bufferTransportingItem = transportingItem;
            bufferPreviousTube = previousTube;
            return;
        }
        
        // These functions return true if they succesfully pull/ move an item
        if (transportingItem == null) {
            if (tryToPullFromAdjInventory())
                return;

        // Am with an item, try to transport it (but not into an inventory)
        } else {
            currentTicks = 0;

            if (tryToMoveIntoAdjTubeUnbiased())
                return;
        }

        // If the code reaches here, then it wasn't able to move the item anywhere.
        // So, buffer is filled with current info so that nothing changed
        bufferTransportingItem = transportingItem;
        bufferPreviousTube = previousTube;
    }

    private boolean tryToPullFromAdjInventory () {
        if (existsAdjacentInventory == false)
            return false;
        
        for (int i=0; i<4; i++) {
            Inventory adjInventory = adjacentInventories[i];
            if (adjInventory == null ||
                adjInventory.getTotalSize() == 0)
                    continue;
            
            // Pull from inventory
            ItemIndex pullItem = adjInventory.returnFirstItem();
            bufferTransportingItem = pullItem;
            adjInventory.removeItem(pullItem);
            return true;
        }

        return false;
    }

}