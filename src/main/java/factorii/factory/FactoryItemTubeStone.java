package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.world.Tile;

/*
The rules of transport are as follows.

An item tube will always try to put its current transporting 
item into an adjacent item tube or inventory.
*/

public class FactoryItemTubeStone implements FacData, FacItemTube {
    
    private final Tile tile;

    private FacItemTube[] adjacentTubes;
    private Inventory[] adjacentInventories;

    // Where the current item came from
    private FacItemTube previousTube;
    private ItemIndex transportingItem;

    // To avoid major issues with update order, each instance essentially stores buffer info.
    private FacItemTube bufferPreviousTube;
    private ItemIndex bufferTransportingItem;

    public FactoryItemTubeStone (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_STONE;
        transportingItem = null;
    }

    /**
     * Given the factory 2D array, will determine neighbouring tubes & inventories.
     * The refreshRecursively flag, when true, will cause refresh to be called on neighbouring tubes.
     */
    public void refresh (FacData[][] factory, int x, int y) {
        adjacentTubes = new FacItemTube[4];
        adjacentInventories = new Inventory[4];

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

            } else if (testData instanceof FacInventory) {
                adjacentInventories[i] = ((FacInventory) testData).getInventory();
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

    public boolean canMoveInto () { return transportingItem == null && bufferTransportingItem == null; }

    public void moveInto (FacItemTube previousTube, ItemIndex newItem) { 
        bufferPreviousTube = previousTube;
        bufferTransportingItem = newItem;
    }

    /**
     * Populates the buffer
     */
    public void movementTick () {
        if (transportingItem == null)
            return;

        for (int i=0; i<4; i++) {
            if (adjacentTubes[i] == null) {
                // No tubes, test for inventory instead
                if (adjacentInventories[i] == null)
                    continue;
                
                // Place into inventory
                adjacentInventories[i].addItem(transportingItem);
                bufferTransportingItem = null;
                bufferPreviousTube = null;
                return;
            }

            // This is checking reference, it is not .equals(...) for a reason
            if (adjacentTubes[i] == previousTube ||
                adjacentTubes[i].canMoveInto() == false)
                    continue;

            adjacentTubes[i].moveInto(this, transportingItem); 
            transportingItem = null;
            previousTube = null;
            return;
        }

        // If the code reaches here, then it wasn't able to move the item anywhere.
        // So, buffer is filled with current item but removes previous tube,
        // allowing the item to move around if at a dead end.
        bufferTransportingItem = transportingItem;
        bufferPreviousTube = null;
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