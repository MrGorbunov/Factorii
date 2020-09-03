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

public class FactoryItemTubeGlass implements FacData, FacItemTube {
    
    private final Tile tile;

    private FacItemTube[] adjacentTubes;
    // This is FacData instead of Inventory because producers need delicate care
    private FacData[] adjacentInventoriesAndProducers;
    private boolean existsAdjacentSteelTube;
    private boolean existsAdjacentInventoryOrProducer;

    private final int TICKS_PER_PULL; // Glass only pulls an item out every n ticks
    private int currentTicks;

    // Where the current item came from
    private FacItemTube previousTube;
    private TubeDirection previousDirection;
    private ItemIndex transportingItem;

    // To avoid major issues with update order, each instance essentially stores buffer info.
    private FacItemTube bufferPreviousTube;
    private TubeDirection bufferPreviousDirection;
    private ItemIndex bufferTransportingItem;

    public FactoryItemTubeGlass (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_GLASS;
        transportingItem = null;

        TICKS_PER_PULL = 4;
        currentTicks = 0;
    }

    /**
     * Given the factory 2D array, will determine neighbouring tubes & inventories.
     * The refreshRecursively flag, when true, will cause refresh to be called on neighbouring tubes.
     */
    public void refresh (FacData[][] factory, int x, int y) {
        adjacentTubes = new FacItemTube[4];
        adjacentInventoriesAndProducers = new FacData[4];
        existsAdjacentInventoryOrProducer = false;
        existsAdjacentSteelTube = false;

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

            } else if (testData instanceof FacInventory) {
                adjacentInventoriesAndProducers[i] = testData;
                existsAdjacentInventoryOrProducer = true;

            } else if (testData instanceof FacProducer) {
                adjacentInventoriesAndProducers[i] = testData;
                existsAdjacentInventoryOrProducer = true;

            }
        }
    }

    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }




    //
    // Interacting with others
    //

    public boolean canMoveInto (ItemIndex testItem) { return false; }

    public void moveInto (FacItemTube fromTube, TubeDirection fromDir, ItemIndex newItem) { 
        throw new Error ("Attempted to move item into glass item tube, make sure canMoveInto() is checked");
    }

    /**
     * Resets the buffer
     */
    public void bufferTick () {
        previousTube = bufferPreviousTube;
        previousDirection = bufferPreviousDirection;
        transportingItem = bufferTransportingItem;

        bufferPreviousTube = null;
        bufferPreviousDirection = null;
        bufferTransportingItem = null;
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
            if (tryToPullFromAdjInvOrProducer())
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

    private boolean tryToPullFromAdjInvOrProducer () {
        if (existsAdjacentInventoryOrProducer == false)
            return false;
        
        for (int i=0; i<4; i++) {
            FacData adjFacData = adjacentInventoriesAndProducers[i];
            if (adjFacData == null)
                continue;
            
            if (adjFacData instanceof FactoryChest) {
                Inventory adjInventory = ((FacInventory) adjFacData).getInventory();
                if (adjInventory.getTotalSize() == 0)
                    continue;
                bufferTransportingItem = adjInventory.getFirstItem();
                adjInventory.removeItem(bufferTransportingItem);
                bufferPreviousTube = null;
                return true;

            } else if (adjFacData instanceof FacProducer) {
                FacProducer adjProducer = (FacProducer) adjFacData;
                if (adjProducer.canTakeProduct() == false)
                    continue;
                bufferTransportingItem = adjProducer.takeProduct();
                bufferPreviousTube = null;
                return true;
            }
        }

        return false;
    }

    private boolean tryToMoveIntoAdjTubeUnbiased () {
        if (existsAdjacentSteelTube) {
            for (int i=0; i<4; i++) {
                FacItemTube tube = adjacentTubes[i];
                if (tube == null ||
                    tube instanceof FactoryItemTubeSteel == false)
                        continue;

                if (moveIntoTube(i))
                    return true;
            }
        }

        // No Steel tube, just find any tube to move into
        for (int i=0; i<4; i++) {
            if (moveIntoTube(i))
                return true;
        }

        return false;
    }

    /**
     * Attemptes to move into a tube in the given index
     * @param index int the index to move into
     * @return true = success, false = unable
     */
    private boolean moveIntoTube (int index) {
        FacItemTube tube = adjacentTubes[index];
        if (tube == null ||
            tube.canMoveInto(transportingItem) == false)
                return false;

        tube.moveInto(this, TubeDirection.getFromIndex(index), transportingItem); 
        transportingItem = null;
        previousTube = null;
        return true;
    }

}