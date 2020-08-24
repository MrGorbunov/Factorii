package factorii.subscreen;

import asciiPanel.AsciiPanel;
import factorii.GameState;
import factorii.world.*;

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

        int startX = GameState.player.getX() - (width / 2);
        int startY = GameState.player.getY() - (height / 2);

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int worldX = x+startX;
                int worldY = y+startY;

                if (worldX < 0 || worldX >= world.getWidth() ||
                    worldY < 0 || worldY >= world.getHeight()) {
                        worldSlice[x][y] = Glyph.EMPTY;
                        continue;
                }
                
                worldSlice[x][y] = Glyph.tileToGlyph(worldTiles[worldX][worldY]);
            }
        }

        return worldSlice;
    }



}