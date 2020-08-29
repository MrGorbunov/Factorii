package factorii.world;

import java.awt.Color;

public enum Glyph {
    EMPTY  (' ', Convert.hex("#000000")),
    BOUNDS ('X', Convert.hex("#333333")),

    GROUND   ('#', Convert.hex("#302522")),
    WATER  ('#', Convert.hex("#080642")),

    TREE   ('$', Convert.hex("#44852C")),
    STONE  ('%', Convert.hex("#70605F")),
    SAND   ('#', Convert.hex("#4B4C29")),

    ORE_COAL   ('%', Convert.hex("#3E4049")),
    ORE_COPPER ('%', Convert.hex("#B5542E")),
    ORE_IRON   ('%', Convert.hex("#4C4C7D")),

    PLAYER ('!', Color.WHITE),

    MINING_DRILL        ('\'', Convert.hex("#B0D0EF")),
    AUTO_MINING_UPGRADE ('"',  Convert.hex("#D0E0FF")),
    WORKBENCH           ('H',  Convert.hex("#3C8338")),
    COPPER_WORKBENCH    ('H',  Convert.hex("#81C07D")),
    KILN                ('A',  Convert.hex("#754624")),
    IRON_KILN           ('A',  Convert.hex("#D48781")),
    FORGE               ('A',  Convert.hex("#788BDC")),
    STEEL_FORGE         ('A',  Convert.hex("#A3C6F3")),

    // No one-time items (yet?)
    // Boats, Tall Boots, Shovel, etc.

    CHEST           ('?', Convert.hex("#9F8C74")),
    ITEM_TUBE_GLASS ('+', Convert.hex("#D0E0FF")),
    ITEM_TUBE_STONE ('+', Convert.hex("#A0908F")),
    
    SUBMARINE ('O', Convert.hex("#7A9BC0"));
    




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

            case GROUND:  return Glyph.GROUND;
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
            case IRON_KILN:        return Glyph.IRON_KILN;
            case FORGE:            return Glyph.FORGE;
            case STEEL_FORGE:      return Glyph.STEEL_FORGE;

            case CHEST:           return Glyph.CHEST;
            case ITEM_TUBE_GLASS: return Glyph.ITEM_TUBE_GLASS;
            case ITEM_TUBE_STONE: return Glyph.ITEM_TUBE_STONE;

            case SUBMARINE: return Glyph.SUBMARINE;
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