package factorii.screen;

import factorii.Controls;
import factorii.GameState;
import factorii.InputBuffer;
import factorii.Player;
import factorii.PressState;
import factorii.factory.FactoryChest;
import factorii.factory.FactoryCrafter;
import factorii.factory.FactoryItemTubeSteel;
import factorii.factory.FactoryStatic;
import factorii.factory.FacData;
import factorii.factory.FactoryAutoCrafter;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.subscreen.*;
import factorii.world.*;

import asciiPanel.AsciiPanel;


public class WorldScreen implements Screen {
    private WorldSubscreen worldSubscreen;
    private WorldPlacementSubscreen worldPlacementSubscreen;
    private InventoryGridSubscreen inventoryGridSubscreen;

    private ScreenState screenState;

    public WorldScreen () {
        int width = GameState.windowWidth;
        int height = GameState.windowHeight;

        int inventoryGridHeight = 5;

        Inventory playerInv = GameState.player.getInventory();

        worldSubscreen = new WorldSubscreen(width, height - inventoryGridHeight, 0, 0);
        worldPlacementSubscreen = new WorldPlacementSubscreen(width, height - inventoryGridHeight, 0, 0);
        inventoryGridSubscreen = new InventoryGridSubscreen(width, inventoryGridHeight, 0, height - inventoryGridHeight, playerInv);

        screenState = ScreenState.MOVING_IN_WORLD;
        inventoryGridSubscreen.setActive(false);
        inventoryGridSubscreen.refresh();
    }

    enum ScreenState {
        MOVING_IN_WORLD, 
        SELECTING_FROM_INVENTORY, 
        PLACING_IN_WORLD  
    }




    //
    // Input handling
    //

    public Screen update () {
        switch (screenState) {
            case MOVING_IN_WORLD:
                // player will automatically look at input buffer & update accordingly
                GameState.player.update();
                GameState.factory.update();
                return traversalInput();

            case SELECTING_FROM_INVENTORY:
                return selectionInput();

            case PLACING_IN_WORLD:
                return placementInput();
        }

        return this;
    }

    private Screen traversalInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Open adjacent inventory / crafting thing
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED) {
            return gotoFactoryScreen();
        
        // Goto selection subscreen
        } else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            boolean canTransition = inventoryGridSubscreen.setActive(true);
            if (canTransition)
                screenState = ScreenState.SELECTING_FROM_INVENTORY;
            return this;
        
        // Harvest adjacent
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            if (inputBuffer.pressState(Controls.MODIFIER) == PressState.PRESSED ||
                inputBuffer.pressState(Controls.MODIFIER) == PressState.JUST_PRESSED)
                    GameState.factory.harvestAdjacentToPlayer();
            else
                GameState.world.harvestAdjacentToPlayer();

            inventoryGridSubscreen.refresh();
            return this;
        } 
        
        return this;
    }

    private Screen selectionInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Goto crafting
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED) {
            return gotoFactoryScreen();
        
        // Get out of selection
        } else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            screenState = ScreenState.MOVING_IN_WORLD;
            inventoryGridSubscreen.setActive(false);
        
        // Go to placement mode
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            screenState = ScreenState.PLACING_IN_WORLD;
            ItemIndex selectedItem = inventoryGridSubscreen.getSelectedItem();
            if (selectedItem == null) 
                return this;

            Tile activeTile = ItemIndex.itemToTile(selectedItem);
            worldPlacementSubscreen.setActiveTile(activeTile);
            worldPlacementSubscreen.refresh();
            inventoryGridSubscreen.setActive(false);

        // I don't use the xInput() and yInput() because these are taps and it's not character movement
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED) {
            inventoryGridSubscreen.moveUp();

        } else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED) {
            inventoryGridSubscreen.moveDown();

        } else if (inputBuffer.pressState(Controls.DIR_LEFT) == PressState.JUST_PRESSED) {
            inventoryGridSubscreen.moveLeft();

        } else if (inputBuffer.pressState(Controls.DIR_RIGHT) == PressState.JUST_PRESSED) {
            inventoryGridSubscreen.moveRight();

        }

        return this;
    }

    private Screen placementInput () {
        InputBuffer inputBuffer = GameState.inputBuffer;

        // Exit placement mode
        if (inputBuffer.pressState(Controls.OPEN_SCREEN) == PressState.JUST_PRESSED) {
            screenState = ScreenState.MOVING_IN_WORLD;
        
        // Go back to selection mode
        } else if (inputBuffer.pressState(Controls.SWITCH_SUBSCREEN) == PressState.JUST_PRESSED) {
            boolean canGotoInventorygrid = inventoryGridSubscreen.setActive(true);

            if (canGotoInventorygrid)
                screenState = ScreenState.SELECTING_FROM_INVENTORY;
            else
                screenState = ScreenState.MOVING_IN_WORLD;
        
        // Attempt to place down 
        } else if (inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) {
            boolean placedSuccesfully = worldPlacementSubscreen.placeItem();
            if (placedSuccesfully) {
                // No null-checks because guaranteed (fingers crossed) that the item is there
                GameState.player.getInventory().removeItem(inventoryGridSubscreen.getSelectedItem());

                // Only move out if no more of the item is left
                if (GameState.player.getInventory().getQuantity(inventoryGridSubscreen.getSelectedItem()) == 0) {
                    screenState = ScreenState.MOVING_IN_WORLD;
                    inventoryGridSubscreen.setActive(false);
                }

                inventoryGridSubscreen.refresh();
                worldPlacementSubscreen.refresh();
            }

        // I don't use the xInput() and yInput() because these are taps and it's not character movement
        } else if (inputBuffer.pressState(Controls.DIR_UP) == PressState.JUST_PRESSED) {
                worldPlacementSubscreen.moveUp();

        } else if (inputBuffer.pressState(Controls.DIR_DOWN) == PressState.JUST_PRESSED) {
            worldPlacementSubscreen.moveDown();

        } else if (inputBuffer.pressState(Controls.DIR_LEFT) == PressState.JUST_PRESSED) {
            worldPlacementSubscreen.moveLeft();

        } else if (inputBuffer.pressState(Controls.DIR_RIGHT) == PressState.JUST_PRESSED) {
            worldPlacementSubscreen.moveRight();

        }

        return this;
    }

    private Screen gotoFactoryScreen () {
        Player player = GameState.player;
        FacData factoryData = GameState.factory.getAdjacentFacData(player.getX(), player.getY());
        if (factoryData != null) {
            if (factoryData instanceof FactoryCrafter)
                return new CraftScreen(factoryData);

            else if (factoryData instanceof FactoryChest)
                return new ChestScreen((FactoryChest) factoryData);

            else if (factoryData instanceof FactoryAutoCrafter)
                return new AutoCraftingScreen((FactoryAutoCrafter) factoryData);

            else if (factoryData instanceof FactoryItemTubeSteel)
                return new SteelTubeScreen((FactoryItemTubeSteel) factoryData);
        }

        return new CraftScreen(new FactoryStatic(Tile.EMPTY));
    }




    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        if (screenState == ScreenState.PLACING_IN_WORLD)
            worldPlacementSubscreen.drawSubscreen(terminal);
        else {
            worldSubscreen.drawSubscreen(terminal);
        }
        
        inventoryGridSubscreen.drawSubscreen(terminal);
    }

}
