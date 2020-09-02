package factorii.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import factorii.Controls;
import factorii.GameState;
import factorii.InputBuffer;
import factorii.PressState;
import factorii.factory.FactoryChest;
import factorii.inventory.CraftingRecipe;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.subscreen.CraftingSubscreen;
import factorii.subscreen.InventoryGridSubscreen;
import factorii.subscreen.PlayerInventorySubscreen;


public class ChestScreen implements Screen {
    
    private InventoryGridSubscreen playerInventorySubscreen;
    private InventoryGridSubscreen chestInventorySubscreen;

    private ScreenState screenState;

    public ChestScreen (FactoryChest chest) {
        int screenWidth = GameState.windowWidth;
        int screenHeight = GameState.windowHeight;
        int transferMenuHeight = 5;

        Inventory playerInv = GameState.player.getInventory();
        Inventory chestInv = chest.getInventory();

        playerInventorySubscreen = new InventoryGridSubscreen(screenWidth / 2, screenHeight, playerInv);
        chestInventorySubscreen = new InventoryGridSubscreen((screenWidth + 1) / 2, screenHeight - transferMenuHeight, (screenWidth+1) / 2, 0, chestInv);

        screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        playerInventorySubscreen.setIgnoreResources(false);
        playerInventorySubscreen.setColumns(1);
        playerInventorySubscreen.setPad(2);
        playerInventorySubscreen.setTitle("Your Inventory");
        playerInventorySubscreen.setActive(true);
        playerInventorySubscreen.refresh();

        chestInventorySubscreen.setIgnoreResources(false);
        chestInventorySubscreen.setColumns(1);
        chestInventorySubscreen.setPad(2);
        chestInventorySubscreen.setTitle("Chest Inventory");
        chestInventorySubscreen.setActive(false);
        chestInventorySubscreen.refresh();
    }

    enum ScreenState {
        LOOKING_AT_PLAYER_INVENTORY,
        LOOKING_AT_CHEST_INVENTORY
    }





    //
    // Input handling
    //

    public Screen update () {
        switch (screenState) {
            case LOOKING_AT_CHEST_INVENTORY:
                return chestInput();

            case LOOKING_AT_PLAYER_INVENTORY:
                return inventoryInput();
        }

        return this;
    }

    private Screen chestInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            playerInventorySubscreen.setActive(true);
            chestInventorySubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        // Transfer from chest to player
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            ItemIndex transferItem = chestInventorySubscreen.getSelectedItem();
            if (transferItem == null)
                return this;

            int quantity = 1;
            if (inputBuffer.pressState(Controls.MODIFIER) == PressState.PRESSED ||
                inputBuffer.pressState(Controls.MODIFIER) == PressState.JUST_PRESSED)  
                    quantity = Math.min(chestInventorySubscreen.getInventory().getQuantity(transferItem), 5);

            playerInventorySubscreen.getInventory().addItemMulti(transferItem, quantity);
            chestInventorySubscreen.getInventory().removeItemMulti(transferItem, quantity);
            chestInventorySubscreen.refresh();
            playerInventorySubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            chestInventorySubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            chestInventorySubscreen.moveDown();

        return this;
    }

    private Screen inventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to chest subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            chestInventorySubscreen.setActive(true);
            playerInventorySubscreen.setActive(false);
            screenState = ScreenState.LOOKING_AT_CHEST_INVENTORY;

        // Transfer from player to chest
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            // TODO: Limit chest capacity
            ItemIndex transferItem = playerInventorySubscreen.getSelectedItem();
            if (transferItem == null)
                return this;

            int quantity = 1;
            if (inputBuffer.pressState(Controls.MODIFIER) == PressState.PRESSED ||
                inputBuffer.pressState(Controls.MODIFIER) == PressState.JUST_PRESSED)  
                    quantity = Math.min(playerInventorySubscreen.getInventory().getQuantity(transferItem), 5);

            chestInventorySubscreen.getInventory().addItemMulti(transferItem, quantity); 
            playerInventorySubscreen.getInventory().removeItemMulti(transferItem, quantity);
            playerInventorySubscreen.refresh();
            chestInventorySubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            playerInventorySubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            playerInventorySubscreen.moveDown();

        return this;
    }





    //
    // Display logic
    //
     
    public void displayOutput (AsciiPanel terminal) {
        playerInventorySubscreen.drawSubscreen(terminal);
        chestInventorySubscreen.drawSubscreen(terminal);
    }

}

