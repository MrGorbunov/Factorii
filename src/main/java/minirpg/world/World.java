package minirpg.world;

import java.awt.Color;

public class World {
    private Tile[][] tiles;    
    private int width;
    private int height;

    public int width () { return width; }
    public int height () { return height; }

    public World (Tile[][] tiles) {
        this.tiles = tiles;
        width = tiles.length;
        height = tiles[0].length;
    }

    public Tile tile (int x, int y) {
        if (x < 0 || x > width || y < 0 || y > height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public char glyph (int x, int y) {
        if (tile(x,y) == null) {
            System.out.println(x + "," + y);
            return '?';
        }

        return tile(x, y).glyph();
    }

    public Color color (int x, int y) {
        if (tile(x,y) == null) {
            return Color.GRAY;
        }

        return tile(x, y).color();
    }

}
