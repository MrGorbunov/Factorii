package minirpg.factory;

import minirpg.world.Tile;

public class FactoryStatic implements FactoryData {

    private Tile tile;

    public FactoryStatic (Tile tile) {
        this.tile = tile;
    }

    public Tile getTile () {
        return tile;
    }

}