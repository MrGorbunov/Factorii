package minirpg.screen;

import minirpg.Controls;
import minirpg.GameState;
import minirpg.factory.FactoryChest;
import minirpg.factory.FactoryData;
import minirpg.inventory.ItemIndex;
import minirpg.subscreen.InventoryGridSubscreen;
import minirpg.subscreen.WorldPlacementSubscreen;
import minirpg.subscreen.WorldSubscreen;
import minirpg.world.*;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;


public class WorldScreen implements Screen {
    private int screenWidth;
    private int screenHeight;

    private WorldSubscreen worldSubscreen;
    private WorldPlacementSubscreen worldPlacementSubscreen;
    private InventoryGridSubscreen inventoryGridSubscreen;

    private ScreenState screenState;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;

        worldSubscreen = new WorldSubscreen(screenWidth, screenHeight - 5);
        worldPlacementSubscreen = new WorldPlacementSubscreen(screenWidth, screenHeight - 5);
        inventoryGridSubscreen = new InventoryGridSubscreen(screenWidth, 5, 0, screenHeight - 5);

        screenState = ScreenState.MOVING_IN_WORLD;
        inventoryGridSubscreen.setActive(false);
    }

    enum ScreenState {
        MOVING_IN_WORLD, 
        SELECTING_FROM_INVENTORY, 
        PLACING_IN_WORLD  
    }




    //
    // Input handling
    //

    public Screen handleInput (KeyEvent key) {
        switch (screenState) {
            case MOVING_IN_WORLD:
                return traversalInput(key);

            case SELECTING_FROM_INVENTORY:
                return selectionInput(key);

            case PLACING_IN_WORLD:
                return placementInput(key);
        }

        return this;
    } 

    private Screen traversalInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Open adjacent inventory / crafting thing
            case Controls.INTERACT:
                FactoryData factoryData = GameState.world.getAdjacentFactoryData();
                if (factoryData == null)
                    return new CraftScreen();
                
                Tile factoryTile = factoryData.getTile();
                if (factoryTile == Tile.CHEST)
                    return new ChestScreen((FactoryChest) factoryData);

                return new CraftScreen();
            
            // Goto selection
            case Controls.UI_CYCLE:
                boolean canTransition = inventoryGridSubscreen.setActive(true);
                if (canTransition)
                    screenState = ScreenState.SELECTING_FROM_INVENTORY;
                break;
            
            case Controls.ACTION:
                GameState.world.harvestAdjacent();
                break;

            case Controls.DIR_UP:
                GameState.player.moveUp();
                break;

            case Controls.DIR_DOWN:
                GameState.player.moveDown();
                break;
            
            case Controls.DIR_LEFT:
                GameState.player.moveLeft();
                break;
            
            case Controls.DIR_RIGHT:
                GameState.player.moveRight();
                break;
        }

        return this;
    }

    private Screen selectionInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Goto crafting
            case Controls.INTERACT:
                return new CraftScreen();

            // Get out of selection
            case Controls.UI_CYCLE:
                screenState = ScreenState.MOVING_IN_WORLD;
                inventoryGridSubscreen.setActive(false);
                break;
            
            // Go into placement mode
            case Controls.ACTION:
                screenState = ScreenState.PLACING_IN_WORLD;

                ItemIndex selectedItem = inventoryGridSubscreen.getSelectedItem();
                Tile activeTile = ItemIndex.itemToTile(selectedItem);
                worldPlacementSubscreen.setActiveTile(activeTile);
                worldPlacementSubscreen.refresh();

                inventoryGridSubscreen.setActive(false);
                break;

            case Controls.DIR_UP:
                inventoryGridSubscreen.moveUp();
                break;

            case Controls.DIR_DOWN:
                inventoryGridSubscreen.moveDown();
                break;
            
            case Controls.DIR_LEFT:
                inventoryGridSubscreen.moveLeft();
                break;
            
            case Controls.DIR_RIGHT:
                inventoryGridSubscreen.moveRight();
                break;
        }

        return this;
    }

    private Screen placementInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Exit placement mode
            case Controls.INTERACT:
                screenState = ScreenState.MOVING_IN_WORLD;
                break;

            // Goto selection mode
            case Controls.UI_CYCLE:
                boolean canGotoInventorygrid = inventoryGridSubscreen.setActive(true);

                if (canGotoInventorygrid)
                    screenState = ScreenState.SELECTING_FROM_INVENTORY;
                else
                    screenState = ScreenState.MOVING_IN_WORLD;
                break;
            
            // Place down object
            case Controls.ACTION:
                boolean placedSuccesfully = worldPlacementSubscreen.placeItem();
                if (placedSuccesfully) {
                    GameState.player.getInventory().removeItem(inventoryGridSubscreen.getSelectedItem());
                    inventoryGridSubscreen.refresh();
                    screenState = ScreenState.MOVING_IN_WORLD;
                }
                break;

            case Controls.DIR_UP:
                worldPlacementSubscreen.moveUp();
                break;

            case Controls.DIR_DOWN:
                worldPlacementSubscreen.moveDown();
                break;
            
            case Controls.DIR_LEFT:
                worldPlacementSubscreen.moveLeft();
                break;
            
            case Controls.DIR_RIGHT:
                worldPlacementSubscreen.moveRight();
                break;
        }

        return this;
    }





    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        if (screenState == ScreenState.PLACING_IN_WORLD)
            worldPlacementSubscreen.drawSubscreen(terminal);
        else {
            GameState.world.refresh();
            worldSubscreen.drawSubscreen(terminal);
        }
        
        inventoryGridSubscreen.drawSubscreen(terminal);
    }

}
