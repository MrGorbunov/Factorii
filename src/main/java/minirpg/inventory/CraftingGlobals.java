package minirpg.inventory;

import java.util.ArrayList;

import minirpg.GameState;
import minirpg.TechLevel;
import minirpg.inventory.*;

public class CraftingGlobals {

	// What you can make a workbench given tech level
	public CraftingRecipe[] starterCrafts;
	public CraftingRecipe[] workbenchCrafts;
	public CraftingRecipe[] kilnCrafts;
	public CraftingRecipe[] forgeCrafts;

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

            if (GameState.techLevel.ordinal() >= TechLevel.KILN.ordinal()) {
                for (CraftingRecipe recipe : kilnCrafts)
                    recipes.add(recipe);
            }

            if (GameState.techLevel.ordinal() >= TechLevel.FORGE.ordinal()) {
                for (CraftingRecipe recipe : forgeCrafts)
                    recipes.add(recipe);
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
                ItemIndex.WORKBENCH, "Workbench",
                "Unlocks more crafting options"),

		};



		workbenchCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             2},
                ItemIndex.PICKAXE, "Pickaxe",
                "Allows for harvesting coal and ores"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             1},
                ItemIndex.CHEST, "Chest",
                "Allows for storage of items & funnel interfacing"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD}, 
                new int[]       {2},
                ItemIndex.FUNNEL_WOOD, "Wooden Funnel",
                "Transports items between inventories"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD}, 
                new int[]       {4},
                ItemIndex.WOODEN_FLOAT, "Wooden Float",
                "The player can walk over water without a boat"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {2,             5},
                ItemIndex.KILN, "Kiln",
                "Process copper ore & sand to produce resources"),
		};

        //
        // These are made at the workbench, after having unlocked the kiln & forge
		kilnCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {3,              2},
                ItemIndex.BOAT, "Boat",
                "Move across water to new islands"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.SHOVEL, "Shovel",
                "Allows for sand collection"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.COPPER_WORKBENCH, "Copper Workbench",
                "Will automatically craft when a recipe is selected and items are pumped in"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.MINING_DRILL, "Mining Drill",
                "Will drill up the resource it is placed on infinitely"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.FORGE, "Forge",
                "Can smelt iron and produce alloys"),
        };
        
        forgeCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {3,              2},
                ItemIndex.BRONZE_FLOAT, "Bronze Float",
                "Can support the weight of a funnel"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.AUTO_MINING_UPGRADE, "Auto Mining Upgrade",
                "Placed onto a mining drill, will automatically collect resources"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.FUNNEL_IRON, "Iron Funnel",
                "Perhaps unnecessary"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.FUNNEL_STEEL, "Steel Funnel",
                "Perhaps unnecessary"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.BAR_COPPER}, 
                new int[]       {2,              1},
                ItemIndex.TALL_BOOTS, "Tall Boots",
                "Improved mobility over factory components"),

        };



        //
        // These are made at the Kiln & Forge
        kilnSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_COPPER, ItemIndex.ORE_COAL}, 
                new int[]       {2,             1},
                ItemIndex.BAR_COPPER, "Copper Bar",
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.SAND, ItemIndex.ORE_COAL}, 
                new int[]       {3,             1},
                ItemIndex.GLASS, "Glass",
                "Can be used to craft more items"),
        };

        forgeSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {2,             1},
                ItemIndex.BAR_IRON, "Iron Bar",
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_COPPER, ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {1,                   2,                  1},
                ItemIndex.ALLOY_BRONZE, "Bronze Alloy",
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {3,                  2},
                ItemIndex.ALLOY_STEEL, "Steel Alloy",
                "Can be used to craft more items"),
        };
	}

}
