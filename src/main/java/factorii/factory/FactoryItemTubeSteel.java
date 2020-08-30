package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
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

    private boolean[] filterIsWhitelist;
    private ArrayList<ArrayList<ItemIndex>> filterItems = new ArrayList<ArrayList<ItemIndex>>();

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

        filterIsWhitelist = new boolean[] {false, false, false, false};
        filterItems = new ArrayList<ArrayList<ItemIndex>> ();
        filterItems.add(new ArrayList<ItemIndex> ());
        filterItems.add(new ArrayList<ItemIndex> ());
        filterItems.add(new ArrayList<ItemIndex> ());
        filterItems.add(new ArrayList<ItemIndex> ());
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

    // With 4 cardinal directions, this effectively has 4 filters
    // TubeDirection is thus used as a key for each filter.
    // Specifically, the tubeDirection.toIndex(); is used as the (surprise surprise) index in the filter lists

    /**
     * Sets the filter mode of a given direction. If whiteList is true, then
     * the filter acts as a whitelist. Otherwise it acts as a blacklist.
     */
    public void setFilterMode (TubeDirection dir, boolean whitelist) {
        filterIsWhitelist[dir.toIndex()] = whitelist;
    }

    public void addItemToFilter (TubeDirection dir, ItemIndex item) {
        ArrayList<ItemIndex> itemsInFilter = filterItems.get(dir.toIndex());

        if (itemsInFilter.contains(item))
            return;

        itemsInFilter.add(item);
    }

    public void removeItemFromFilter (TubeDirection dir, ItemIndex item) {
        ArrayList<ItemIndex> itemsInFilter = filterItems.get(dir.toIndex());

        itemsInFilter.remove(item);
    }

    public ArrayList<ItemIndex> getItemsInFilter (TubeDirection dir) {
        return filterItems.get(dir.toIndex());
    }

    /**
     * Checks if the transporting item can go into a certain direction.
     */
    private boolean passesFilter (TubeDirection direction) {
        if (transportingItem == null || direction == null)
            throw new NullPointerException("passesFilter() should not be called on null directions or with no transporting item");

        boolean containedInList = false;
        int index = direction.toIndex();

        for (ItemIndex item : filterItems.get(index)) {
            if (item == transportingItem) {
                containedInList = true;
                break;
            }
        }

        if (filterIsWhitelist[index])
            // Whitelist
            return containedInList;
        else
            // Blacklist
            return containedInList == false;
    }





    //
    // Updating & Item Transfer
    //

    // TODO: canMoveInto() should have a directional input to control directional mobility
    // 
    // Then SteelTubes could have an option to accept or reject based on direction.

    public boolean canMoveInto () { return transportingItem == null && bufferTransportingItem == null; }

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

        if (passesFilter(dir) == false)
            return false;

        // Test for adjacent inventory
        if (adjacentInventories[index] != null) {
            adjacentInventories[index].addItem(transportingItem);
            bufferTransportingItem = null;
            bufferPreviousTube = null;
            return true;

        // Test for adjacent tube
        } else if (adjacentTubes[index] != null &&
                   adjacentTubes[index].canMoveInto()) {
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