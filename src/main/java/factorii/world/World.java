package factorii.world;

import factorii.GameState;
import factorii.factory.FacData;
import factorii.inventory.ItemIndex;

public class World {
    // These must be seperate;
    private Tile[][] terrain;    
    private Tile[][] resources;

    private int width;
    private int height;

    public int getWidth () { return width; }
    public int getHeight () { return height; }

    public World (Tile[][] terrain, Tile[][] resources, int playerX, int playerY) {
        this.terrain = terrain;
        this.resources = resources;

        width = terrain.length;
        height = terrain[0].length;
    }

    public World (Tile[][] terrain, Tile[][] resources) {
        this(terrain, resources, 0, 0);
    }

    /**
     * Combines the terrain, resources, AND factory layers (accessed via GameState)
     * to produce a Tile[][] representing the world
     */
    public Tile[][] getWorld () {
        Tile[][] combinedWorldTiles = new Tile[width][height];
        for (int i=0; i<width; i++)
            combinedWorldTiles[i] = terrain[i].clone();
        
        Tile[][] factory = GameState.factory.getFactoryLayer();
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Tile resource = resources[x][y];
                Tile factoryTile = factory[x][y];

                if (resource != Tile.EMPTY)
                    combinedWorldTiles[x][y] = resource;
                else if (factoryTile != Tile.EMPTY)
                    combinedWorldTiles[x][y] = factoryTile;
            }
        }

        combinedWorldTiles[GameState.player.getX()][GameState.player.getY()] = Tile.PLAYER;

        return combinedWorldTiles;
    }




    //
    // Factory Stuff
    //

    public boolean canStandAt (int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        Tile terrainTile = terrain[x][y];
        Tile resourceTile = resources[x][y];
        Tile factoryTile = GameState.factory.getFactoryLayer()[x][y];

        return Tile.canStandOn(terrainTile) &&
               Tile.canStandOn(resourceTile) &&
               Tile.canStandOn(factoryTile);
    }

    public ItemIndex harvestSpecific (int x, int y) {
        ItemIndex resource = Tile.tileToItem(resources[x][y]);
        resources[x][y] = Tile.EMPTY;
        return resource;
    }




    //
    // Player Actions
    //

    /**
     * Will find a tile adjacent to the player that can be harvested, 
     * remove it from the world, and add it to the player's inventory.
     */
    public void harvestAdjacentToPlayer () {
        // If standing on something -> harvest
        // else look around & harvest trees first, then ore
        Tile[][] factory = GameState.factory.getFactoryLayer();

        int playerX = GameState.player.getX();
        int playerY = GameState.player.getY();
        Tile testResource = resources[playerX][playerY];
        Tile testFactory = factory[playerX][playerY];

        if (Tile.canHarvest(testResource)) {
            GameState.player.getInventory().addItem(Tile.tileToItem(testResource));
            resources[playerX][playerY] = Tile.EMPTY;
            return;

        }

        for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = playerX + dx;
                int testY = playerY + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                testResource = resources[testX][testY];
                testFactory = factory[testX][testY];

                if (Tile.canHarvest(testResource)) { 
                    GameState.player.getInventory().addItem(Tile.tileToItem(testResource));
                    resources[testX][testY] = Tile.EMPTY;
                    return;

                }
            }
        }
    }

    /**
     * Returns true if successfully was able to place the tile
     */
    public boolean placeCraftedTile (Tile craftedTile, int x, int y) {
        if (canPlaceCraftedTileAt(craftedTile, x, y) == false)
            return false;

        // Most crafted tiles are factory tiles (i.e. are handled by the factory instance)
        // but the two cases checked for are different
        if (craftedTile == Tile.GROUND)
            terrain[x][y] = Tile.GROUND;
        else if (craftedTile == Tile.SUBMARINE)
            terrain[x][y] = Tile.SUBMARINE;
        else
            GameState.factory.placeFactoryTile(craftedTile, x, y);


        return true;
    }

    public boolean canPlaceCraftedTileAt (Tile craftedTile, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height ||
            (x == GameState.player.getX() && y == GameState.player.getY()))
                return false;

        Tile testTerrainTile = terrain[x][y];
        Tile testResourceTile = resources[x][y];
        Tile testFactoryTile = GameState.factory.getFactoryLayer()[x][y];

        switch (craftedTile) {
            case DEEP_DRILL:
                return testTerrainTile == Tile.GROUND &&
                       // Trees are not mine-able
                       testResourceTile != Tile.EMPTY && testResourceTile != Tile.TREE &&
                       testFactoryTile == Tile.EMPTY;
            
            case GROUND:
            case SUBMARINE:
                return testTerrainTile == Tile.WATER &&
                       testResourceTile == Tile.EMPTY &&
                       testFactoryTile == Tile.EMPTY;

            default:
                return testTerrainTile == Tile.GROUND &&
                       testResourceTile == Tile.EMPTY &&
                       testFactoryTile == Tile.EMPTY;
        }
    }




    //
    // Testing methods
    //

    public void setTile (Tile tile, int x, int y) {
        if (tile == Tile.GROUND || 
            tile == Tile.WATER)
                terrain[x][y] = tile;
    
        else
            resources[x][y] = tile;
    }


}
