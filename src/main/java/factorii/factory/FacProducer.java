package factorii.factory;

import factorii.inventory.ItemIndex;

public interface FacProducer {
    public void update ();
    public boolean canTakeProduct ();
    public ItemIndex takeProduct ();
}