package minirpg.inventory;

// Instead of a hashmap, inventory is an
// int[], and this enum stores the indicies
// of the different items

// The order here slightly mattters
// All resources must be placed before ALLOY_STEEL
// resource = used in crafting
// items = (with some exception) not usable in other crafts

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

	public boolean isResource () {
		return this.ordinal() <= ItemIndex.ALLOY_STEEL.ordinal();
	}

	@Override
	public String toString () {
		switch (this) {
			case WOOD:
				return "Wood";
			case STONE:
				return "Stone";
			case ORE_COAL:
				return "Coal";
			case ORE_COPPER:
				return "Copper Ore";
			case ORE_IRON:
				return "Iron Ore";
			case SAND:
				return "Sand";
			case BAR_COPPER:
				return "Copper Bar";
			case BAR_IRON:
				return "Iron Bar";
			case GLASS:
				return "Glass";
			case ALLOY_BRONZE:
				return "Bronze Alloy";
			case ALLOY_STEEL:
				return "Steel Alloy";

			case PICKAXE:
				return "Pickaxe";
			case SHOVEL:
				return "Shovel";
			case WORKBENCH:
				return "Workbench";
			case COPPER_WORKBENCH:
				return "Copper Workbench";

			default:
				return "";
		}
	}

}
