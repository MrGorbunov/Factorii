package minirpg.inventory;

// Instead of a hashmap, inventory is an
// int[], and this enum stores the indicies
// of the different items

// The order here does NOT matter and is only grouped
// for readability

public enum ItemIndex {
	// Raw Resources
	WOOD, STONE, 
	ORE_COAL, ORE_COPPER, ORE_IRON,
	SAND,

	// Craftable Resources
	BAR_COPPER, BAR_IRON, GLASS, 
	ALLOY_BRONZE, ALLOY_STEEL,

	// Craftable Items
	PICKAXE, SHOVEL,
	WORKBENCH, COPPER_WORKBENCH;

}
