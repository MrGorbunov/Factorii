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
        }

        System.out.println("Kiln & Forge recipes not yet implemented");
        return null;
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



		kilnCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             1},
                ItemIndex.SHOVEL, "Shovel",
                "Allows for sand collection"),
        };
        
        forgeCrafts = new CraftingRecipe[] {

        };
	}

}
