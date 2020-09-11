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
            case ASSEMBLY_TABLE:
                this.tile = Tile.ASSEMBLY_TABLE;
                selectedRecipe = GameState.craftingGlobals.getAssemblyCrafts(false)[0];
                return;
            
            case MANUAL_KILN:
            case KILN:
                this.tile = Tile.KILN;
                selectedRecipe = GameState.craftingGlobals.getKilnCrafts()[0];
                return;
            
            case FORGE:
                this.tile = Tile.FORGE;
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
            case ASSEMBLY_TABLE:
                return GameState.craftingGlobals.getAssemblyCrafts(false);
            
            case KILN:
                return GameState.craftingGlobals.getKilnCrafts();

            case FORGE:
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