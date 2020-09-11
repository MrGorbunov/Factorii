package factorii.inventory;

import java.security.InvalidParameterException;

public class CraftingRecipe {

    // Parallel arrays
    private ItemIndex[] items;
    private int[] itemQuantities;
    private ItemIndex result;
    private String description;
    private boolean manuallyCraftable;

    public CraftingRecipe (ItemIndex[] items, int[] itemQuantities, 
        boolean manuallyCraftable, ItemIndex result, String description) {
            this.items = items;
            this.itemQuantities = itemQuantities;
            this.manuallyCraftable = manuallyCraftable;

            this.result = result;
            this.description = description;
    }

    public ItemIndex result () { return result; } 
    public String resultName () { return result.toString(); }
    public String description () { return description; } 

    public ItemIndex[] inputItems () { return items; }
    public int[] inputAmounts () { return itemQuantities; }
    public boolean isManuallyCraftable () { return manuallyCraftable; }

}