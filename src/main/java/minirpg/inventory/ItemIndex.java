package minirpg.inventory;

import minirpg.world.Tile;

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
	PICKAXE, SHOVEL, MINING_DRILL, AUTO_MINING_UPGRADE,
	WORKBENCH, COPPER_WORKBENCH, KILN, FORGE,
	BOAT, TALL_BOOTS, WOODEN_FLOAT, BRONZE_FLOAT,
	CHEST, FUNNEL_WOOD, FUNNEL_COPPER, FUNNEL_IRON, FUNNEL_STEEL;

	public boolean isResource () {
		return this.ordinal() <= ItemIndex.ALLOY_STEEL.ordinal();
	}

	public boolean isPlacable () {
		return this.ordinal() > ItemIndex.ALLOY_STEEL.ordinal()
			   && !isEquipable();
	}

	// Stuff you only craft once 
	public boolean isEquipable () {
		return this == PICKAXE ||
			   this == SHOVEL ||
			   this == BOAT ||
			   this == TALL_BOOTS;
	}

	public static Tile itemToTile (ItemIndex item) {
        switch (item) {
            case WOOD:  return Tile.TREE;
            case STONE: return Tile.STONE;
            case SAND:  return Tile.SAND;

            case ORE_COAL:   return Tile.ORE_COAL;
            case ORE_COPPER: return Tile.ORE_COPPER;
            case ORE_IRON:   return Tile.ORE_IRON;

            case MINING_DRILL:        return Tile.MINING_DRILL;
            case AUTO_MINING_UPGRADE: return Tile.AUTO_MINING_UPGRADE;

            case WORKBENCH:        return Tile.WORKBENCH;
            case COPPER_WORKBENCH: return Tile.COPPER_WORKBENCH;
            case KILN:             return Tile.KILN;
            case FORGE:            return Tile.FORGE;

            case WOODEN_FLOAT: return Tile.WOODEN_FLOAT;
            case BRONZE_FLOAT: return Tile.BRONZE_FLOAT;

            case CHEST:         return Tile.CHEST;
            case FUNNEL_WOOD:   return Tile.FUNNEL_WOOD;
            case FUNNEL_COPPER: return Tile.FUNNEL_COPPER;
            case FUNNEL_IRON:   return Tile.FUNNEL_IRON;
            case FUNNEL_STEEL:  return Tile.FUNNEL_STEEL;
        }

        return Tile.EMPTY;
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
			case MINING_DRILL:
				return "Mining Drill";
			case AUTO_MINING_UPGRADE:
				return "Auto Mining Upgrade";
			case WORKBENCH:
				return "Workbench";
			case COPPER_WORKBENCH:
				return "Copper Workbench";

			case BOAT:
				return "Boat";
			case TALL_BOOTS:
				return "Tall Boots";
			case WOODEN_FLOAT:
				return "Wooden Float";
			case BRONZE_FLOAT:
				return "Bronze Float";
			
			case CHEST:
				return "Chest";
			case FUNNEL_WOOD:
				return "Wooden Funnel";
			case FUNNEL_COPPER:
				return "Copper Funnel";
			case FUNNEL_IRON:
				return "Iron Funnel";
			case FUNNEL_STEEL:
				return "Steel Funnel";

			case KILN:
				return "Kiln";
			case FORGE:
				return "Forge";
		}

		return "";
	}

}
