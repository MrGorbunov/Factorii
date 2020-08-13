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


public class CraftScreen implements Screen {
    
    private int screenWidth;
    private int screenHeight;
    
    private InventorySubscreen inventorySubscreen;
    private CraftingSubscreen craftingSubscreen;

    private ScreenState screenState;

    public CraftScreen () {
        // TODO: Put these constants into gameState or settings
        //          (since this isn't really relevant to gamestate)

        screenWidth = 79;
        screenHeight = 24;

        inventorySubscreen = new InventorySubscreen(screenWidth / 2, screenHeight);
        craftingSubscreen = new CraftingSubscreen(screenWidth / 2, screenHeight, (screenWidth+1) / 2, 0);

        screenState = ScreenState.LOOKING_AT_CRAFTING_PANEL;
        inventorySubscreen.setActive(false);
        craftingSubscreen.setActive(true);
    }

    enum ScreenState {
        LOOKING_AT_INVENTORY,
        LOOKING_AT_CRAFTING_PANEL
    }





    //
    // Input handling
    //

    public Screen handleInput (KeyEvent key) {
        switch (screenState) {
            case LOOKING_AT_CRAFTING_PANEL:
                return craftingInput(key);

            case LOOKING_AT_INVENTORY:
                return inventoryInput(key);
        }

        return this;
    }

    private Screen craftingInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new WorldScreen();
            
            case KeyEvent.VK_UP:
                craftingSubscreen.selectionUp();
                break;
            
            case KeyEvent.VK_DOWN:
                craftingSubscreen.selectionDown();
                break;
            
            case KeyEvent.VK_SPACE:
                craftingSubscreen.craftSelection();
                break;

            case KeyEvent.VK_Z: // Switch active subscreen
                inventorySubscreen.setActive(true);
                craftingSubscreen.setActive(false);
                screenState = ScreenState.LOOKING_AT_INVENTORY;
                break;
        }

        return this;
    }

    private Screen inventoryInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new WorldScreen();

            
            case KeyEvent.VK_UP:
                inventorySubscreen.scrollUp();
                break;

            case KeyEvent.VK_DOWN:
                inventorySubscreen.scrollDown();
                break;

            case KeyEvent.VK_Z: // Swtich active subscreen
                craftingSubscreen.setActive(true);
                inventorySubscreen.setActive(false);
                screenState = ScreenState.LOOKING_AT_CRAFTING_PANEL;
                break;
        }

        return this;
    }





    //
    // Display logic
    //
     
    public void displayOutput (AsciiPanel terminal) {
        inventorySubscreen.drawSubscreen(terminal);
        craftingSubscreen.drawSubscreen(terminal);
    }

}
