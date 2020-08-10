package minirpg.screen;

import minirpg.GameState;
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

    private ScreenState state;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;

        worldSubscreen = new WorldSubscreen(screenWidth, screenHeight - 5);
        worldPlacementSubscreen = new WorldPlacementSubscreen(screenWidth, screenHeight - 5);
        inventoryGridSubscreen = new InventoryGridSubscreen(screenWidth, 5, 0, screenHeight - 5);

        inventoryGridSubscreen.setActive(false);

        state = ScreenState.TRAVERSAL;
    }





    //
    // Input handling
    //

    public Screen handleInput (KeyEvent key) {
        switch (state) {
            case TRAVERSAL:
                return traversalInput(key);

            case SELECTION:
                return selectionInput(key);

            case PLACEMENT:
                return placementInput(key);
        }

        return this;
    } 

    private Screen traversalInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            // Goto crafting
            case KeyEvent.VK_C:
                return new CraftScreen();
            
            // Goto selection
            case KeyEvent.VK_Z:
                boolean canTransition = inventoryGridSubscreen.setActive(true);
                if (canTransition)
                    state = ScreenState.SELECTION;
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
                state = ScreenState.TRAVERSAL;
                inventoryGridSubscreen.setActive(false);
                break;
            
            // Go into placement mode
            case KeyEvent.VK_SPACE:
                state = ScreenState.PLACEMENT;

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
                state = ScreenState.TRAVERSAL;
                break;

            // Goto selection mode
            case KeyEvent.VK_Z:
                boolean canGotoInventorygrid = inventoryGridSubscreen.setActive(true);

                if (canGotoInventorygrid)
                    state = ScreenState.SELECTION;
                else
                    state = ScreenState.TRAVERSAL;
                break;
            
            // Place down object
            case KeyEvent.VK_SPACE:
                boolean placedSuccesfully = worldPlacementSubscreen.placeItem();
                if (placedSuccesfully) {
                    GameState.player.getInventory().removeItem(inventoryGridSubscreen.getSelectedItem());
                    inventoryGridSubscreen.refresh();
                    state = ScreenState.TRAVERSAL;
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
        if (state == ScreenState.PLACEMENT)
            worldPlacementSubscreen.drawSubscreen(terminal);
        else {
            GameState.world.refresh();
            worldSubscreen.drawSubscreen(terminal);
        }
        
        inventoryGridSubscreen.drawSubscreen(terminal);
    }

}

enum ScreenState {
    TRAVERSAL, // When player is moving aroudn
    SELECTION, // When player is selecting something to place
    PLACEMENT  // When player is placing down something
}