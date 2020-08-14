package minirpg.screen;

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
            case KeyEvent.VK_C:
                FactoryData factoryData = GameState.world.getAdjacentFactoryData();
                if (factoryData == null)
                    return new CraftScreen();

                Tile adjacentFactoryTile = factoryData.getTile();
                if (adjacentFactoryTile == Tile.WORKBENCH ||
                    adjacentFactoryTile == Tile.KILN ||
                    adjacentFactoryTile == Tile.FORGE) 
                        return new CraftScreen();
                
                if (adjacentFactoryTile == Tile.CHEST)
                    return new ChestScreen((FactoryChest) factoryData);
            
            // Goto selection
            case KeyEvent.VK_Z:
                boolean canTransition = inventoryGridSubscreen.setActive(true);
                if (canTransition)
                    screenState = ScreenState.SELECTING_FROM_INVENTORY;
                break;
            
            case KeyEvent.VK_SPACE:
                GameState.world.harvestAdjacent();
                break;

            case KeyEvent.VK_UP:
                GameState.player.moveUp();
                break;

            case KeyEvent.VK_DOWN:
                GameState.player.moveDown();
                break;
            
            case KeyEvent.VK_LEFT:
                GameState.player.moveLeft();
                break;
            
            case KeyEvent.VK_RIGHT:
                GameState.player.moveRight();
                break;
        }

        return this;
    }

    private Screen selectionInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Goto crafting
            case KeyEvent.VK_C:
                return new CraftScreen();

            // Get out of selection
            case KeyEvent.VK_Z:
                screenState = ScreenState.MOVING_IN_WORLD;
                inventoryGridSubscreen.setActive(false);
                break;
            
            // Go into placement mode
            case KeyEvent.VK_SPACE:
                screenState = ScreenState.PLACING_IN_WORLD;

                ItemIndex selectedItem = inventoryGridSubscreen.getSelectedItem();
                Tile activeTile = ItemIndex.itemToTile(selectedItem);
                worldPlacementSubscreen.setActiveTile(activeTile);
                worldPlacementSubscreen.refresh();

                inventoryGridSubscreen.setActive(false);
                break;

            case KeyEvent.VK_UP:
                inventoryGridSubscreen.moveUp();
                break;

            case KeyEvent.VK_DOWN:
                inventoryGridSubscreen.moveDown();
                break;
            
            case KeyEvent.VK_LEFT:
                inventoryGridSubscreen.moveLeft();
                break;
            
            case KeyEvent.VK_RIGHT:
                inventoryGridSubscreen.moveRight();
                break;
        }

        return this;
    }

    private Screen placementInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Exit placement mode
            case KeyEvent.VK_C:
                screenState = ScreenState.MOVING_IN_WORLD;
                break;

            // Goto selection mode
            case KeyEvent.VK_Z:
                boolean canGotoInventorygrid = inventoryGridSubscreen.setActive(true);

                if (canGotoInventorygrid)
                    screenState = ScreenState.SELECTING_FROM_INVENTORY;
                else
                    screenState = ScreenState.MOVING_IN_WORLD;
                break;
            
            // Place down object
            case KeyEvent.VK_SPACE:
                boolean placedSuccesfully = worldPlacementSubscreen.placeItem();
                if (placedSuccesfully) {
                    GameState.player.getInventory().removeItem(inventoryGridSubscreen.getSelectedItem());
                    inventoryGridSubscreen.refresh();
                    screenState = ScreenState.MOVING_IN_WORLD;
                }
                break;

            case KeyEvent.VK_UP:
                worldPlacementSubscreen.moveUp();
                break;

            case KeyEvent.VK_DOWN:
                worldPlacementSubscreen.moveDown();
                break;
            
            case KeyEvent.VK_LEFT:
                worldPlacementSubscreen.moveLeft();
                break;
            
            case KeyEvent.VK_RIGHT:
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
