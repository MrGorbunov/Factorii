package minirpg.factory;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.Inventory;
import minirpg.inventory.ItemIndex;
import minirpg.world.Tile;

public class FactoryAutoCrafter implements FacData, FacInventory, FacProducer {
    
    private final Tile tile;

    private Inventory inventory;
    private CraftingRecipe selectedRecipe;

    public FactoryAutoCrafter (CraftingRecipe recipe) {
        inventory = new Inventory(); 
        tile = Tile.COPPER_WORKBENCH;
        setRecipe(recipe);
    }

    public Tile getTile () { return tile; }
    public Inventory getInventory () { return inventory; }
    public CraftingRecipe getSelectedRecipe () { return selectedRecipe; }

    public void setRecipe (CraftingRecipe newRecipe) {
        selectedRecipe = newRecipe;
    }




    //
    // Interface implementation
    //

    public void update () {
        if (inventory.canCraft(selectedRecipe))
            inventory.craftItem(selectedRecipe);
    }

    public boolean canTakeProduct () {
        return inventory.getQuantity(selectedRecipe.result()) > 0;
    }

    /**
     * Returns null if no product exists. Otherwise will
     * return product in inventory.
     */
    public ItemIndex takeProduct () {
        if (!canTakeProduct())
            throw new Error ("Did not check whether can take product");

        inventory.removeItem(selectedRecipe.result());
        return selectedRecipe.result();
    }

}