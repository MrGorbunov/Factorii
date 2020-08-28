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
                selectedRecipe = GameState.craftingGlobals.getWorkbenchCrafts(false)[0];
                return;
            
            case KILN:
            case IRON_KILN:
                this.tile = Tile.IRON_KILN;
                selectedRecipe = GameState.craftingGlobals.getKilnCrafts()[0];
                return;
            
            case FORGE:
            case STEEL_FORGE:
                this.tile = Tile.STEEL_FORGE;
                selectedRecipe = GameState.craftingGlobals.getForgeCrafts()[0];
                return;

        }

        throw new Error("Attmpted to create an auto-crafter from invalid workstation (workstation being Kiln, Forge, Workbench, etc)");
    }

    public Tile getTile () { return tile; }
    public Inventory getInventory () { return inventory; }
    public CraftingRecipe getSelectedRecipe () { return selectedRecipe; }

    public CraftingRecipe[] getRecipes () {
        switch (tile) {
            case COPPER_WORKBENCH:
                return GameState.craftingGlobals.getWorkbenchCrafts(false);
            
            case IRON_KILN:
                return GameState.craftingGlobals.getKilnCrafts();

            case STEEL_FORGE:
                return GameState.craftingGlobals.getForgeCrafts();
        }

        throw new Error ("Auto Crafter not created from workstation. Possible that non-auto version of tile was used (Workbench instead of AutoWorkbench)");
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