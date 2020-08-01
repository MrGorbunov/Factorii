package minirpg.world;

import java.awt.event.KeyEvent;

import minirpg.GameState;
import minirpg.inventory.ItemIndex;

public class World {
    private Tile[][] terrain;    
    private Tile[][] interactables;
    private PlayerTile player;

    // Used to be efficient
    private Tile[][] worldBuffer;
    private Tile[][] staticsBuffer;
    private int width;
    private int height;

    public int width () { return width; }
    public int height () { return height; }

    public int getPlayerX () { return player.getX(); }
    public int getPlayerY () { return player.getY(); }

    public World (Tile[][] terrain, Tile[][] interactables, int playerX, int playerY) {
        this.terrain = terrain;
        this.interactables = interactables;
        player = new PlayerTile(playerX, playerY);

        width = terrain.length;
        height = terrain[0].length;

        updateStatics();
        updateActives();
    }

    public World (Tile[][] terrain, Tile[][] interactables) {
        this(terrain, interactables, 0, 0);
    }




    private void updateStatics () {
        staticsBuffer = new Tile[width][height];
        for (int i=0; i<width; i++)
            staticsBuffer[i] = terrain[i].clone();
        
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Tile interact = interactables[x][y];
                if (interact == Tile.EMPTY)
                    continue;
                
                staticsBuffer[x][y] = interact;
            }
        }
    }

    private void updateActives () {
        worldBuffer = new Tile[width][height];
        for (int i=0; i<width; i++)
            worldBuffer[i] = staticsBuffer[i].clone();
        
        worldBuffer[player.getX()][player.getY()] = player.getTile();
    }





    public Glyph[][] getWorldSlice (int sliceWidth, int sliceHeight, int xOff, int yOff) {
        Glyph[][] worldSlice = new Glyph[sliceWidth][sliceHeight];

        int pad = 10;

        int maxX = width - sliceWidth / 2 + pad;
        int minX = sliceWidth / 2 - pad;
        int centerX = Math.max(minX, Math.min(maxX, getPlayerX())) + xOff;

        int maxY = height - sliceHeight / 2 + pad;
        int minY = sliceHeight / 2 - pad;
        int centerY = Math.max(minY, Math.min(maxY, getPlayerY())) + yOff;

        int startX = centerX - (sliceWidth / 2);
        int startY = centerY - (sliceHeight / 2);

        for (int x=0; x<sliceWidth; x++) {
            for (int y=0; y<sliceHeight; y++) {
                int worldX = x+startX;
                int worldY = y+startY;

                if (worldX < 0 || worldX >= width ||
                    worldY < 0 || worldY >= height) {
                        worldSlice[x][y] = Glyph.EMPTY;
                        continue;
                }
                
                worldSlice[x][y] = tileToGlyph(worldBuffer[worldX][worldY]);
            }
        }

        return worldSlice;
    }

    private Glyph tileToGlyph (Tile tile) {
        switch (tile) {
            case EMPTY:
                return Glyph.EMPTY;

            case BOUNDS:
                return Glyph.BOUNDS;

            case DIRT:
                return Glyph.DIRT;

            case WATER:
                return Glyph.WATER;



            case TREE:
                return Glyph.TREE;

            case STONE:
                return Glyph.STONE;

            case ORE_COAL:
                return Glyph.ORE_COAL;



            case PLAYER:
                return Glyph.PLAYER;

        }

        return Glyph.EMPTY;
    }





    public void handleNewInput (int keyCode) {
        int newX = player.getX();
        int newY = player.getY();

        if      (keyCode == KeyEvent.VK_LEFT) { newX -= 1; }
        else if (keyCode == KeyEvent.VK_RIGHT) { newX += 1; }
        else if (keyCode == KeyEvent.VK_UP) { newY -= 1; }
        else if (keyCode == KeyEvent.VK_DOWN) { newY += 1; }
        else if (keyCode == KeyEvent.VK_SPACE) { harvestAdjacent(); updateStatics(); }
        else { return; }

        if (newX < 0 || newX >= width || newY < 0 || newY >= height)
            return;

        Tile testTile = worldBuffer[newX][newY];
        if (testTile == Tile.BOUNDS || testTile == Tile.TREE)
            return;
        
        player.setX(newX);
        player.setY(newY);
        updateActives();
    }

    private void harvestAdjacent () {
        // If standing on something -> harvest
        // else look around & harvest trees first, then ore
        Tile standingOver = interactables[getPlayerX()][getPlayerY()];
        if (standingOver == Tile.STONE) {
            GameState.inventory.addItem(ItemIndex.STONE);
            interactables[getPlayerX()][getPlayerY()] = Tile.EMPTY;
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

                Tile testTile = interactables[getPlayerX()+dx][getPlayerY()+dy];
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
        interactables[getPlayerX()+finalDx][getPlayerY()+finalDy] = Tile.EMPTY;
        updateStatics();
    }

}
