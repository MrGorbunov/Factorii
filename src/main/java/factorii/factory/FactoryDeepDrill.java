package factorii.factory;

import factorii.inventory.ItemIndex;
import factorii.world.Tile;

public class FactoryDeepDrill implements FacData, FacProducer {

    private final ItemIndex PRODUCT;
    private final int TICKS_PER_TAKE;

    private int ticksSinceLastTake;

    public Tile getTile () { return Tile.DEEP_DRILL; }

    public FactoryDeepDrill (ItemIndex product) {
        if (product == null)
            throw new NullPointerException("Product must be an actual item");

        this.PRODUCT = product;
        TICKS_PER_TAKE = 10;
    }



    public boolean canTakeProduct () { 
        return ticksSinceLastTake >= TICKS_PER_TAKE; 
    }

    public ItemIndex takeProduct () {
        ticksSinceLastTake = 0;
        return PRODUCT;
    }

    public void tickUpdate () {
        if (ticksSinceLastTake < TICKS_PER_TAKE)
            ticksSinceLastTake++;
    }

}