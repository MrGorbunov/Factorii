package minirpg.subscreen;

import asciiPanel.AsciiPanel;
import minirpg.GameState;
import minirpg.world.*;

/*
World Subscreen

Responsible for drawing out a segment of the world
*/

public class WorldSubscreen {

    private int width;
    private int height;
    private int xOff;
    private int yOff;

    public WorldSubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;

        this.xOff = xOff;
        this.yOff = yOff;
    }

    /**
     * Sets xOff & yOff = 0 aka top-left corner
     */
    public WorldSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }





    //
    // Drawing Methods
    //

    public void drawSubscreen (AsciiPanel terminal) {
        Glyph[][] worldSlice = getGlyphSlice();

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Glyph g = worldSlice[x][y];
                terminal.write(g.getChar(), x + xOff, y + yOff, g.getColor());
            }
        }
    }

    // TODO: Right now this is center on player, will likely need to be changed
    private Glyph[][] getGlyphSlice () {
        World world = GameState.world;
        Tile[][] worldTiles = world.getWorld();
        Glyph[][] worldSlice = new Glyph[width][height];

        int startX = world.getPlayerX() - (width / 2);
        int startY = world.getPlayerY() - (height / 2);

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int worldX = x+startX;
                int worldY = y+startY;

                if (worldX < 0 || worldX >= world.getWidth() ||
                    worldY < 0 || worldY >= world.getHeight()) {
                        worldSlice[x][y] = Glyph.EMPTY;
                        continue;
                }
                
                worldSlice[x][y] = tileToGlyph(worldTiles[worldX][worldY]);
            }
        }

        return worldSlice;
    }




    //
    // Drawing Utility Methods
    //

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

}