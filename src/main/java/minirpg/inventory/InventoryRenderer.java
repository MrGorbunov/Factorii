package minirpg.inventory;

import java.util.ArrayList;

import minirpg.GameState;
import minirpg.TechLevel;


public class InventoryRenderer {

    CraftingRecipe[] starterRecipes;

    public InventoryRenderer () {
        initializeRecipes();
    }

    private void initializeRecipes () {
        // TODO: Read out of a json or something
        starterRecipes = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {5,             3},
                ItemIndex.WORKBENCH, "Workbench",
                "Unlocks more crafting options"),
            
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             2},
                ItemIndex.PICKAXE, "Pickaxe",
                "Allows for harvesting coal and ores"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             1},
                ItemIndex.SHOVEL, "Shovel",
                "Allows for sand collection"),


        };
    }

    // TODO: This feels pretty sloppy
    public CraftingRecipe getRecipe (String name) {
        for (CraftingRecipe cr : starterRecipes) {
            if (cr.resultName().equals(name))
                return cr;
        }

        return null;
    }



    //
    // Inventory Management Calls
    //

    public ArrayList<String> getResourceList () {
        Inventory inv = GameState.inventory;
        ArrayList<String> resources = new ArrayList<String>();

        resources.add("Wood: " + inv.getQuantity(ItemIndex.WOOD));
        resources.add("Stone: " + inv.getQuantity(ItemIndex.STONE));
        resources.add("");
        
        if (GameState.techLevel.ordinal() >= TechLevel.WORKBENCH.ordinal()) {
            resources.add("Coal: " + inv.getQuantity(ItemIndex.ORE_COAL));
            resources.add("Copper Ore: " + inv.getQuantity(ItemIndex.ORE_COPPER));
            resources.add("Iron Ore: " + inv.getQuantity(ItemIndex.ORE_IRON));
        }

        if (GameState.techLevel.ordinal() >= TechLevel.COPPER.ordinal()) {
            resources.add(4, "Sand: " + inv.getQuantity(ItemIndex.SAND));
            resources.add("");

            resources.add("Glass: " + inv.getQuantity(ItemIndex.GLASS));
            resources.add("Copper Bars: " + inv.getQuantity(ItemIndex.BAR_COPPER));
        }

        if (GameState.techLevel.ordinal() >= TechLevel.IRON.ordinal()) {
            resources.add("Iron Bars: " + inv.getQuantity(ItemIndex.BAR_IRON));
            resources.add("Bronze: " + inv.getQuantity(ItemIndex.ALLOY_BRONZE));
            resources.add("Steel: " + inv.getQuantity(ItemIndex.ALLOY_STEEL));
        }
        
        return resources;
    }

    public ArrayList<ItemIndex> getCraftedItems () {
        ArrayList<ItemIndex> items = new ArrayList<ItemIndex> ();
        Inventory inv = GameState.inventory;

        for (ItemIndex in : ItemIndex.values()) {
            if (!in.isPlacable() ||
                inv.getQuantity(in) == 0)
                    continue;
            
            items.add(in);
        }

        return items;
    }

    public ArrayList<String> recipesUnlocked () {
        ArrayList<String> resources = new ArrayList<String> ();

        for (CraftingRecipe recipe : starterRecipes) {
            resources.add(recipe.resultName());
        }

        return resources;
    }

    public ArrayList<Boolean> recipesCraftable () {
        Inventory inv = GameState.inventory;
        ArrayList<Boolean> availabality = new ArrayList<Boolean> ();

        for (CraftingRecipe recipe : starterRecipes) {
            availabality.add(inv.canCraft(recipe));
        }

        return availabality;
    }
}