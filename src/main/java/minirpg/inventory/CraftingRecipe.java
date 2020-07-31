package minirpg.inventory;

import java.security.InvalidParameterException;

public class CraftingRecipe {

    // Parallel arrays
    private ItemIndex[] items;
    private int[] itemQuantities;
    private ItemIndex result;
    private String resultName;
    private String description;

    public CraftingRecipe (ItemIndex[] items, int[] itemQuantities, 
        ItemIndex result, String resultName, String description) {
            this.items = items;
            this.itemQuantities = itemQuantities;

            this.result = result;
            this.resultName = resultName;
            this.description = description;
    }

    public ItemIndex result () { return result; } 
    public String resultName () { return resultName; }
    public String description () { return description; } 

    public ItemIndex[] inputItems () { return items; }
    public int[] inputAmounts () { return itemQuantities; }

    public boolean canCraft (Inventory inv) {
        for (int i=0; i<items.length; i++) {
            if (inv.getQuantity(items[i]) < itemQuantities[i])
                return false;
        }

        return true;
    }

    public boolean canCraftMultiple (Inventory inv, int amount) {
        for (int i=0; i<items.length; i++) {
            if (inv.getQuantity(items[i]) * amount < itemQuantities[i])
                return false;
        }

        return true;
    }

    public void craft (Inventory inv) {
        if (!canCraft(inv))
            throw new InvalidParameterException("Given an inventory with insufficient resources");

        for (int i=0; i<items.length; i++) {
            inv.removeItemMulti(items[i], itemQuantities[i]);
        }

        inv.addItem(result);
    }

    public void craftMultiple (Inventory inv, int amount) {
        if (!canCraftMultiple(inv, amount))
            throw new InvalidParameterException("Given an inventory with insufficient resources");

        for (int i=0; i<items.length; i++) {
            inv.removeItemMulti(items[i], itemQuantities[i] * amount);
        }

        inv.addItemMulti(result, amount);
    }

}