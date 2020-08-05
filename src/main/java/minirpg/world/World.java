package minirpg.world;

import minirpg.GameState;
import minirpg.inventory.ItemIndex;

public class World {
    private Tile[][] terrain;    
    private Tile[][] resources; // TODO: rename to resources
    private Tile[][] factory;
    private PlayerTile player;

    // Used in order to be efficient
    private Tile[][] worldBuffer;
    private Tile[][] staticsBuffer;
    private int width;
    private int height;

    public int getWidth () { return width; }
    public int getHeight () { return height; }

    public int getPlayerX () { return player.getX(); }
    public int getPlayerY () { return player.getY(); }

    public Tile[][] getWorld () { return worldBuffer; }

    public World (Tile[][] terrain, Tile[][] interactables, Tile[][] factory, int playerX, int playerY) {
        this.terrain = terrain;
        this.resources = interactables;
        this.factory = factory;
        player = new PlayerTile(playerX, playerY);

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
                
                // Tile resource = interactables[x][y];
                // Tile factoryTile = factory[x][y];
                // if (resource == Tile.EMPTY) {
                //     if (factoryTile == Tile.EMPTY)
                //         continue;

                //     staticsBuffer[x][y] = factoryTile;

                // } else {
                //     staticsBuffer[x][y] = resource;
                // }
            }
        }
    }

    private void updateActives () {
        worldBuffer = new Tile[width][height];
        for (int i=0; i<width; i++)
            worldBuffer[i] = staticsBuffer[i].clone();
        
        worldBuffer[player.getX()][player.getY()] = player.getTile();
    }






    //
    // Input handling
    //
    // TODO: Get this out of here?

	public void moveUp () {
		int newY = player.getY() - 1;
		Tile testTile = worldBuffer[player.getX()][newY];
		if (newY < 0 ||
			testTile == Tile.BOUNDS || testTile == Tile.TREE) { 
				return; 
		}

		player.setY(newY);
		updateActives();
	}
	
	public void moveDown () {
		int newY = player.getY() + 1;
		Tile testTile = worldBuffer[player.getX()][newY];
		if (newY >= height ||
			testTile == Tile.BOUNDS || testTile == Tile.TREE) { 
				return; 
		}

		player.setY(newY);
		updateActives();
	}

	public void moveLeft () {
		int newX = player.getX() - 1;
		Tile testTile = worldBuffer[newX][player.getY()];
		if (newX < 0 ||
			testTile == Tile.BOUNDS || testTile == Tile.TREE) { 
				return; 
		}

		player.setX(newX);
		updateActives();
	}

	public void moveRight () {
		int newX = player.getX() + 1;
		Tile testTile = worldBuffer[newX][player.getY()];
		if (newX >= width ||
			testTile == Tile.BOUNDS || testTile == Tile.TREE) { 
				return; 
		}

		player.setX(newX);
		updateActives();
	}

    public void harvestAdjacent () {
        // If standing on something -> harvest
        // else look around & harvest trees first, then ore
        Tile standingOver = resources[getPlayerX()][getPlayerY()];
        if (standingOver == Tile.STONE) {
            GameState.inventory.addItem(ItemIndex.STONE);
            resources[getPlayerX()][getPlayerY()] = Tile.EMPTY;
            updateStatics();
            return;
        }

        int finalDx = 0;
        int finalDy = 0;
        Tile collectTile = Tile.EMPTY;

        outer : for (int dx=-1; dx<=1; dx++) {
            for (int dy=-1; dy<=1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int testX = getPlayerX() + dx;
                int testY = getPlayerY() + dy;
                if (testX < 0 || testX >= width || testY < 0 || testY >= height) continue;

                Tile testTile = resources[getPlayerX()+dx][getPlayerY()+dy];
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

        // No interactable found
        if (finalDx == 0 && finalDy == 0) return;

        // Do updating
        if (collectTile == Tile.STONE) { GameState.inventory.addItem(ItemIndex.STONE); }
        else if (collectTile == Tile.TREE) { GameState.inventory.addItem(ItemIndex.WOOD); }
        resources[getPlayerX()+finalDx][getPlayerY()+finalDy] = Tile.EMPTY;
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
            (x == getPlayerX() && y == getPlayerY()))
                return false;

        if (terrain[x][y] == Tile.WATER ||
            resources[x][y] != Tile.EMPTY ||
            factory[x][y] != Tile.EMPTY) // This will need changing when upgrading is implemented
                return false;
        
        return true;
    }

}
