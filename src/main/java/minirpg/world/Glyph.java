package minirpg.world;

import java.awt.Color;

public enum Glyph {
    EMPTY  (' ', Convert.hex("#000000")),
    BOUNDS ('X', Convert.hex("#333333")),

    DIRT   ('#', Convert.hex("#422E27")),
    WATER  ('#', Convert.hex("#080642")),

    TREE   ('$', Convert.hex("#366923")),
    STONE  ('%', Convert.hex("#70605F")),
    SAND   ('#', Convert.hex("#9D9C1C")),

    ORE_COAL   ('%', Convert.hex("#2A2C36")),
    ORE_COPPER ('%', Convert.hex("#B5542E")),
    ORE_IRON   ('%', Convert.hex("#4C4C7D")),

    PLAYER ('!', Color.WHITE),

    MINING_DRILL        ('\'', Convert.hex("#B0D0EF")),
    AUTO_MINING_UPGRADE ('"',  Convert.hex("#D0E0FF")),

    WORKBENCH        ('@',  Convert.hex("#8D6C54")),
    COPPER_WORKBENCH ('@',  Convert.hex("#D48781")),
    KILN             ('@', Convert.hex("#788BDC")),
    FORGE            ('@', Convert.hex("#C6E3FF")),

    WOODEN_FLOAT ('&', Convert.hex("#7D5C44")),
    BRONZE_FLOAT ('&', Convert.hex("#AB8546")),

    // No one-time items (yet?)
    // Boats, Tall Boots, Shovel, etc.

    CHEST         ('?', Convert.hex("#9F8C74")),
    FUNNEL_WOOD   ('+', Convert.hex("#8D6C54")),
    FUNNEL_COPPER ('+', Convert.hex("#BD8349")),
    FUNNEL_IRON   ('+', Convert.hex("#77939C")),
    FUNNEL_STEEL  ('+', Convert.hex("#ADD6DC"));
    




    private char character;
    private Color color;
    
    public char getChar () { return character; }
    public Color getColor () { return color; }

    private Glyph (char character, Color color) {
        this.character = character;
        this.color = color;
    }

    public static Glyph tileToGlyph (Tile tile) {
        switch (tile) {
            case EMPTY:  return Glyph.EMPTY;
            case BOUNDS: return Glyph.BOUNDS;

            case DIRT:  return Glyph.DIRT;
            case WATER: return Glyph.WATER;

            case TREE:  return Glyph.TREE;
            case STONE: return Glyph.STONE;
            case SAND:  return Glyph.SAND;

            case ORE_COAL:   return Glyph.ORE_COAL;
            case ORE_COPPER: return Glyph.ORE_COPPER;
            case ORE_IRON:   return Glyph.ORE_IRON;

            case PLAYER: return Glyph.PLAYER;

            case MINING_DRILL:        return Glyph.MINING_DRILL;
            case AUTO_MINING_UPGRADE: return Glyph.AUTO_MINING_UPGRADE;

            case WORKBENCH:        return Glyph.WORKBENCH;
            case COPPER_WORKBENCH: return Glyph.COPPER_WORKBENCH;
            case KILN:             return Glyph.KILN;
            case FORGE:            return Glyph.FORGE;

            case WOODEN_FLOAT: return Glyph.WOODEN_FLOAT;
            case BRONZE_FLOAT: return Glyph.BRONZE_FLOAT;

            case CHEST:         return Glyph.CHEST;
            case FUNNEL_WOOD:   return Glyph.FUNNEL_WOOD;
            case FUNNEL_COPPER: return Glyph.FUNNEL_COPPER;
            case FUNNEL_IRON:   return Glyph.FUNNEL_IRON;
            case FUNNEL_STEEL:  return Glyph.FUNNEL_STEEL;
        }

        return Glyph.EMPTY;
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