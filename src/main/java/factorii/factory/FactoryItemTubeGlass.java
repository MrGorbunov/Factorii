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

    private FacProducer[] adjacentProducers;
    private boolean existsAdjProducer;

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
    // Because producers need tracking, this is overridden
    //
    @Override
    public void refresh (FacData[][] factory, int x, int y) {
        adjacentTubes = new FacItemTube[4];
        adjacentProducers = new FacProducer[4];
        adjacentInventories = new Inventory[4];
        existsAdjacentSteelTube = false;
        existsAdjProducer = false;
        existsAdjacentInventory = false;

        int width = factory.length;
        int height = factory[0].length;
        for (int i=0; i<4; i++) {
            TubeDirection dir = TubeDirection.getFromIndex(i);
            int[] offset = dir.getCordinateOffset();
            int testX = x + offset[0];
            int testY = y + offset[1];

            if (testX < 0 || testX >= width || testY < 0 || testY >= height)
                continue;
            
            FacData testData = factory[testX][testY];
            if (testData instanceof FacItemTube) {
                FacItemTube adjTube = (FacItemTube) testData;
                adjacentTubes[i] = adjTube;
                if (testData instanceof FactoryItemTubeSteel)
                    existsAdjacentSteelTube = true;

            // This is important because FacProducer is usually also FacInventory
            // but the glass tubes don't want to treat like it's an inventory
            } else if (testData instanceof FacProducer) {
                adjacentProducers[i] = (FacProducer) testData;
                existsAdjProducer = true;
            
            } else if (testData instanceof FacInventory) {
                adjacentInventories[i] = ((FacInventory) testData).getInventory();
                existsAdjacentInventory = true;
            }
        }
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

            if (tryToPullFromAdjProducer())
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

    private boolean tryToPullFromAdjProducer () {
        if (existsAdjProducer == false)
            return false;

        for (int i=0; i<4; i++) {
            FacProducer adjProducer = adjacentProducers[i];
            if (adjProducer == null ||
                adjProducer.canTakeProduct() == false)
                    continue;
            
            // Pull from inventory
            ItemIndex pullItem = adjProducer.takeProduct();
            bufferTransportingItem = pullItem;
            return true;
        }
        
        return false;
    }

}