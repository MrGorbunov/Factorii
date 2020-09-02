package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.inventory.StackSizedInventory;
import factorii.world.Tile;

import java.util.ArrayList;

/*
A Steel item tube, which sorts item by filtering what can exit
in what direction. These can be eithe white or black lists.

Currently there is no way to filter what goes into the itemtube, 
only the output.
*/

public class FactoryItemTubeSteel implements FacData, FacItemTube {
    
    private final Tile tile;

    private FacItemTube[] adjacentTubes;
    private Inventory[] adjacentInventories;

    private boolean filterIsWhitelist;
    private Inventory filterItems;

    // Where the current item came from
    private FacItemTube previousTube;
    private TubeDirection previousDirection;
    private ItemIndex transportingItem;

    // To avoid major issues with update order, each instance essentially stores buffer info.
    private FacItemTube bufferPreviousTube;
    private TubeDirection bufferPreviousDirection;
    private ItemIndex bufferTransportingItem;

    public FactoryItemTubeSteel (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_STEEL;
        transportingItem = null;

        filterIsWhitelist = false;
        filterItems = new StackSizedInventory(1);
    }

    /**
     * Given the factory 2D array, will determine neighbouring tubes & inventories.
     * The refreshRecursively flag, when true, will cause refresh to be called on neighbouring tubes.
     */
    public void refresh (FacData[][] factory, int x, int y) {
        adjacentTubes = new FacItemTube[4];
        // Because this pulls items out of inventories, we need to be more delicate
        // and so look at the FacData
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

    // TODO: Return tile is almost always constant
    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }





    //
    // Item Filter Configuration
    //

    /**
     * Sets the filter mode. If whiteList is true, then
     * the filter acts as a whitelist. Otherwise it acts as a blacklist.
     */
    public void setFilterMode (boolean whitelist) {
        filterIsWhitelist = whitelist;
    }

    /**
     * true = whitelist
     * false = blacklist
     */
    public boolean getFilterMode () {
        return filterIsWhitelist;
    }

    public Inventory getFilterInventory () {
        return filterItems;
    }

    /**
     * Checks if an item can go into a certain direction.
     */
    private boolean passesFilter (ItemIndex testItem) {
        if (transportingItem == null)
            throw new NullPointerException("passesFilter() should not be called with null item");

        boolean containedInList = filterItems.getQuantity(testItem) > 0;

        if (filterIsWhitelist)
            return containedInList;
        else
            return containedInList == false;
    }





    //
    // Updating & Item Transfer
    //

    public boolean canMoveInto (ItemIndex testItem) { 
        return transportingItem == null && 
               bufferTransportingItem == null &&
               passesFilter(testItem);
    }

    public void moveInto (FacItemTube fromTube, TubeDirection fromDir, ItemIndex newItem) { 
        bufferPreviousTube = fromTube;
        bufferPreviousDirection = fromDir.getOppositeDirection();
        bufferTransportingItem = newItem;

        throw new Error ("Attempted to move item into glass item tube, make sure canMoveInto() is checked");
    }

    /**
     * Populates the buffer
     */
    public void movementTick () {
        if (transportingItem == null)
            return;

        // If possible, move in preferredDirection, otherwise look at other dirs
        TubeDirection preferredDirection = previousDirection.getOppositeDirection();
        if (tryToMoveInDir(preferredDirection))
            return;

        for (TubeDirection dir : TubeDirection.values()) {
            if (dir == preferredDirection)
                continue;

            if (tryToMoveInDir(dir))
                return;
        }

        // If the code reaches here, then it wasn't able to move the item anywhere.
        // So, buffer is filled with current item but removes previous tube,
        // allowing the item to move around if at a dead end.
        bufferTransportingItem = transportingItem;
        bufferPreviousTube = null;
    }

    /**
     * Attempts to move the transporting item in the direction
     * provided. 
     * If successful, returns true (and handles movement logic).
     * If unsuccessful, returns false and nothign happens.
     */
    private boolean tryToMoveInDir (TubeDirection dir) {
        int index = dir.toIndex();

        // Test for adjacent inventory
        if (adjacentInventories[index] != null) {
            adjacentInventories[index].addItem(transportingItem);
            bufferTransportingItem = null;
            bufferPreviousTube = null;
            return true;

        // Test for adjacent tube
        } else if (adjacentTubes[index] != null &&
                   adjacentTubes[index].canMoveInto(transportingItem)) {
            adjacentTubes[index].moveInto(this, TubeDirection.getFromIndex(index), transportingItem); 
            transportingItem = null;
            previousTube = null;
            return true;
        }

        return false;
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

}