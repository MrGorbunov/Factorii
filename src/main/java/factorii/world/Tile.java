package factorii.world;

import java.awt.Color;

import factorii.GameState;
import factorii.inventory.ItemIndex;

public enum Tile {
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

    CHEST           ('?', Convert.hex("#9F8C74")),
    ITEM_TUBE_GLASS ('+', Convert.hex("#D0E0FF")),
    ITEM_TUBE_STONE ('+', Convert.hex("#A0908F")),
    ITEM_TUBE_STEEL ('+', Convert.hex("#788BDC")),
    
    SUBMARINE ('O', Convert.hex("#7A9BC0"));




    //
    // Graphical stuff
    //

    private char character;
    private Color color;

    public char getChar () { return character; }
    public Color getColor () { return color; }

    private Tile (char character, Color color) {
        this.character = character;
        this.color = color;
    }




    //
    // Tile flags
    //

    public static boolean canStandOn (Tile tile) {
        switch (tile) {
            case TREE:
            case PLAYER:
                return false;

            case WATER:
                // Check if boat has been crafted
                return GameState.player.getInventory().getQuantity(ItemIndex.BOAT) > 0;

            case MINING_DRILL:
            case AUTO_MINING_UPGRADE:
            case WORKBENCH:
            case COPPER_WORKBENCH:
            case KILN:
            case IRON_KILN:
            case FORGE:
            case STEEL_FORGE:
            case CHEST:
                return GameState.player.getInventory().getQuantity(ItemIndex.TALL_BOOTS) > 0;

            default:
                return true;
        }
    }

    public static boolean canHarvest (Tile tile) {
        return tile == Tile.TREE ||
               tile == Tile.STONE ||
               tile == Tile.SAND ||
               tile == Tile.ORE_COAL ||
               tile == Tile.ORE_COPPER ||
               tile == Tile.ORE_IRON;
    }

    /**
     * When deciding what tile to interact with, this value
     * is used for the comparision.
     * High value = priority
     * -1 = not interactable
     */
    public static int interactabilityPriotiy (Tile tile) {
        switch (tile) {
            case COPPER_WORKBENCH:
            case IRON_KILN:
            case STEEL_FORGE:
                return 3;

            case CHEST:
                return 2;

            case KILN:
            case FORGE:
            case WORKBENCH:
                return 1;
                
            case ITEM_TUBE_STEEL:
                return 0;

            default:
                return -1;
        }
    }

    public static ItemIndex tileToItem (Tile tile) {
        switch (tile) {
            case TREE:  return ItemIndex.WOOD;
            case STONE: return ItemIndex.STONE;
            case SAND:  return ItemIndex.SAND;

            case ORE_COAL:   return ItemIndex.ORE_COAL;
            case ORE_COPPER: return ItemIndex.ORE_COPPER;
            case ORE_IRON:   return ItemIndex.ORE_IRON;

            case MINING_DRILL:        return ItemIndex.MINING_DRILL;
            case AUTO_MINING_UPGRADE: return ItemIndex.AUTO_MINING_UPGRADE;
            case WORKBENCH:           return ItemIndex.WORKBENCH;
            case COPPER_WORKBENCH:    return ItemIndex.COPPER_WORKBENCH;
            case KILN:                return ItemIndex.KILN;
            case IRON_KILN:           return ItemIndex.IRON_KILN;
            case FORGE:               return ItemIndex.FORGE;
            case STEEL_FORGE:         return ItemIndex.STEEL_FORGE;

            case CHEST:           return ItemIndex.CHEST;
            case ITEM_TUBE_GLASS: return ItemIndex.ITEM_TUBE_GLASS;
            case ITEM_TUBE_STONE: return ItemIndex.ITEM_TUBE_STONE;
            case ITEM_TUBE_STEEL: return ItemIndex.ITEM_TUBE_STEEL;
            
            case SUBMARINE: return ItemIndex.SUBMARINE;
        }

        throw new Error ("Attempted to do Tile -> Item conversion which does not exist");
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

