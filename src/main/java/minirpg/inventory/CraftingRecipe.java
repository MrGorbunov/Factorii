package minirpg.inventory;

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

}