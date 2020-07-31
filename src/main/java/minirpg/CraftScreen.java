package minirpg;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import minirpg.inventory.CraftingRenderer;
import minirpg.inventory.ItemIndex;

public class CraftScreen implements Screen {
    
    private CraftingRenderer invRenderer;

    public CraftScreen () {
        invRenderer = new CraftingRenderer();
    }

    public Screen handleInput (KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_C)
            return new WorldScreen();

        return this;
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
    }

    private void displayInventory (AsciiPanel terminal) {
        ArrayList<String> resources = invRenderer.getResourceList();

        // Displaying the crafting ingredients
        terminal.write("Resources", 1, 1, Color.CYAN);
        for (int i=0; i<resources.size(); i++) {
            terminal.write(resources.get(i), 1, i+2, Color.WHITE);
        }
    }


    private void displayCraftingOptions (AsciiPanel terminal, int xOff) {
        ArrayList<String> craftRecipes = invRenderer.recipesUnlocked();
        ArrayList<Boolean> craftAvalability = invRenderer.recipesCraftable();

        for (int i=0; i<craftRecipes.size(); i++) {
            Color displayColor = Color.GRAY;
            if (craftAvalability.get(i))
                displayColor = Color.WHITE;

            terminal.write(craftRecipes.get(i), xOff, i+2, displayColor);
        }
    }


}