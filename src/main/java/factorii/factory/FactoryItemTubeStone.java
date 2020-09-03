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
    private boolean existsAdjacentSteelTube;
    private boolean existsAdjacentInventory;

    // Where the current item came from
    private FacItemTube previousTube;
    private TubeDirection previousDirection;
    private ItemIndex transportingItem;

    // To avoid major issues with update order, each instance essentially stores buffer info.
    private FacItemTube bufferPreviousTube;
    private TubeDirection bufferPreviousDirection;
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
        existsAdjacentInventory = false;
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
                adjacentInventories[i] = ((FacInventory) testData).getInventory();
                existsAdjacentInventory = true;
            }
        }
    }

    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }




    //
    // Interacting with other tubes & updating
    //

    public boolean canMoveInto (ItemIndex testItem) { 
        // Stone item tubes do not care what items are passed to them
        return transportingItem == null && 
               bufferTransportingItem == null; 
    }

    public void moveInto (FacItemTube fromTube, TubeDirection fromDir, ItemIndex newItem) { 
        bufferPreviousTube = fromTube;
        bufferPreviousDirection = fromDir.getOppositeDirection();
        bufferTransportingItem = newItem;
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

    private boolean tryToMoveIntoAdjInventory () {
        if (existsAdjacentInventory == false)
            return false;

        for (Inventory inv : adjacentInventories) {
            if (inv == null) continue;

            inv.addItem(transportingItem);
            transportingItem = null;
            previousTube = null;
            return true;
        }

        return false;
    }

    private boolean tryToMoveIntoAdjTubeBiased (TubeDirection preferredDir) {
        TubeDirection unfavoredDirection = preferredDir.getOppositeDirection();

        if (existsAdjacentSteelTube) {
            for (int i=0; i<4; i++) {
                FacItemTube tube = adjacentTubes[i];
                if (tube == null || 
                    tube instanceof FactoryItemTubeSteel == false ||
                    unfavoredDirection.toIndex() == i) 
                        continue;

                if (moveIntoTube(i))
                    return true;
            }
        }

        // Then just try to move into preffered direction, other two directions, non preffered direction
        if (moveIntoTube(preferredDir.toIndex()))
            return true;
        for (TubeDirection dir : TubeDirection.values()) {
            if (dir.equals(preferredDir) ||
                dir.equals(unfavoredDirection))
                    continue;

            if (moveIntoTube(dir.toIndex()))
                return true;
        }
        if (moveIntoTube(unfavoredDirection.toIndex()))
            return true;

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

        for (TubeDirection dir : TubeDirection.values()) {
            if (moveIntoTube(dir.toIndex()))
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