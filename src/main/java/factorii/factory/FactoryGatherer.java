package factorii.factory;

import factorii.inventory.ItemIndex;
import factorii.world.Tile;

public class FactoryGatherer extends FacItemTube implements FacData {

    private final ItemIndex PRODUCT;
    private final int TICKS_PER_TAKE;
    // Gatherers can either be Deep Drills or Lumber Yards
    private final boolean IS_DEEP_DRILL;

    private int ticksSinceLastTake;

    public Tile getTile () { 
        if (transportingItem == null) {
            if (IS_DEEP_DRILL)
                return Tile.DEEP_DRILL; 
            else
                return Tile.LUMBER_YARD;
        }
        return ItemIndex.itemToTile(transportingItem);
    }

    public FactoryGatherer (ItemIndex product, boolean isDeepDrill) {
        if (product == null)
            throw new NullPointerException("Product must be an actual item");

        this.PRODUCT = product;
        IS_DEEP_DRILL = isDeepDrill;
        TICKS_PER_TAKE = 10;
    }



    //
    // Updating
    //

    @Override
    public boolean canMoveInto (ItemIndex testItem) {
        return false;
    }
    
    @Override
    public void moveInto (FacItemTube fromTube, TubeDirection fromDir, ItemIndex newItem) {
        throw new Error ("Tried moving into Deep Drill, should not be possible");
    }

    public void movementTick () {
        if (ticksSinceLastTake <= TICKS_PER_TAKE) {
            ticksSinceLastTake++;

        } else {
            if (transportingItem == null && 
                bufferTransportingItem == null) {
                    bufferTransportingItem = PRODUCT;
                    return;
            }
            ticksSinceLastTake = 0;

            if (tryToMoveIntoAdjInventory())
                return;

            if (tryToMoveIntoAdjTubeUnbiased())
                return;
        }

        // Normally, at the end there would something that holds onto
        // items incase no transport happened. But instead the drill
        // just discards them.
    }

}