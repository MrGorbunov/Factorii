package minirpg.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import minirpg.Controls;
import minirpg.GameState;
import minirpg.InputBuffer;
import minirpg.PressState;
import minirpg.factory.FactoryAutoCrafter;
import minirpg.factory.FactoryChest;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.ItemIndex;
import minirpg.subscreen.CraftingSubscreen;
import minirpg.subscreen.InventoryGridSubscreen;
import minirpg.subscreen.PlayerInventorySubscreen;

public class AutoCraftingScreen implements Screen {
    
    private InventoryGridSubscreen playerInventorySubscreen;
    private CraftingSubscreen workbenchCraftsSubscreen;
    private InventoryGridSubscreen workbenchInventorySubscreen;
    private ScreenState screenState;

    private FactoryAutoCrafter autoCrafter;


    public AutoCraftingScreen (FactoryAutoCrafter autoCrafter) {
        int screenWidth = GameState.windowWidth;
        int screenHeight = GameState.windowHeight;
        int inventoryHeight = 8;
        int workbenchSubscreensX = (screenWidth + 1) / 2;

        playerInventorySubscreen = new InventoryGridSubscreen(screenWidth / 2, screenHeight);
        workbenchCraftsSubscreen = new CraftingSubscreen(workbenchSubscreensX, screenHeight - inventoryHeight, workbenchSubscreensX, 0);
        workbenchInventorySubscreen = new InventoryGridSubscreen(workbenchSubscreensX, inventoryHeight, workbenchSubscreensX, screenHeight - inventoryHeight);

        screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        playerInventorySubscreen.setIgnoreResources(false);
        playerInventorySubscreen.setColumns(1);
        playerInventorySubscreen.setPad(2);
        playerInventorySubscreen.setTitle("Your Inventory");
        playerInventorySubscreen.setActive(true);
        playerInventorySubscreen.refresh();

        workbenchCraftsSubscreen.setDrawDescription(false);
        workbenchCraftsSubscreen.setSetRecipe(autoCrafter.getSelectedRecipe());
        workbenchCraftsSubscreen.setChangeColorIfAvailable(false);
        workbenchCraftsSubscreen.refresh();

        workbenchInventorySubscreen.setInventory(autoCrafter.getInventory());
        workbenchInventorySubscreen.setIgnoreResources(false);
        workbenchInventorySubscreen.setColumns(1);
        workbenchInventorySubscreen.setPad(2);
        workbenchInventorySubscreen.setTitle("Workbench Inventory");
        workbenchInventorySubscreen.setActive(false);
        workbenchInventorySubscreen.refresh();


        // State stuff
        this.autoCrafter = autoCrafter;
    }

    enum ScreenState {
        LOOKING_AT_PLAYER_INVENTORY,
        LOOKING_AT_WORKBENCH_INVENTORY,
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

            case LOOKING_AT_WORKBENCH_INVENTORY:
                return workbenchInventoryInput();
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

        // Transfer from player to chest
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            // TODO: Limit chest capacity
            ItemIndex transferItem = playerInventorySubscreen.getSelectedItem();
            if (transferItem == null)
                return this;

            workbenchInventorySubscreen.getInventory().addItem(transferItem);
            playerInventorySubscreen.getInventory().removeItem(transferItem);
            playerInventorySubscreen.refresh();
            workbenchInventorySubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            playerInventorySubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            playerInventorySubscreen.moveDown();

        return this;
    }

    private Screen workbenchCraftInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to workbench inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            workbenchInventorySubscreen.setActive(true);
            workbenchCraftsSubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_WORKBENCH_INVENTORY;

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

    private Screen workbenchInventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to player inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            playerInventorySubscreen.setActive(true);
            workbenchInventorySubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        // Transfer from chest to player
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            ItemIndex transferItem = workbenchInventorySubscreen.getSelectedItem();
            if (transferItem == null)
                return this;

            playerInventorySubscreen.getInventory().addItem(transferItem);
            workbenchInventorySubscreen.getInventory().removeItem(transferItem);
            workbenchInventorySubscreen.refresh();
            playerInventorySubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            workbenchInventorySubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            workbenchInventorySubscreen.moveDown();

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
