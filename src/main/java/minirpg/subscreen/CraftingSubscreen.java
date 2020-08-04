package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.GameState;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.Inventory;
import minirpg.inventory.ItemIndex;

/*
This is a 2 parter

On the top, it shows the crafting options and allow for selection but 
  no scrolling (Scrolling will probably be needed but not rn)
  Also, if scrolling is ever needed then tabs would probably be ideal. (left <-> right for categories)
On the bottom, it shows a preview of the current craft option

*/

public class CraftingSubscreen {

    private int width;
    private int height;
    private int xOff;
    private int yOff;
    private boolean active;

    private int pad;
    private int descriptionY;
    private int selection;
    private ArrayList<CraftingRecipe> recipes;
    private ArrayList<Boolean> availability;

    private CraftingRecipe[] starterRecipes;

    public CraftingSubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;
        this.xOff = xOff;
        this.yOff = yOff;
        pad = 2;
        selection = 0;

        // TODO: This assumes that recipes have at most 3 unique ingredients
        descriptionY = yOff + height - pad - 5;

        initRecipeLists();
        updateAllLists();
    }

    public CraftingSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }

    public void initRecipeLists () {
        // TODO: Read out of a json or something
        starterRecipes = new CraftingRecipe[] {
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {5,             3},
                ItemIndex.WORKBENCH, "Workbench",
                "Unlocks more crafting options"),
            
            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             2},
                ItemIndex.PICKAXE, "Pickaxe",
                "Allows for harvesting coal and ores"),

            new CraftingRecipe(
                new ItemIndex[] {ItemIndex.WOOD, ItemIndex.STONE}, 
                new int[]       {3,             1},
                ItemIndex.SHOVEL, "Shovel",
                "Allows for sand collection"),


        };
    }





    //
    // Small things
    //

    public void setActive (boolean active) {
        this.active = active;
    }

    // TODO: Right now, this is set to only show the started crafts
    public void drawSubscreen (AsciiPanel terminal) {
        drawRecipesList(terminal);
        drawDescription(terminal);
    }

 


    //
    // List Updating
    //

    private void updateAllLists () {
        recipes = recipesUnlocked();
        availability = recipesCraftable();
    }

    private ArrayList<CraftingRecipe> recipesUnlocked () {
        ArrayList<CraftingRecipe> recipes = new ArrayList<CraftingRecipe> ();

        for (CraftingRecipe recipe : starterRecipes) {
            recipes.add(recipe);
        }

        return recipes;
    }

    private ArrayList<Boolean> recipesCraftable () {
        Inventory inv = GameState.inventory;
        ArrayList<Boolean> availabality = new ArrayList<Boolean> ();

        for (CraftingRecipe recipe : starterRecipes) {
            availabality.add(inv.canCraft(recipe));
        }

        return availabality;
    }





    //
    // Recipe Options Display
    //

    private void drawRecipesList (AsciiPanel terminal) {
        for (int i=0; i<recipes.size(); i++) {
            CraftingRecipe recipe = recipes.get(i);
            Color textColor = availability.get(i) ? Color.WHITE : Color.LIGHT_GRAY;
            Color bgColor = selection == i && active ? Color.GRAY : Color.BLACK;

            terminal.write(recipe.resultName(), xOff+pad, yOff+pad+i, textColor, bgColor);
        }
    }

    public void selectionUp () {
        selection--;

        if (selection < 0) {
            selection = recipes.size() - 1;
        }
    }

    public void selectionDown () {
        selection++;

        if (selection == recipes.size()) {
            selection = 0;
        }
    }

	/**
	 * Crafts the current selection. If not possible,
	 * (insufficient materials) nothing happens.
	 */
	public void craftSelection () {
		CraftingRecipe selectedRecipe = recipes.get(selection);
		// craftItem auto-checks for proper # ingredients
		GameState.inventory.craftItem(selectedRecipe);
	}





    //
    // Descriptions display
    //

    public void drawDescription (AsciiPanel terminal) {
        CraftingRecipe recipe = recipes.get(selection);
        terminal.write(recipe.resultName(), xOff, descriptionY, Color.CYAN);
        terminal.write(recipe.description(), xOff, descriptionY+1);

        for (int i=0; i<recipe.inputItems().length; i++) {
            ItemIndex ingredient = recipe.inputItems()[i];
            int amount = recipe.inputAmounts()[i];

            boolean enoughOfIngredient = GameState.inventory.getQuantity(ingredient) >= amount;
            Color textColor = enoughOfIngredient ? Color.WHITE : Color.LIGHT_GRAY;

            terminal.write(ingredient + " x" + amount, xOff, descriptionY+2+i, textColor);
        }
    }

}
