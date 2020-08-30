package factorii.inventory;

import factorii.world.Tile;

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
	WORKBENCH, COPPER_WORKBENCH, KILN, IRON_KILN, FORGE, STEEL_FORGE,
	BOAT, TALL_BOOTS, 
	LANDFILL,
	CHEST, ITEM_TUBE_GLASS, ITEM_TUBE_STONE, ITEM_TUBE_STEEL,
	SUBMARINE;

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
			
			case BAR_COPPER:
			case BAR_IRON:
			case GLASS:
			case ALLOY_BRONZE:
			case ALLOY_STEEL:
				System.out.println("WARNING: Attempted to convert crafted resource " + item + " into Tile. Not yet implemented");
				return Tile.EMPTY;

            case MINING_DRILL:        return Tile.MINING_DRILL;
            case AUTO_MINING_UPGRADE: return Tile.AUTO_MINING_UPGRADE;

            case WORKBENCH:        return Tile.WORKBENCH;
            case COPPER_WORKBENCH: return Tile.COPPER_WORKBENCH;
			case KILN:             return Tile.KILN;
			case IRON_KILN:		   return Tile.IRON_KILN;
			case FORGE:            return Tile.FORGE;
			case STEEL_FORGE:	   return Tile.STEEL_FORGE;

			case LANDFILL: return Tile.GROUND;

            case CHEST:           return Tile.CHEST;
            case ITEM_TUBE_GLASS: return Tile.ITEM_TUBE_GLASS;
			case ITEM_TUBE_STONE: return Tile.ITEM_TUBE_STONE;
			case ITEM_TUBE_STEEL: return Tile.ITEM_TUBE_STEEL;
			
			case SUBMARINE: return Tile.SUBMARINE;

			case PICKAXE:
			case SHOVEL:
			case BOAT:
			case TALL_BOOTS:
				throw new Error("Attempted to convert equipment into tile. Equipment should never be on an item tube/ displayed");
		}

		throw new Error("Missing case in item -> tile conversion");
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
			
			case LANDFILL:
				return "Landfill";

			case CHEST:
				return "Chest";
			case ITEM_TUBE_GLASS:
				return "Glass Item Tube";
			case ITEM_TUBE_STONE:
				return "Stone Item Tube";
			case ITEM_TUBE_STEEL:
				return "Steel Item Tube";

			case KILN:
				return "Kiln";
			case IRON_KILN:
				return "Iron Kiln";
			case FORGE:
				return "Forge";
			case STEEL_FORGE:
				return "Steel Forge";

			case SUBMARINE:
				return "Submarine";

			default:
				throw new Error("Unmatched case in .toString() for ItemIndex");
		}
	}

}
