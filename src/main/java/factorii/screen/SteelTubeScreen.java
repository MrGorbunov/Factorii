package factorii.screen;

import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import factorii.GameState;
import factorii.InputBuffer;
import factorii.PressState;
import factorii.factory.FactoryItemTubeSteel;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.Controls;
import factorii.subscreen.GridSubscreen;
import factorii.subscreen.InventoryGridSubscreen;
import factorii.subscreen.PlayerInventorySubscreen;

public class SteelTubeScreen implements Screen {

    private InventoryGridSubscreen playerInvSubscreen;
    private InventoryGridSubscreen tubeInvSubscreen;
    private GridSubscreen filterModeSubscreen;

    private FactoryItemTubeSteel steelTube;
    private ScreenState screenState;

    private ArrayList<String> filterModes;

    public SteelTubeScreen (FactoryItemTubeSteel steelTube) {
        int screenWidth = GameState.windowWidth;
        int screenHeight = GameState.windowHeight;
        final int filterModeHeight = 7; 

        Inventory playerInv = GameState.player.getInventory();
        Inventory steelTubeInv = steelTube.getFilterInventory();

        // Warning: there are some places of code that rely on the fact
        // that whitelist is index 0 and blacklist is index 1
        filterModes = new ArrayList<String>();
        filterModes.add("Whitelist");
        filterModes.add("Blacklist");

        int centerX = screenWidth / 2;
        int centerXOff = (screenWidth + 1) / 2;

        playerInvSubscreen = new InventoryGridSubscreen(centerX, screenHeight, playerInv);
        tubeInvSubscreen = new InventoryGridSubscreen(centerX, screenHeight - filterModeHeight, centerXOff, 0, steelTubeInv);
        filterModeSubscreen = new GridSubscreen(centerX, filterModeHeight, centerXOff, screenHeight - filterModeHeight);

        playerInvSubscreen.setIgnoreResources(false);
        playerInvSubscreen.setColumns(1);
        playerInvSubscreen.setPad(2);
        playerInvSubscreen.setTitle("Your Inventory");
        playerInvSubscreen.setActive(true);
        playerInvSubscreen.refresh();

        tubeInvSubscreen.setIgnoreResources(false);
        tubeInvSubscreen.setColumns(1);
        tubeInvSubscreen.setPad(2);
        tubeInvSubscreen.setTitle("Filter Items");
        tubeInvSubscreen.setActive(false);
        tubeInvSubscreen.refresh();

        filterModeSubscreen.setOptions(filterModes);
        filterModeSubscreen.setPad(2);
        filterModeSubscreen.setTitle("Filter Mode");
        filterModeSubscreen.setActive(false);
        filterModeSubscreen.setMarkedOption(steelTube.getFilterMode() ? 0 : 1);
        filterModeSubscreen.refresh();

        this.steelTube = steelTube;
        screenState = ScreenState.LOOKING_AT_INVENTORY;
    }

    enum ScreenState {
        LOOKING_AT_INVENTORY,
        LOOKING_AT_FILTER_INVENTORY,
        LOOKING_AT_FILTER_MODE
    }

    public void displayOutput (AsciiPanel terminal) {
        playerInvSubscreen.drawSubscreen(terminal);
        tubeInvSubscreen.drawSubscreen(terminal);
        filterModeSubscreen.drawSubscreen(terminal);
    }





    //
    // Input Handling
    //
    
    public Screen update () {
        switch (screenState) {
            case LOOKING_AT_INVENTORY:
                return playerInventoryInput();
            case LOOKING_AT_FILTER_INVENTORY:
                return filterInventoryInput();
            case LOOKING_AT_FILTER_MODE:
                return filterModeInput();
        }

        return this;
    }

    private Screen playerInventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to filter inventory subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            playerInvSubscreen.setActive(false);
            tubeInvSubscreen.setActive(true);
            screenState = ScreenState.LOOKING_AT_FILTER_INVENTORY;

        // Add item to filter (does not consume)
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            ItemIndex newItem = playerInvSubscreen.getSelectedItem();
            // Because no consumption takes place, it's not important to check if an item can be added
            tubeInvSubscreen.getInventory().addItem(newItem);
            tubeInvSubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            playerInvSubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            playerInvSubscreen.moveDown();

        return this;
    }

    private Screen filterInventoryInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to filter mode subscreen
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            tubeInvSubscreen.setActive(false);
            filterModeSubscreen.setActive(true);
            screenState = ScreenState.LOOKING_AT_FILTER_MODE;

        // Remove item from filter
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            ItemIndex removeItem = tubeInvSubscreen.getSelectedItem();
            tubeInvSubscreen.getInventory().removeItem(removeItem);
            tubeInvSubscreen.refresh();
        
        // Scrolling
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED)
            playerInvSubscreen.moveUp();
        else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED)
            playerInvSubscreen.moveDown();

        return this;
    }

    private Screen filterModeInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit back to world
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED)
            return new WorldScreen();

        // Go to player inventory
        else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            filterModeSubscreen.setActive(false);
            playerInvSubscreen.setActive(true);
            screenState = ScreenState.LOOKING_AT_INVENTORY;

        // Set filter mode to selectedFilterMode
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            // true = whitelist, false = blacklist
            boolean newFilterMode = filterModeSubscreen.getSelectedIndex() == 0;
            filterModeSubscreen.setMarkedOptionToSelection();
            steelTube.setFilterMode(newFilterMode);
        
        // Scrolling (left & right)
        } else if (inputBuffer.pressState(Controls.DIR_LEFT) == PressState.JUST_PRESSED)
            filterModeSubscreen.moveLeft();
        else if (inputBuffer.pressState(Controls.DIR_RIGHT) == PressState.JUST_PRESSED)
            filterModeSubscreen.moveRight();

        return this;
    }

}