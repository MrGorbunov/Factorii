package minirpg.factory;

import minirpg.inventory.ItemIndex;
import minirpg.world.Tile;

public class FactoryMiningDrill implements FacData {

    private Tile tile;
    private ItemIndex resourceCollected;

    public FactoryMiningDrill (ItemIndex resourceCollected) {
        tile = Tile.MINING_DRILL;
        this.resourceCollected = resourceCollected;
    }

    public Tile getTile () { return tile; }
    public ItemIndex getResource () { return resourceCollected; }

}