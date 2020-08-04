package minirpg.screen;

import minirpg.GameState;
import minirpg.inventory.ItemIndex;
import minirpg.subscreen.InventoryGridSubscreen;
import minirpg.subscreen.WorldSubscreen;
import minirpg.world.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {
    private int screenWidth;
    private int screenHeight;

    private WorldSubscreen worldSubscreen;
    private InventoryGridSubscreen inventoryGridSubscreen;

    private boolean selectionMode;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;

        worldSubscreen = new WorldSubscreen(screenWidth, screenHeight - 5);
        inventoryGridSubscreen = new InventoryGridSubscreen(screenWidth, 5, 0, screenHeight - 5);

        inventoryGridSubscreen.setActive(false);
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new CraftScreen();

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


        }

        return this;
    } 





    //
    // Selection Mode
    //

    private void toggleSelectionMode () {
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

    private void upInput () {
        if (selectionMode) 
            inventoryGridSubscreen.moveUp();
        else
            GameState.world.handleNewInput(KeyEvent.VK_UP);
    }

    private void downInput () {
        if (selectionMode) 
            inventoryGridSubscreen.moveDown();
        else
            GameState.world.handleNewInput(KeyEvent.VK_DOWN);
    }

    private void leftInput () {
        if (selectionMode) 
            inventoryGridSubscreen.moveLeft();
        else
            GameState.world.handleNewInput(KeyEvent.VK_LEFT);
    }

    private void rightInput () {
        if (selectionMode) 
            inventoryGridSubscreen.moveRight();
        else
            GameState.world.handleNewInput(KeyEvent.VK_RIGHT);
    }






    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        worldSubscreen.drawSubscreen(terminal);
        inventoryGridSubscreen.drawSubscreen(terminal);
    }

}