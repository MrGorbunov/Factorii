package factorii.inventory;

import java.security.InvalidParameterException;

public class CraftingRecipe {

    // Parallel arrays
    private ItemIndex[] items;
    private int[] itemQuantities;
    private ItemIndex result;
    private String description;

    public CraftingRecipe (ItemIndex[] items, int[] itemQuantities, 
        ItemIndex result, String description) {
            this.items = items;
            this.itemQuantities = itemQuantities;

            this.result = result;
            this.description = description;
    }

    public ItemIndex result () { return result; } 
    public String resultName () { return result.toString(); }
    public String description () { return description; } 

    public ItemIndex[] inputItems () { return items; }
    public int[] inputAmounts () { return itemQuantities; }

}