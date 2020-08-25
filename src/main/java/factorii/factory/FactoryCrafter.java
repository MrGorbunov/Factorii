package factorii.factory;

import factorii.GameState;
import factorii.inventory.CraftingRecipe;
import factorii.world.Tile;

public class FactoryCrafter implements FacData, FacCrafter {
    
    private Tile tile;

    public Tile getTile () { return tile; }

    public FactoryCrafter (Tile tile) {
        this.tile = tile;
    } 

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

}