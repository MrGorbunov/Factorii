package factorii.factory;

import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.inventory.StackSizedInventory;
import factorii.world.Tile;

/*
A Steel item tube, which sorts item by filtering what can exit
in what direction. These can be eithe white or black lists.
*/

public class FactoryItemTubeSteel extends FacItemTube implements FacData {
    
    private final Tile tile;

    private boolean filterIsWhitelist;
    private Inventory filterItems;

    public FactoryItemTubeSteel (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_STEEL;
        transportingItem = null;

        filterIsWhitelist = false;
        filterItems = new StackSizedInventory(1);
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
        if (testItem == null)
            throw new NullPointerException("passesFilter() should not be called with null item");

        boolean containedInList = filterItems.getQuantity(testItem) > 0;

        if (filterIsWhitelist)
            return containedInList;
        else
            return containedInList == false;
    }





    //
    // Interacting with other tubes & updating
    //

    @Override
    public boolean canMoveInto (ItemIndex testItem) { 
        return transportingItem == null && 
               bufferTransportingItem == null &&
               passesFilter(testItem);
    }






    //
    // Self Updating
    //

    public void movementTick () {
        if (transportingItem == null)
            return;

        if (tryToMoveIntoAdjInventory())
            return;

        if (previousDirection == null) {
            if (tryToMoveIntoAdjTubeUnbiased())
                return;

        } else {
            TubeDirection preferredDir = previousDirection.getOppositeDirection();
            if (tryToMoveIntoAdjTubeBiased(preferredDir))
                return;
        }

        // If the code reaches here, then it wasn't able to move the item anywhere.
        // So, buffer is filled with current item but removes previous tube,
        // allowing the item to move around if at a dead end.
        bufferTransportingItem = transportingItem;
        bufferPreviousTube = null;
    }

}