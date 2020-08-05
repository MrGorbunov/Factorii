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

    private boolean selectionMode;
    private boolean placementMode;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;

        worldSubscreen = new WorldSubscreen(screenWidth, screenHeight - 5);
        worldPlacementSubscreen = new WorldPlacementSubscreen(screenWidth, screenHeight - 5);
        inventoryGridSubscreen = new InventoryGridSubscreen(screenWidth, 5, 0, screenHeight - 5);

        inventoryGridSubscreen.setActive(false);
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return changeScreen();

            case KeyEvent.VK_Z:
                toggleSelectionMode();
                break;

            case KeyEvent.VK_UP:
                upInput();
                break;

            case KeyEvent.VK_DOWN:
                downInput();
                break;

            case KeyEvent.VK_LEFT:
                leftInput();
                break;

            case KeyEvent.VK_RIGHT:
                rightInput();
                break;

			case KeyEvent.VK_SPACE:
				spaceInput();
				break;


        }

        return this;
    } 





    //
    // Input handling
    //

    private Screen changeScreen () {
        if (placementMode) {
            placementMode = false;
            selectionMode = false;
            return this;
        }

        return new CraftScreen();
    }

    private void toggleSelectionMode () {
        if (placementMode) {
            placementMode = false;
            selectionMode = true;
            inventoryGridSubscreen.setActive(true);
            return;
        }

        if (selectionMode == true) {
            inventoryGridSubscreen.setActive(false);
            // worldSubscreen.setActive(true);
            selectionMode = false;

        } else {
            inventoryGridSubscreen.setActive(true);
            // worldSubscreen.setActive(true);
            selectionMode = true;
        }
    }

	private void spaceInput () {
        if (placementMode) {
            boolean placedSuccesfully = worldPlacementSubscreen.placeItem();
            System.out.println(placedSuccesfully);
            if (placedSuccesfully) {
                GameState.inventory.removeItem(inventoryGridSubscreen.getSelectedItem());
                inventoryGridSubscreen.refresh();
                placementMode = false;
                selectionMode = false;
            }


            return;
        }

        if (selectionMode) {
            ItemIndex selectedItem = inventoryGridSubscreen.getSelectedItem();
            Tile activeTile = ItemIndex.itemToTile(selectedItem);
            worldPlacementSubscreen.setActiveTile(activeTile);
            worldPlacementSubscreen.refresh();

            inventoryGridSubscreen.setActive(false);
            placementMode = true;

        } else {
            GameState.world.harvestAdjacent();
        }
	}

    private void upInput () {
        if (placementMode)
            worldPlacementSubscreen.moveUp();
        else if (selectionMode) 
            inventoryGridSubscreen.moveUp();
        else
            GameState.world.moveUp();
    }

    private void downInput () {
        if (placementMode)
            worldPlacementSubscreen.moveDown();
        else if (selectionMode) 
            inventoryGridSubscreen.moveDown();
        else
			GameState.world.moveDown();
    }

    private void leftInput () {
        if (placementMode)
            worldPlacementSubscreen.moveLeft();
        else if (selectionMode) 
            inventoryGridSubscreen.moveLeft();
        else
			GameState.world.moveLeft();
    }

    private void rightInput () {
        if (placementMode)
            worldPlacementSubscreen.moveRight();
        else if (selectionMode) 
            inventoryGridSubscreen.moveRight();
        else
			GameState.world.moveRight();
    }






    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        if (placementMode)
            worldPlacementSubscreen.drawSubscreen(terminal);
        else
            worldSubscreen.drawSubscreen(terminal);
        
        inventoryGridSubscreen.drawSubscreen(terminal);
    }

}
