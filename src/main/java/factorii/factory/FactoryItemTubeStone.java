package factorii.factory;

import factorii.inventory.ItemIndex;
import factorii.world.Tile;

/*
The rules of transport are as follows.

An item tube will always try to put its current transporting 
item into an adjacent item tube or inventory.
*/

public class FactoryItemTubeStone extends FacItemTube implements FacData {
    
    private final Tile tile;

    public FactoryItemTubeStone (FacData[][] factory, int x, int y) {
        tile = Tile.ITEM_TUBE_STONE;
        transportingItem = null;
    }

    public Tile getTile () { 
        if (transportingItem != null)     
            return ItemIndex.itemToTile(transportingItem); 

        return tile;
    }




    //
    // Interacting with other tubes & updating
    //

    // The abstract class' default implementations are what I need



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