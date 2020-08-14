package minirpg.inventory;

import minirpg.GameState;
import minirpg.TechLevel;

public class SizedInventory extends Inventory {

    private int[] itemAmounts;
    private int maxSize;

    public SizedInventory (int maxSize) {
        itemAmounts = new int[ItemIndex.values().length];
        this.maxSize = maxSize;
    }





    //
    // Basic Inventory operations
    //

    public void addItem (ItemIndex item) {
        itemAmounts[item.ordinal()]++;
    }

    public void addItemMulti (ItemIndex item, int amount) {
        itemAmounts[item.ordinal()] += amount;
    }

    public int getQuantity (ItemIndex item) {
        return itemAmounts[item.ordinal()];
    }

    public int getTotalSize () {
        int total = 0;

        for (int stack : itemAmounts) {
            total += stack;
        }

        return total;
    }

    /**
     * Attempts to remove an item. If not possible, debugs a warning
     */
    public void removeItem (ItemIndex item) {
        if (itemAmounts[item.ordinal()] == 0) {
            System.out.println("ERROR: Attempted to remove item which has 0 quantity");
            return;
        }

        itemAmounts[item.ordinal()]--;
    }





    //
    // Crafting
    //

    /**
     * If it cannot craft, nothing happens
     * make sure to check with canCraft/ canCraftMultiple
     */
    public void craftItem (CraftingRecipe recipe) {
        if (canCraft(recipe) == false) return;

        ItemIndex[] ingredients = recipe.inputItems();
        int[] quantities = recipe.inputAmounts();

        for (int i=0; i<ingredients.length; i++) {
            itemAmounts[ingredients[i].ordinal()] -= quantities[i];
        }

        itemAmounts[recipe.result().ordinal()]++;
        updateTechLevel(recipe.result());
    }

    /**
     * If it cannot craft, nothing happens
     * make sure to check with canCraft/ canCraftMultiple
     */
    public void craftItemMultiple (CraftingRecipe recipe, int amount) {
        if (canCraftMultiple(recipe, amount) == false) return;

        ItemIndex[] ingredients = recipe.inputItems();
        int[] quantities = recipe.inputAmounts();

        for (int i=0; i<ingredients.length; i++) {
            itemAmounts[ingredients[i].ordinal()] -= quantities[i] * amount;
        }

        itemAmounts[recipe.result().ordinal()] += amount;
        updateTechLevel(recipe.result());
    }

    public boolean canCraft (CraftingRecipe recipe) {
        ItemIndex[] ingredients = recipe.inputItems();
        int[] quantities = recipe.inputAmounts();

        for (int i=0; i<ingredients.length; i++) {
            if (getQuantity(ingredients[i]) < quantities[i])
                return false;
        }
        return true;
    }

    public boolean canCraftMultiple (CraftingRecipe recipe, int amount) {
        ItemIndex[] ingredients = recipe.inputItems();
        int[] quantities = recipe.inputAmounts();

        for (int i=0; i<ingredients.length; i++) {
            if (getQuantity(ingredients[i]) < quantities[i] * amount)
                return false;
        }
        return true;
    }


    private void updateTechLevel (ItemIndex craftedItem) {
        TechLevel techLevel = GameState.techLevel;

        if (craftedItem == ItemIndex.WORKBENCH &&
            techLevel.ordinal() <= TechLevel.START.ordinal()) {
                GameState.techLevel = TechLevel.WORKBENCH;

        } else if (craftedItem == ItemIndex.KILN &&
            techLevel.ordinal() <= TechLevel.WORKBENCH.ordinal()) {
                GameState.techLevel = TechLevel.KILN;

        } else if (craftedItem == ItemIndex.FORGE &&
            techLevel.ordinal() <= TechLevel.KILN.ordinal()) {
                GameState.techLevel = TechLevel.FORGE;
        } 
    }
}