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

}