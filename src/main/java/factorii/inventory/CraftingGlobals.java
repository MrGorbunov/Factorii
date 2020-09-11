package factorii.inventory;

import java.util.ArrayList;

import factorii.GameState;
import factorii.TechLevel;
import factorii.inventory.*;

public class CraftingGlobals {

	// What you can make a workbench given tech level
	private CraftingRecipe[] starterCrafts;
	private CraftingRecipe[] kilnCrafts;
    private CraftingRecipe[] forgeCrafts;
    
    // Equipment is weird because they're unlocked but also 1-time crafts
    private CraftingRecipe[] equipmentCrafts;
    private TechLevel[] equipmentUnlocks;

	// Probably need a new recipe class (CraftingSmelt)
	// to take fuel into account
	private CraftingRecipe[] kilnSmelts;
	private CraftingRecipe[] forgeSmelts;

    public CraftingGlobals () {
        initializeRecipes();
    }

    // Equipment should only be craftable at a workbench, NOT an autoworkbench
    // hence the parameter
    public CraftingRecipe[] getAssemblyCrafts (boolean includeEquipment) {
        ArrayList<CraftingRecipe> playerCrafts = new ArrayList<CraftingRecipe> ();
        ArrayList<CraftingRecipe> autoCrafts = new ArrayList<CraftingRecipe> ();

        for (CraftingRecipe recipe : getAllUnlockedRecipes(false)) {
            if (recipe.isManuallyCraftable() == true) {
                playerCrafts.add(recipe);
            } else {
                autoCrafts.add(recipe);
            }
        }

        CraftingRecipe[] recipes = new CraftingRecipe[autoCrafts.size() + playerCrafts.size()];
        int autoCraftSize = autoCrafts.size();

        for (int i=0; i<autoCraftSize; i++) {
            recipes[i] = autoCrafts.get(i);
        }
        for (int i=0; i<playerCrafts.size(); i++) {
            recipes[i+autoCraftSize] = playerCrafts.get(i);
        }

        return recipes;
    }

    public CraftingRecipe[] getPlayerCrafts () {
        ArrayList<CraftingRecipe> playerCrafts = new ArrayList<CraftingRecipe> ();

        for (CraftingRecipe recipe : getAllUnlockedRecipes(true)) {
            if (recipe.isManuallyCraftable()) {
                playerCrafts.add(recipe);
            }
        }

        CraftingRecipe[] recipes = new CraftingRecipe[playerCrafts.size()];
        for (int i=0; i<playerCrafts.size(); i++) {
            recipes[i] = playerCrafts.get(i);
        }

        return recipes;
    }

    public CraftingRecipe[] getKilnCrafts () {
        return kilnSmelts;
    }

    public CraftingRecipe[] getForgeCrafts () {
        return forgeSmelts;
    }

    /**
     * Returns the unlocked recipes, based on skill level
     */
    public ArrayList<CraftingRecipe> getAllUnlockedRecipes (boolean includeEquipment) {
        ArrayList<CraftingRecipe> recipes = new ArrayList<CraftingRecipe>();

        for (CraftingRecipe recipe : starterCrafts)
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

        if (includeEquipment) {
            for (int i=0; i<equipmentCrafts.length; i++) {
                int requiredLevel = equipmentUnlocks[i].ordinal();
                boolean alreadyCrafted = GameState.player.getInventory().getQuantity(equipmentCrafts[i].result()) > 0;
                if (currentTechOrdinal >= requiredLevel &&
                    alreadyCrafted == false)
                        recipes.add(equipmentCrafts[i]);
            }
        }

        return recipes;
    }




    //
    // Recipe decleration
    //

	private void initializeRecipes () {
		starterCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {2,              5},
                true,
                ItemIndex.CHEST,
                "Can store items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {1,              2},
                true,
                ItemIndex.LANDFILL,
                "Fills water in with dirt that can be walked & built on top of"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {2,              3},
                true,
                ItemIndex.MANUAL_KILN,
                "Process copper & sand to produce new resources"),
        };
        
        // TODO: Crafts are too hard, there are 2 hard crafts: the forge & the sub
        // It might be good to remove mining drills from start

        //
        // These next two arrays are made at the workbench, but only after 
        // having unlocked the kiln or forge
		kilnCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.STONE, ItemIndex.BAR_COPPER}, 
                new int[]       {5,               5},
                true,
                ItemIndex.DEEP_DRILL,
                "Place down to automatically collect resources"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.STONE}, 
                new int[]       {3},
                true,
                ItemIndex.ITEM_TUBE_STONE,
                "Transports items between tubes & inserts into inventories"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.GLASS}, 
                new int[]       {3},
                true,
                ItemIndex.ITEM_TUBE_GLASS,
                "Pulls items out of inventories"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.GLASS, ItemIndex.BAR_COPPER}, 
                new int[]       {3,               10},
                true,
                ItemIndex.ASSEMBLY_TABLE,
                "Will automatically craft when a recipe is selected and items are pumped in"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.STONE, ItemIndex.BAR_COPPER, ItemIndex.GLASS}, 
                new int[]       {15,              15,                   15},
                false,
                ItemIndex.FORGE,
                "Smelt iron and produce alloys"),
        };
        
        forgeCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.MANUAL_KILN, ItemIndex.BAR_IRON}, 
                new int[]       {1,              10},
                false,
                ItemIndex.KILN,
                "Can smelt like a kiln, but automatically"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ALLOY_STEEL}, 
                new int[]       {5},
                true,
                ItemIndex.ITEM_TUBE_STEEL,
                "Used to filter out-going items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.STONE, ItemIndex.GLASS, ItemIndex.ALLOY_STEEL}, 
                new int[]       {20,              30,              40},
                false,
                ItemIndex.SUBMARINE,
                "The ultimate creation"),
        };

        

        //
        // Equipment is weird because it's a 1-time craft
        equipmentCrafts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.ALLOY_BRONZE}, 
                new int[]       {20,             25},
                true,
                ItemIndex.BOAT,
                "Move across water without landfills"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ALLOY_BRONZE}, 
                new int[]       {10,                 5},
                true,
                ItemIndex.TALL_BOOTS,
                "Improved mobility over factory components"),
        };

        // Stores at what TechLevel each equipment is unlocked
        equipmentUnlocks = new TechLevel[] {
            TechLevel.FORGE,  // Boat
            TechLevel.FORGE,  // Tall Boots
        };



        //
        // These are made at the Kiln & Forge
        kilnSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_COPPER, ItemIndex.ORE_COAL}, 
                new int[]       {1,             1},
                true,
                ItemIndex.BAR_COPPER,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.SAND, ItemIndex.ORE_COAL}, 
                new int[]       {1,             1},
                true,
                ItemIndex.GLASS,
                "Can be used to craft more items"),
        };

        forgeSmelts = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.ORE_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {1,             1},
                true,
                ItemIndex.BAR_IRON,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_COPPER, ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {1,                    2,                  1},
                true,
                ItemIndex.ALLOY_BRONZE,
                "Can be used to craft more items"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.BAR_IRON, ItemIndex.ORE_COAL}, 
                new int[]       {3,                  1},
                true,
                ItemIndex.ALLOY_STEEL,
                "Can be used to craft more items"),
        };
	}

}
