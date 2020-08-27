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
    private FacData[] adjacentInventoriesAndProducers;

    private final int TICKS_PER_PULL; // Glass only pulls an item out every n ticks
    private int currentTicks;

    // Where the current item came from
    private FacItemTube previousTube;
    private ItemIndex transportingItem;

    // To avoid major issues with update order, each instance essentially stores buffer info.
    private FacItemTube bufferPreviousTube;
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
        // Because this pulls items out of inventories, we need to be more delicate
        // and so look at the FacData
        adjacentInventoriesAndProducers = new FacData[4]; 

        final int[][] cardinalDirections = new int[][] { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
        int width = factory.length;
        int height = factory[0].length;
        for (int i=0; i<4; i++) {
            int[] offset = cardinalDirections[i];
            int testX = x + offset[0];
            int testY = y + offset[1];

            if (testX < 0 || testX >= width || testY < 0 || testY >= height)
                continue;
            
            FacData testData = factory[testX][testY];
            if (testData instanceof FacItemTube) {
                FacItemTube adjTube = (FacItemTube) testData;
                adjacentTubes[i] = adjTube;

            } else if (testData instanceof FacInventory ||
                       testData instanceof FacProducer) {
                adjacentInventoriesAndProducers[i] = testData;
            }
        }
    }

    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }




    //
    // Updating & Item Transfer
    //

    public boolean canMoveInto () { return false; }

    public void moveInto (FacItemTube previousTube, ItemIndex newItem) { 
        bufferPreviousTube = previousTube;
        bufferTransportingItem = newItem;

        throw new Error ("Attempted to move item into glass item tube, make sure canMoveInto() is checked");
    }

    /**
     * Populates the buffer
     */
    public void movementTick () {
        currentTicks++;

        // If without item, try to get one
        if (transportingItem == null &&
            currentTicks > TICKS_PER_PULL) {
                currentTicks = 0;

                for (int i=0; i<4; i++) {
                    // No tubes, so now try to pull from inventory
                    if (adjacentInventoriesAndProducers[i] == null)
                        continue;
                    
                    
                    FacData adjFacData = adjacentInventoriesAndProducers[i];

                    // Pull from chest if possible
                    if (adjFacData instanceof FactoryChest) {
                        Inventory adjInventory = ((FacInventory) adjFacData).getInventory();

                        if (adjInventory.getTotalSize() == 0)
                            continue;
                        
                        bufferTransportingItem = adjInventory.getFirstItem();
                        adjInventory.removeItem(bufferTransportingItem);
                        bufferPreviousTube = null;

                    // Maybe it's a producer (auto mining drill & auto crafting stations)
                    } else if (adjFacData instanceof FacProducer) {
                        FacProducer adjProducer = (FacProducer) adjFacData;
                        if (adjProducer.canTakeProduct() == false)
                            continue;
                        
                        bufferTransportingItem = adjProducer.takeProduct();
                        bufferPreviousTube = null;
                    }


                    return;
                }

        // Am with an item, try to transport it
        } else {
            for (int i=0; i<4; i++) {
                if (adjacentTubes[i] == null ||
                    adjacentTubes[i] == previousTube ||
                    adjacentTubes[i].canMoveInto() == false)
                        continue;

                adjacentTubes[i].moveInto(this, transportingItem); 
                transportingItem = null;
                previousTube = null;
                return;
            }
        }

        // If the code reaches here, then it wasn't able to move the item anywhere.
        // So, buffer is filled with current info so that nothing changed
        bufferTransportingItem = transportingItem;
        bufferPreviousTube = previousTube;
    }

    /**
     * Resets the buffer
     */
    public void bufferTick () {
        previousTube = bufferPreviousTube;
        transportingItem = bufferTransportingItem;

        bufferPreviousTube = null;
        bufferTransportingItem = null;
    }

}