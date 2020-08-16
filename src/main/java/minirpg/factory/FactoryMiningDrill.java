package minirpg.factory;

import minirpg.inventory.ItemIndex;
import minirpg.world.Tile;

public class FactoryMiningDrill implements FactoryData {

    private Tile tile;
    private ItemIndex resourceCollected;

    public FactoryMiningDrill (Tile tile, ItemIndex resourceCollected) {
        this.tile = tile;
        this.resourceCollected = resourceCollected;
    }

    public Tile getTile () { return tile; }
    public ItemIndex getResource () { return resourceCollected; }

}