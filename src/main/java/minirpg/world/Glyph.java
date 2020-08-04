package minirpg.world;

import java.awt.Color;

public enum Glyph {
    EMPTY (' ',  Convert.hex("#000000")),
    BOUNDS ('X', Convert.hex("#333333")),

    DIRT ('#',  Convert.hex("#422E27")),
    WATER ('#', Convert.hex("#080642")),

    TREE ('^',  Convert.hex("#68C842")),
    STONE ('%', Convert.hex("#70605F")),
    SAND ('#',  Convert.hex("#9D9C1C")),

    ORE_COAL ('%',  Convert.hex("#2A2C36")),
    ORE_COPPER ('%',Convert.hex("#B5542E")),
    ORE_IRON ('%',  Convert.hex("#4C4C7D")),

    WORKBENCH ('@', Convert.hex("#D48781")),

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

class Convert {
    public static Color hex (String hexInput) {
        int red =   Integer.decode("0x" + hexInput.substring(1, 3));
        int green = Integer.decode("0x" + hexInput.substring(3, 5));
        int blue =  Integer.decode("0x" + hexInput.substring(5, 7));

        return new Color(red / 255.0f, green / 255.0f, blue / 255.0f);
    }
}