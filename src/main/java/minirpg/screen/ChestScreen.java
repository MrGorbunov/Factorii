package minirpg.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import minirpg.Controls;
import minirpg.GameState;
import minirpg.factory.FactoryChest;
import minirpg.inventory.CraftingRecipe;
import minirpg.inventory.ItemIndex;
import minirpg.subscreen.CraftingSubscreen;
import minirpg.subscreen.InventoryGridSubscreen;
import minirpg.subscreen.PlayerInventorySubscreen;


public class ChestScreen implements Screen {
    
    private int screenWidth;
    private int screenHeight;
    
    private InventoryGridSubscreen playerInventorySubscreen;
    private InventoryGridSubscreen chestInventorySubscreen;

    private ScreenState screenState;

    public ChestScreen (FactoryChest chest) {
        // TODO: Put these constants into gameState or settings
        //          (since this isn't really relevant to gamestate)

        screenWidth = 79;
        screenHeight = 24;
        int transferMenuHeight = 5;

        playerInventorySubscreen = new InventoryGridSubscreen(screenWidth / 2, screenHeight);
        chestInventorySubscreen = new InventoryGridSubscreen((screenWidth + 1) / 2, screenHeight - transferMenuHeight, (screenWidth+1) / 2, 0);

        screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;

        playerInventorySubscreen.setIgnoreResources(false);
        playerInventorySubscreen.setColumns(1);
        playerInventorySubscreen.setPad(2);
        playerInventorySubscreen.setTitle("Your Inventory");
        playerInventorySubscreen.setActive(true);
        playerInventorySubscreen.refresh();

        chestInventorySubscreen.setInventory(chest.getInventory());
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

    public Screen handleInput (KeyEvent key) {
        switch (screenState) {
            case LOOKING_AT_CHEST_INVENTORY:
                return chestInput(key);

            case LOOKING_AT_PLAYER_INVENTORY:
                return inventoryInput(key);
        }

        return this;
    }

    private Screen chestInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                return new WorldScreen();
            
            case KeyEvent.VK_UP:
                chestInventorySubscreen.moveUp();
                break;
            
            case KeyEvent.VK_DOWN:
                chestInventorySubscreen.moveDown();
                break;
            
            case KeyEvent.VK_C: // Transfer item from chest to player
                ItemIndex transferItem = chestInventorySubscreen.getSelectedItem();
                playerInventorySubscreen.getInventory().addItem(transferItem);
                chestInventorySubscreen.getInventory().removeItem(transferItem);
                chestInventorySubscreen.refresh();
                playerInventorySubscreen.refresh();
                break;

            case KeyEvent.VK_Z: // Switch active subscreen
                playerInventorySubscreen.setActive(true);
                chestInventorySubscreen.setActive(false);
                screenState = ScreenState.LOOKING_AT_PLAYER_INVENTORY;
                break;
        }

        return this;
    }

    private Screen inventoryInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                return new WorldScreen();

            case KeyEvent.VK_UP:
                playerInventorySubscreen.moveUp();
                break;

            case KeyEvent.VK_DOWN:
                playerInventorySubscreen.moveDown();
                break;
            
            case KeyEvent.VK_C: // Transfer item from player to chest
                // TODO: Limit how much can be transfered
                ItemIndex transferItem = playerInventorySubscreen.getSelectedItem();
                chestInventorySubscreen.getInventory().addItem(transferItem);
                playerInventorySubscreen.getInventory().removeItem(transferItem);
                playerInventorySubscreen.refresh();
                chestInventorySubscreen.refresh();
                break;

            case KeyEvent.VK_Z: // Swtich active subscreen
                chestInventorySubscreen.setActive(true);
                playerInventorySubscreen.setActive(false);
                screenState = ScreenState.LOOKING_AT_CHEST_INVENTORY;
                break;
        }

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

