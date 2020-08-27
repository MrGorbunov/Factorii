package factorii.factory;

import factorii.inventory.ItemIndex;

public interface FacProducer {
    public void tickUpdate ();
    public boolean canTakeProduct ();
    public ItemIndex takeProduct ();
}