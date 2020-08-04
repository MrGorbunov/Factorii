package minirpg.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import minirpg.GameState;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.ItemIndex;
import minirpg.subscreen.CraftingSubscreen;
import minirpg.subscreen.InventorySubscreen;


// TODO: When one screen is active, arrows & selection of the other should dissapear

public class CraftScreen implements Screen {
    
    private int screenWidth;
    private int screenHeight;
    
    private InventorySubscreen inventorySubscreen;
    private CraftingSubscreen craftingSubscreen;

    private boolean lookingAtInventory = false;

    public CraftScreen () {
        // TODO: Put these constants into gameState or settings
        //          (since this isn't really relevant to gamestate)

        screenWidth = 79;
        screenHeight = 24;

        inventorySubscreen = new InventorySubscreen(screenWidth / 2, screenHeight);
        craftingSubscreen = new CraftingSubscreen(screenWidth / 2, screenHeight, (screenWidth+1) / 2, 0);

        inventorySubscreen.setActive(lookingAtInventory);
        craftingSubscreen.setActive(!lookingAtInventory);
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new WorldScreen();
            
            case KeyEvent.VK_UP:
                scrollDirection(true);
                break;

            case KeyEvent.VK_DOWN:
                scrollDirection(false);
                break;

            case KeyEvent.VK_Z:
                switchActiveSubscreen();
                break;
            
            case KeyEvent.VK_X:
                // TODO: Craft selection
                break;

        }

        return this;
    }

    public void displayOutput (AsciiPanel terminal) {
        inventorySubscreen.drawSubscreen(terminal);
        craftingSubscreen.drawSubscreen(terminal);
    }





    //
    // State logic
    //

    private void switchActiveSubscreen () {
        if (lookingAtInventory) {
            inventorySubscreen.setActive(false);
            craftingSubscreen.setActive(true);
            lookingAtInventory = false;

        } else {
            inventorySubscreen.setActive(true);
            craftingSubscreen.setActive(false);
            lookingAtInventory = true;

        }
    }

    /**
     * goingUp = False -> moves down
     * goingUp = True  -> moves up
     */
    private void scrollDirection (boolean goingUp) {
        if (lookingAtInventory) {
            if (goingUp) 
                inventorySubscreen.scrollUp();
            else
                inventorySubscreen.scrollDown();

        } else {
            if (goingUp)
                craftingSubscreen.selectionUp();
            else
                craftingSubscreen.selectionDown();
        }
    }

}