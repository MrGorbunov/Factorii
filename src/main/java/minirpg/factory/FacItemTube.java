package minirpg.factory;

import minirpg.inventory.ItemIndex;

public interface FacItemTube {
    public void refresh (FacData[][] factory, int x, int y);
    
    public boolean canMoveInto ();
    public void moveInto (FacItemTube previousTube, ItemIndex trasportingItem);
    public void movementTick ();
    public void bufferTick ();

}