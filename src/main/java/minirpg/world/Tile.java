package minirpg.world;

public enum Tile {
    EMPTY, // EMPTY should always be first so it's defaulted to
    BOUNDS,

    DIRT,
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
    
}