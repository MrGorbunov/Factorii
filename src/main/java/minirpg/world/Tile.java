package minirpg.world;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
    WALL  ((char) 178, AsciiPanel.yellow), // filled in shade
    FLOOR ((char) 176, AsciiPanel.yellow), // light shade
    BOUNDS ( 'x', AsciiPanel.brightBlack);
    
    private char glyph;
    private Color color;

    public char glyph() { return glyph; }
    public Color color() { return color; }


    Tile (char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }

}