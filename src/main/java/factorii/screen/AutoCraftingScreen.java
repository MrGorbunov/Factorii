package factorii.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import factorii.Controls;
import factorii.GameState;
import factorii.InputBuffer;
import factorii.PressState;
import factorii.factory.FactoryAutoCrafter;
import factorii.factory.FactoryChest;
import factorii.inventory.CraftingRecipe;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.subscreen.CraftingSubscreen;
import factorii.subscreen.InventoryGridSubscreen;
import factorii.subscreen.PlayerInventorySubscreen;

public class AutoCraftingScreen implements Screen {
    
    private PlayerInventorySubscreen playerInventorySubscreen;
    private CraftingSubscreen workbenchCraftsSubscreen;
    private InventoryGridSubscreen workbenchInventorySubscreen;
    private ScreenState screenState;

    private FactoryAutoCrafter autoCrafter;


    public AutoCraftingScreen (FactoryAutoCrafter autoCrafter) {
        int screenWidth = GameState.windowWidth;
        int screenHeight = GameState.windowHeight;
        int inventoryHeight = 8;
        int workbenchSubscreensX = (screenWidth + 1) / 2;

        Inventory playerInventory = GameState.player.getInventory();
        Inventory workbenchInv = autoCrafter.getInventory(); 

        playerInventorySubscreen = new PlayerInventorySubscreen(screenWidth / 2, screenHeight);
        workbenchCraftsSubscreen = new CraftingSubscreen(workbenchSubscreensX, screenHeight - inventoryHeight, workbenchSubscreensX, 0, autoCrafter);
        workbenchInventorySubscreen = new InventoryGridSubscreen(workbenchSubscreensX, inventoryHeight, workbenchSubscreensX, screenHeight - inventoryHeight, workbenchInv);

        screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        playerInventorySubscreen.setActive(true);

        workbenchCraftsSubscreen.setDrawDescription(false);
        workbenchCraftsSubscreen.setSetRecipe(autoCrafter.getSelectedRecipe());
        workbenchCraftsSubscreen.setChangeColorIfAvailable(false);
        workbenchCraftsSubscreen.refresh();

        workbenchInventorySubscreen.setIgnoreResources(false);
        workbenchInventorySubscreen.setColumns(1);
        workbenchInventorySubscreen.setPad(2);
        workbenchInventorySubscreen.setTitle("Workbench Stored Items");
        workbenchInventorySubscreen.setActive(false);
        workbenchInventorySubscreen.refresh();


        // State stuff
        this.autoCrafter = autoCrafter;
    }

    enum ScreenState {
        LOOKING_AT_PLAYER_INVENTORY,
        LOOKING_AT_WORKBENCH_CRAFTS;
    }





    //
    // Input handling
    //

    public Screen update () {
        switch (screenState) {
            case LOOKING_AT_PLAYER_INVENTORY:
                return playerInventoryInput();

            case LOOKING_AT_WORKBENCH_CRAFTS:
                return workbenchCraftInput();
        }

        return this;
    }

    private Screen playerInventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to workbench craft selection subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            workbenchCraftsSubscreen.setActive(true);
            playerInventorySubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_WORKBENCH_CRAFTS;

        // Never transfer from player to chest

        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            playerInventorySubscreen.scrollUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            playerInventorySubscreen.scrollDown();

        return this;
    }

    private Screen workbenchCraftInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to player inventory screen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            playerInventorySubscreen.setActive(true);
            workbenchCraftsSubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        // Set new recipe
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            CraftingRecipe selectedRecipe = workbenchCraftsSubscreen.getSelectedRecipe();
            workbenchCraftsSubscreen.setSetRecipe(selectedRecipe);
            autoCrafter.setRecipe(selectedRecipe);
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            workbenchCraftsSubscreen.selectionUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            workbenchCraftsSubscreen.selectionDown();

        return this;
    }




    //
    // Display logic
    //
     
    public void displayOutput (AsciiPanel terminal) {
        playerInventorySubscreen.drawSubscreen(terminal);
        workbenchCraftsSubscreen.drawSubscreen(terminal);
        workbenchInventorySubscreen.drawSubscreen(terminal);
    }

}
