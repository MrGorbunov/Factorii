package factorii.factory;
import factorii.GameState;
import factorii.inventory.CraftingRecipe;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.world.Tile;

public class FactoryAutoCrafter implements FacData, FacInventory, FacProducer, FacCrafter {
    
    private final Tile tile;

    private Inventory inventory;
    private CraftingRecipe selectedRecipe;

    public FactoryAutoCrafter (Tile tile) {
        inventory = new Inventory(); 

        switch (tile) {
            case WORKBENCH:
            case COPPER_WORKBENCH:
                this.tile = Tile.COPPER_WORKBENCH;
                selectedRecipe = GameState.craftingGlobals.getWorkbenchCrafts()[0];
                return;
        }

        this.tile = Tile.COPPER_WORKBENCH;
        selectedRecipe = GameState.craftingGlobals.getWorkbenchCrafts()[0];
    }

    public Tile getTile () { return tile; }
    public Inventory getInventory () { return inventory; }
    public CraftingRecipe getSelectedRecipe () { return selectedRecipe; }

    public CraftingRecipe[] getRecipes () {
        switch (tile) {
            case WORKBENCH:
            case COPPER_WORKBENCH:
                return GameState.craftingGlobals.getWorkbenchCrafts();
            
            case KILN:
                return GameState.craftingGlobals.getKilnCrafts();

            case FORGE:
                return GameState.craftingGlobals.getForgeCrafts();
        }

        return GameState.craftingGlobals.getPlayerCrafts();
    }

    public void setRecipe (CraftingRecipe newRecipe) {
        selectedRecipe = newRecipe;
    }




    //
    // Interface implementation
    //

    public void tickUpdate () {
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