package minirpg.world;

import java.awt.event.KeyEvent;

public class World {
    private Tile[][] terrain;    
    private Tile[][] interactables;
    private Player player;

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
        player = new Player(playerX, playerY);

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





    public Glyph[][] getWorldSlice (int sliceWidth, int sliceHeight) {
        Glyph[][] worldSlice = new Glyph[sliceWidth][sliceHeight];

        // Remember, integer arithmatic; the +1 means that startX - endX always equals screenWidth
        int startX = getPlayerX() - (sliceWidth / 2);
        int startY = getPlayerY() - (sliceHeight / 2);

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

}
