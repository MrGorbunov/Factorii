package minirpg.world;

import java.awt.Color;

public enum Glyph {
    EMPTY (' ', new Color(0, 0, 0)),
    BOUNDS ('X', Color.GRAY),

    DIRT ('#', new Color(0.3f, 0.15f, 0.1f)),
    WATER ('#', new Color(0.13f, 0.15f, 0.5f)),

    TREE ('^', Color.GREEN),
    ORE_COAL ('*', new Color(0.3f, 0.4f, 0.5f)),
    PLAYER ('!', Color.WHITE);



    private char character;
    private Color color;
    
    public char getChar () { return character; }
    public Color getColor () { return color; }

    private Glyph (char character, Color color) {
        this.character = character;
        this.color = color;
    }
}