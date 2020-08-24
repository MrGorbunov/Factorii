package factorii.world;

import factorii.GameState;
import factorii.factory.FacData;
import factorii.factory.FactoryMiningDrill;
import factorii.inventory.CraftingLocation;
import factorii.inventory.ItemIndex;

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
        
        factory = GameState.factory.getFactoryLayer();
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
    // Factory Stuff
    //

    public FacData getAdjacentFactoryData () {
        int playerX = GameState.player.getX();
        int playerY = GameState.player.getY();
        Tile standingOver = factory[playerX][playerY];
        if (standingOver != Tile.EMPTY) {
            return GameState.factory.getFacData(playerX, playerY);
        }

        for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = playerX + dx;
                int testY = playerY + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                Tile testTile = factory[testX][testY];
                if (testTile != Tile.EMPTY) 
                    return GameState.factory.getFacData(testX, testY);
            }
        }

        return null;
    }

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
        if (testLocation != CraftingLocation.PLAYER) {
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
                if (testLocation == CraftingLocation.PLAYER) continue;

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
        if (testTile == Tile.WORKBENCH ||
            testTile == Tile.COPPER_WORKBENCH)
                return CraftingLocation.WORKBENCH;
        
        if (testTile == Tile.KILN)
            return CraftingLocation.KILN;

        if (testTile == Tile.FORGE)
            return CraftingLocation.FORGE;
        
        return CraftingLocation.PLAYER;
    }

    public boolean canStandAt (int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        Tile terrainTile = terrain[x][y];
        Tile resourceTile = resources[x][y];
        Tile factoryTile = factory[x][y];

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

    public void harvestAdjacent () {
        // If standing on something -> harvest
        // else look around & harvest trees first, then ore
        int playerX = GameState.player.getX();
        int playerY = GameState.player.getY();
        Tile testResource = resources[playerX][playerY];
        Tile testFactory = factory[playerX][playerY];

        if (Tile.canHarvest(testResource)) {
            GameState.player.getInventory().addItem(Tile.tileToItem(testResource));
            resources[playerX][playerY] = Tile.EMPTY;
            updateStatics();
            return;

        } else if (testFactory == Tile.MINING_DRILL) {
            FactoryMiningDrill drill = (FactoryMiningDrill) GameState.factory.getFacData(playerX, playerY);
            ItemIndex extractedResource = drill.getResource();
            GameState.player.getInventory().addItem(extractedResource);
            // Doesn't change map so no need to update anything
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
                    updateStatics();
                    return;

                } else if (testFactory == Tile.MINING_DRILL) {
                    FactoryMiningDrill drill = (FactoryMiningDrill) GameState.factory.getFacData(testX, testY);
                    ItemIndex extractedResource = drill.getResource();
                    GameState.player.getInventory().addItem(extractedResource);
                    return;
                }
            }
        }
    }

    /**
     * Returns true if successfully was able to place the tile
     */
    public boolean placeFactoryTile (Tile factoryTile, int x, int y) {
        if (canPlaceAt(factoryTile, x, y) == false)
            return false;

        GameState.factory.placeFactoryTile(factoryTile, x, y);

        updateStatics();
        updateActives();
        return true;
    }

    public boolean canPlaceAt (Tile factoryTile, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height ||
            (x == GameState.player.getX() && y == GameState.player.getY()))
                return false;

        Tile testTerrainTile = terrain[x][y];
        Tile testResourceTile = resources[x][y];
        Tile testFactoryTile = factory[x][y];

        switch (factoryTile) {
            case MINING_DRILL:
                return testTerrainTile == Tile.GROUND &&
                       // Trees are not renewable
                       testResourceTile != Tile.EMPTY && testResourceTile != Tile.TREE &&
                       testFactoryTile == Tile.EMPTY;

            case BRONZE_FLOAT:
            case WOODEN_FLOAT:
                return testTerrainTile == Tile.WATER &&
                       testResourceTile == Tile.EMPTY &&
                       testFactoryTile == Tile.EMPTY;

            default:
                return testTerrainTile == Tile.GROUND &&
                       testResourceTile == Tile.EMPTY &&
                       testFactoryTile == Tile.EMPTY;
        }
    }

}
