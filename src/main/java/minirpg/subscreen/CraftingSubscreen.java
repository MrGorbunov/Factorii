package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.GameState;
import minirpg.inventory.CraftingLocation;
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

// TODO: [Bug] When changing tech level, new crafts do not become visible

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

    private CraftingRecipe[] craftingRecipes;

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

    private void initRecipeLists () {
        CraftingLocation location = GameState.world.getCraftingLocation();
        craftingRecipes = GameState.craftingGlobals.getUnlockedRecieps(location);
    }



    //
    // Small things
    //

    public void setActive (boolean active) {
        this.active = active;
    }

    public void drawSubscreen (AsciiPanel terminal) {
        updateAllLists();
        drawRecipesList(terminal);
        drawDescription(terminal);
    }

 


    //
    // List Updating
    //

    private void updateAllLists () {
        Inventory inv = GameState.player.getInventory();
        availability = new ArrayList<Boolean> ();
        recipes = new ArrayList<CraftingRecipe>();

        for (CraftingRecipe recipe : craftingRecipes) {
            recipes.add(recipe);
            availability.add(inv.canCraft(recipe));
        }
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
		GameState.player.getInventory().craftItem(selectedRecipe);
	}





    //
    // Descriptions display
    //

    private void drawDescription (AsciiPanel terminal) {
        int yCord = descriptionY;

        CraftingRecipe recipe = recipes.get(selection);
        terminal.write(recipe.resultName(), xOff, yCord, Color.CYAN);
        yCord++;

        String[] brokenUpDesc = breakupDescription(recipe.description());
        for (String s : brokenUpDesc) {
            terminal.write(s, xOff, yCord);
            yCord++;
        }

        for (int i=0; i<recipe.inputItems().length; i++) {
            ItemIndex ingredient = recipe.inputItems()[i];
            int amount = recipe.inputAmounts()[i];

            boolean enoughOfIngredient = GameState.player.getInventory().getQuantity(ingredient) >= amount;
            Color textColor = enoughOfIngredient ? Color.WHITE : Color.LIGHT_GRAY;

            terminal.write(ingredient + " x" + amount, xOff, yCord, textColor);
            yCord++;
        }
    }

    /**
     * Breaks up the description into multiple lines
     * so it fits into the space
     */
    public String[] breakupDescription (String descString) {
        int maxLineLength = width - 2*pad;
        // This is the same as a ceiling operation, but more efficient
        // i.e. equivalent to ```(int) Math.ceil((double) descString.length() / maxLineLength)
        int numLines = (descString.length() + maxLineLength - 1) / maxLineLength;
        String[] brokenUp = new String[numLines];

        for (int i=0; i<numLines; i++) {
            if (i + 1 >= numLines) {
                brokenUp[i] = descString.substring(i*maxLineLength);

            } else {
                brokenUp[i] = descString.substring(i*maxLineLength, (i+1)*maxLineLength);
            }
        }

        return brokenUp;
    }

}
