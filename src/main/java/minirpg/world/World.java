package minirpg.world;

import minirpg.GameState;
import minirpg.inventory.CraftingLocation;
import minirpg.inventory.ItemIndex;

public class World {
    private Tile[][] terrain;    
    private Tile[][] resources;
    private Tile[][] factory; // Gotten from a different class (Factory Layer)

    // Used in order to be efficient
    private Tile[][] worldBuffer;
    private Tile[][] staticsBuffer;
    private int width;
    private int height;

    public int getWidth () { return width; }
    public int getHeight () { return height; }

    public Tile[][] getWorld () { return worldBuffer; }

    public World (Tile[][] terrain, Tile[][] interactables, Tile[][] factory, int playerX, int playerY) {
        this.terrain = terrain;
        this.resources = interactables;
        this.factory = factory;

        width = terrain.length;
        height = terrain[0].length;

        updateStatics();
        updateActives();
    }

    public World (Tile[][] terrain, Tile[][] interactables, Tile[][] factory) {
        this(terrain, interactables, factory, 0, 0);
    }

    private void updateStatics () {
        staticsBuffer = new Tile[width][height];
        for (int i=0; i<width; i++)
            staticsBuffer[i] = terrain[i].clone();
        
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Tile resource = resources[x][y];
                Tile factoryTile = factory[x][y];

                if (resource != Tile.EMPTY)
                    staticsBuffer[x][y] = resource;
                else if (factoryTile != Tile.EMPTY)
                    staticsBuffer[x][y] = factoryTile;
            }
        }
    }

    private void updateActives () {
        worldBuffer = new Tile[width][height];
        for (int i=0; i<width; i++)
            worldBuffer[i] = staticsBuffer[i].clone();
        
        worldBuffer[GameState.player.getX()][GameState.player.getY()] = Tile.PLAYER;
    }

    public void refresh () {
        updateStatics();
        updateActives();
    }




    //
    // Preliminary Player Stuff
    //

    /**
     * Checks what crafting location the player is at.
     * First checks if standing over anything. Then checks
     * for directly adjacent crafting places, prioritizing
     * the highest level ((Copper) WorkBench < Kiln < Forge).
     */
    public CraftingLocation getCraftingLocation () {
        // If standing on a craftin surface -> use that
        // else look around & harvest trees first, then ore
        int playerX = GameState.player.getX();
        int playerY = GameState.player.getY();
        Tile standingOver = factory[playerX][playerY];
        CraftingLocation testLocation = checkTileCrafting(standingOver);
        if (testLocation != null) {
            return testLocation;
        }

        CraftingLocation highestLocation = CraftingLocation.PLAYER;

        for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = playerX + dx;
                int testY = playerY + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                Tile testTile = factory[testX][testY];
                testLocation = checkTileCrafting(testTile);
                if (testLocation == null) continue;

                if (testLocation.ordinal() > highestLocation.ordinal()) {
                        highestLocation = testLocation;
                }
            }
        }

        // Defaults to player if nothing found
        return highestLocation;
    }

    /**
     * Checks if the input tiles is a crafting location, returns
     * null otherwise. Does NOT return player type; only
     * WORKBENCH, KILN, FORGE.
     */
    private CraftingLocation checkTileCrafting (Tile testTile) {
        if (testTile == Tile.WORKBENCH || testTile == Tile.COPPER_WORKBENCH)
            return CraftingLocation.WORKBENCH;
        
        if (testTile == Tile.KILN)
            return CraftingLocation.KILN;

        if (testTile == Tile.FORGE)
            return CraftingLocation.FORGE;
        
        return null;
    }





    //
    // Player stuff
    //

    public boolean canStandOn (int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        Tile terrainTile = terrain[x][y];
        Tile resourceTile = resources[x][y];
        Tile factoryTile = factory[x][y];

        // if (terrainTile == Tile.WATER) return false;
        if (resourceTile == Tile.TREE) return false;
        // TODO: Figure out if you can walk on the factory

        return true;
    }

    public void harvestAdjacent () {
        // If standing on something -> harvest
        // else look around & harvest trees first, then ore
        int playerX = GameState.player.getX();
        int playerY = GameState.player.getY();
        Tile standingOver = resources[playerX][playerY];
        if (standingOver == Tile.STONE) {
            GameState.player.getInventory().addItem(ItemIndex.STONE);
            resources[playerX][playerY] = Tile.EMPTY;
            updateStatics();
            return;
        }

        int finalDx = 0;
        int finalDy = 0;
        Tile collectTile = Tile.EMPTY;

        outer : for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = playerX + dx;
                int testY = playerY + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                Tile testTile = resources[testX][testY];
                if (testTile == Tile.EMPTY) continue;

                finalDx = dx;
                finalDy = dy;
                collectTile = testTile;

                // Trees always are always preffered
                if (testTile == Tile.TREE) { 
                    break outer;
                } 
            }
        }

        // No resources found
        if (finalDx == 0 && finalDy == 0) return;

        // Do updating
        if (collectTile == Tile.STONE) { GameState.player.getInventory().addItem(ItemIndex.STONE); }
        else if (collectTile == Tile.TREE) { GameState.player.getInventory().addItem(ItemIndex.WOOD); }
        resources[playerX + finalDx][playerY + finalDy] = Tile.EMPTY;
        updateStatics();
		updateActives();
    }

    /**
     * Returns true if successfully was able to place the tile
     */
    public boolean placeFactoryTile (Tile factoryTile, int x, int y) {
        if (canPlaceAt(x, y) == false)
            return false;

        factory[x][y] = factoryTile;
        updateStatics();
        updateActives();
        return true;
    }

    public boolean canPlaceAt (int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height ||
            (x == GameState.player.getX() && y == GameState.player.getY()))
                return false;

        if (terrain[x][y] == Tile.WATER ||
            resources[x][y] != Tile.EMPTY ||
            factory[x][y] != Tile.EMPTY) // This will need changing when upgrading is implemented
                return false;
        
        return true;
    }

}
