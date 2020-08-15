package minirpg.world;

import minirpg.GameState;
import minirpg.inventory.ItemIndex;

public enum Tile {
    EMPTY, // EMPTY should always be first so it's defaulted to
    BOUNDS,

    GROUND,
    WATER, 

    TREE,
    STONE,
    SAND,

    ORE_COAL,
    ORE_COPPER,
    ORE_IRON,

    PLAYER,

    MINING_DRILL, AUTO_MINING_UPGRADE, 
    WORKBENCH, COPPER_WORKBENCH, KILN, FORGE,
	WOODEN_FLOAT, BRONZE_FLOAT,
    CHEST, FUNNEL_WOOD, FUNNEL_COPPER, FUNNEL_IRON, FUNNEL_STEEL;
    




    //
    // Tile flags
    //
    // TODO: Update these flags to take crafted items into account

    public static boolean canStandOn (Tile tile) {
        return false ==
                (tile == Tile.TREE ||
                //  tile == Tile.WATER ||
                 tile == Tile.PLAYER);
            
    }

    public static boolean canHarvest (Tile tile) {
        // First 2 checks are for tiles that require equipment (shovel & pickaxe)
        if (tile == Tile.ORE_COAL || 
            tile == Tile.ORE_COPPER ||
            tile == Tile.ORE_IRON)
                return GameState.player.getInventory().getQuantity(ItemIndex.PICKAXE) > 0;
        
        else if (tile == Tile.SAND)
            return GameState.player.getInventory().getQuantity(ItemIndex.SHOVEL) > 0;

        return tile == Tile.TREE ||
               tile == Tile.STONE;
    }

    public static boolean canPlaceOn (Tile tile) {
        return tile == Tile.EMPTY ||
               tile == Tile.GROUND;
    }

    public static ItemIndex tileToItem (Tile tile) {
        switch (tile) {
            case TREE:
                return ItemIndex.WOOD;
            case STONE:
                return ItemIndex.STONE;
            case SAND:
                return ItemIndex.SAND;

            case ORE_COAL:
                return ItemIndex.ORE_COAL;
            case ORE_COPPER:
                return ItemIndex.ORE_COPPER;
            case ORE_IRON:
                return ItemIndex.ORE_IRON;

            case MINING_DRILL:
                return ItemIndex.MINING_DRILL;
            case AUTO_MINING_UPGRADE:
                return ItemIndex.AUTO_MINING_UPGRADE;
            case WORKBENCH:
                return ItemIndex.WORKBENCH;
            case COPPER_WORKBENCH:
                return ItemIndex.COPPER_WORKBENCH;
            case KILN:
                return ItemIndex.KILN;
            case FORGE:
                return ItemIndex.FORGE;

            case WOODEN_FLOAT:
                return ItemIndex.WOODEN_FLOAT;
            case BRONZE_FLOAT:
                return ItemIndex.BRONZE_FLOAT;

            case CHEST:
                return ItemIndex.CHEST;
            case FUNNEL_WOOD:
                return ItemIndex.FUNNEL_WOOD;
            case FUNNEL_COPPER:
                return ItemIndex.FUNNEL_COPPER;
            case FUNNEL_IRON:
                return ItemIndex.FUNNEL_IRON;
            case FUNNEL_STEEL:
                return ItemIndex.FUNNEL_STEEL;
        }

        System.out.println("WARNING: Undefined Tile to Item conversion");
        return ItemIndex.GLASS;
    }
}