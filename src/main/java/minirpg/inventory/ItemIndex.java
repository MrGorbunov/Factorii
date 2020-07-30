package minirpg.inventory;

// Instead of a hashmap, inventory is an
// int[], and this enum stores the indicies
// of the different items

public enum ItemIndex {
	// Raw Resources
	WOOD, STONE, 
	ORE_COAL, ORE_COPPER, ORE_IRON,
	SAND,

	// Craftable Resources
	GLASS, ALLOY_BRONZE, ALLOY_STEEL,

	// Craftable Items
	WORKBENCH, COPPER_WORKBENCH;
}
