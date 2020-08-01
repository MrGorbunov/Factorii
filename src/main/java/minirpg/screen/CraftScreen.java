package minirpg.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import minirpg.GameState;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.InventoryRenderer;
import minirpg.inventory.ItemIndex;

public class CraftScreen implements Screen {
    
    private InventoryRenderer invRenderer;
    // These guys don't need to be updated every frame, so
    // are kept persistently
    private ArrayList<String> resources;
    private ArrayList<ItemIndex> craftedItems;
    private ArrayList<String> recipeResults;
    private ArrayList<Boolean> recipeAvailability;

    private int currentSelection;

    public CraftScreen () {
        invRenderer = new InventoryRenderer();
        updateAllLists();
        currentSelection = 0;
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new WorldScreen();
            
            case KeyEvent.VK_UP:
                selectUp();
                break;

            case KeyEvent.VK_DOWN:
                selectDown();
                break;
            
            case KeyEvent.VK_X:
                craftSelection();
                break;

        }

        return this;
    }

    private void updateAllLists () {
        resources = invRenderer.getResourceList();
        craftedItems = invRenderer.getCraftedItems();
        recipeResults = invRenderer.recipesUnlocked();
        recipeAvailability = invRenderer.recipesCraftable();
    }

    private void selectUp () {
        currentSelection--; //Up is subtraction because indexes are 0 at top

        if (currentSelection < 0)
            currentSelection = recipeResults.size() - 1;
    }

    private void selectDown () {
        currentSelection++;

        if (currentSelection >= recipeResults.size())
            currentSelection = 0;
    }

    private void craftSelection () {
        if (recipeAvailability.get(currentSelection) == false)
            return;

        CraftingRecipe recipe = invRenderer.getRecipe(recipeResults.get(currentSelection));
        GameState.inventory.craftItem(recipe);
        updateAllLists();
    }





    //
    //    Display
    //
    /*
    For the crafting screen, left = inventory, right = craft options
    */

    public void displayOutput (AsciiPanel terminal) {
        displayInventory(terminal);
        displayCraftingOptions(terminal, 40);
        displayCraftingDescription(terminal, 40, 19);
    }

    private void displayInventory (AsciiPanel terminal) {
        // Displaying the crafting ingredients
        int yCord = 1;
        terminal.write("Resources", 1, yCord, Color.CYAN); 
        yCord++;

        for (int i=0; i<resources.size(); i++) {
            terminal.write(resources.get(i), 1, yCord, Color.WHITE);
            yCord++;
        }

        // Displaying crafted items
        yCord += 2;
        terminal.write("Crafted Items", 1, yCord, Color.CYAN); 
        yCord++;

        for (int i=0; i<craftedItems.size(); i++) {
            ItemIndex item = craftedItems.get(i);
            int amount = GameState.inventory.getQuantity(item);
            terminal.write(item.toString() + " x" + amount, 1, yCord);
            yCord++;
        }

    }

    private void displayCraftingOptions (AsciiPanel terminal, int xOff) {
        for (int i=0; i<recipeResults.size(); i++) {
            Color backgroundColor = Color.BLACK;
            if (i == currentSelection) 
                backgroundColor = Color.DARK_GRAY;

            Color displayColor = Color.GRAY;
            if (recipeAvailability.get(i))
                displayColor = Color.WHITE;

            terminal.write(recipeResults.get(i), xOff, i+2, displayColor, backgroundColor);
        }
    }

    private void displayCraftingDescription (AsciiPanel terminal, int xOff, int yOff) {
        CraftingRecipe recipe = invRenderer.getRecipe(recipeResults.get(currentSelection));

        ArrayList<String> requirements = new ArrayList<String> ();
        ArrayList<Boolean> enough = new ArrayList<Boolean> ();
        ItemIndex[] requiredItems = recipe.inputItems();
        int[] requiredAmounts = recipe.inputAmounts();

        for (int i=0; i<requiredItems.length; i++) {
            ItemIndex resource = requiredItems[i];

            requirements.add(resource.toString() + " x " + requiredAmounts[i]);
            int givenAmount = GameState.inventory.getQuantity(resource);
            enough.add(givenAmount >= requiredAmounts[i]);
        }

        terminal.write(recipe.resultName(), xOff, yOff, Color.CYAN);
        terminal.write(recipe.description(), xOff, yOff+1);
        for (int i=0; i<requirements.size(); i++) {
            Color textColor = Color.WHITE;
            if (enough.get(i) == false)
                textColor = Color.RED;
            
            terminal.write(requirements.get(i), xOff, yOff + i + 2, textColor);
        }
    }

}