package minirpg.factory;

import minirpg.inventory.ItemIndex;

public interface FacProducer {
    public void update ();
    public boolean canTakeProduct ();
    public ItemIndex takeProduct ();
}