package factorii.inventory;

import java.util.ArrayList;

import factorii.GameState;
import factorii.TechLevel;
import factorii.inventory.*;

public class CraftingGlobals {

	// What you can make a workbench given tech level
	public CraftingRecipe[] starterCrafts;
	public CraftingRecipe[] workbenchCrafts;
	public CraftingRecipe[] kilnCrafts;
    public CraftingRecipe[] forgeCrafts;
    
    // Equipment is weird because they're unlocked but also 1-time crafts
    public CraftingRecipe[] equipmentCrafts;
    public TechLevel[] equipmentUnlocks;

	// Probably need a new recipe class (CraftingSmelt)
	// to take fuel into account
	public CraftingRecipe[] kilnSmelts;
	public CraftingRecipe[] forgeSmelts;

    public CraftingGlobals () {
        initializeRecipes();
    }

    public CraftingRecipe[] getUnlockedRecieps (CraftingLocation location) {
        if (location == CraftingLocation.PLAYER) 
            return starterCrafts;

        else if (location == CraftingLocation.WORKBENCH) {
            ArrayList<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

            for (CraftingRecipe recipe : starterCrafts)
                recipes.add(recipe);

            // By being at a workbench, it's guaranteed that tech level is at least WORKBENCH
            for (CraftingRecipe recipe : workbenchCrafts)
                recipes.add(recipe);

            int currentTechOrdinal = GameState.techLevel.ordinal();
            if (currentTechOrdinal >= TechLevel.KILN.ordinal()) {
                for (CraftingRecipe recipe : kilnCrafts)
                    recipes.add(recipe);
            }

            if (currentTechOrdinal >= TechLevel.FORGE.ordinal()) {
                for (CraftingRecipe recipe : forgeCrafts)
                    recipes.add(recipe);
            }

            for (int i=0; i<equipmentCrafts.length; i++) {
                int requiredLevel = equipmentUnlocks[i].ordinal();
                boolean alreadyCrafted = GameState.player.getInventory().getQuantity(equipmentCrafts[i].result()) > 0;
                if (currentTechOrdinal >= requiredLevel &&
                    alreadyCrafted == false)
                        recipes.add(equipmentCrafts[i]);
            }

            // Conversion
            CraftingRecipe[] returnRecipes = new CraftingRecipe[recipes.size()];
            for (int i=0; i<returnRecipes.length; i++) {
                returnRecipes[i] = recipes.get(i);
            }

            return returnRecipes;
        
        
        } else if (location == CraftingLocation.KILN) {
            return kilnSmelts;
        
        
        } else if (location == CraftingLocation.FORGE) {
            return forgeSmelts;
        }

        throw new Error("Kiln & Forge recipes not yet implemented");
    }

	private void initializeRecipes () {
        starterCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {5,             3},
                ItemIndex.WORKBENCH,
                "Unlocks more crafting options"),

		};



		workbenchCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             1},
                ItemIndex.CHEST,
                "Allows for storage of items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD}, 
                new int[]       {2},
                ItemIndex.ITEM_TUBE_STONE,
                "Transports items between tubes & inserts into inventories"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD}, 
                new int[]       {2},
                ItemIndex.ITEM_TUBE_GLASS,
                "Pulls items out of inventories"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD}, 
                new int[]       {4},
                ItemIndex.WOODEN_FLOAT,
                "The player can walk over water without a boat"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {2,             5},
                ItemIndex.KILN,
                "Process copper ore & sand to produce resources"),
		};

        //
        // These are made at the workbench, after having unlocked the kiln & forge
		kilnCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.COPPER_WORKBENCH,
                "Will automatically craft when a recipe is selected and items are pumped in"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.MINING_DRILL,
                "Will drill up the resource it is placed on infinitely"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.FORGE,
                "Can smelt iron and produce alloys"),
        };
        
        forgeCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {3,              2},
                ItemIndex.BRONZE_FLOAT,
                "Can support the weight of an item tube"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.AUTO_MINING_UPGRADE,
                "Placed onto a mining drill, will automatically collect resources"),

        };

        

        //
        // Equipment is weird because it's a 1-time craft
        equipmentCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             2},
                ItemIndex.PICKAXE,
                "Allows for harvesting coal and ores"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {3,              2},
                ItemIndex.BOAT,
                "Move across water to new islands"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.SHOVEL,
                "Allows for sand collection"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.TALL_BOOTS,
                "Improved mobility over factory components"),
        };

        // Stores at what TechLevel each equipment is unlocked
        equipmentUnlocks = new TechLevel[] {
            TechLevel.WORKBENCH, // Pickaxe
            TechLevel.KILN, // Boat
            TechLevel.KILN, // Shovel
            TechLevel.FORGE, // Tall Boots
        };

        //
        // These are made at the Kiln & Forge
        kilnSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_COPPER, ItemIndex.ORE_COAL}, 
                new int[]       {2,             1},
                ItemIndex.BAR_COPPER,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.SAND, ItemIndex.ORE_COAL}, 
                new int[]       {3,             1},
                ItemIndex.GLASS,
                "Can be used to craft more items"),
        };

        forgeSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {2,             1},
                ItemIndex.BAR_IRON,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_COPPER, ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {1,                   2,                  1},
                ItemIndex.ALLOY_BRONZE,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {3,                  2},
                ItemIndex.ALLOY_STEEL,
                "Can be used to craft more items"),
        };
	}

}
