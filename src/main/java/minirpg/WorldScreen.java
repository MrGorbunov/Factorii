package minirpg;

import minirpg.inventory.InventoryRenderer;
import minirpg.inventory.ItemIndex;
import minirpg.world.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {
    private int screenWidth;
    private int screenHeight;
    // TODO: Very duplicate code between CraftScreen & here > maybe create SubScreen for individual parts
    private InventoryRenderer invRend;
    private String[] craftedItemStrings;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;

        invRend = new InventoryRenderer();
        updateList();
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new CraftScreen();

            case KeyEvent.VK_X:
                // Go to selection
                break;

            default:
                GameState.world.handleNewInput(key.getKeyCode());
        }

        return this;
    } 

    private void updateList () {
        ArrayList<ItemIndex> craftedItems = invRend.getCraftedItems();
        craftedItemStrings = new String[craftedItems.size()];

        for (int i=0; i<craftedItems.size(); i++) {
            ItemIndex item = craftedItems.get(i);
            craftedItemStrings[i] = item + " x" + GameState.inventory.getQuantity(item);
        }
    }





    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        drawWorld(terminal, -5);
        drawInventoryPane(terminal, 5);
    }

    private void drawWorld (AsciiPanel terminal, int heightOffset) {
        if (heightOffset > 0)
            throw new IllegalArgumentException("HeightOffset must be 0 or negative");

        int yOff = -(heightOffset + 2) / 3;
        Glyph[][] worldSlice = GameState.world.getWorldSlice(screenWidth, screenHeight + heightOffset, 0, yOff);

        for (int x=0; x<screenWidth; x++) {
            for (int y=0; y<screenHeight+heightOffset; y++) {
                Glyph glyph = worldSlice[x][y];
                terminal.write(glyph.getChar(), x, y, glyph.getColor());
            }
        }
    }

    // TODO: Right now, it's assumed that none of the names are too too long
    private void drawInventoryPane (AsciiPanel terminal, int lines) {
        int yCord = screenHeight - lines + 1;
        int xCord = 1;
        int colWidth = (screenWidth - 2) / 3;

        for (String s : craftedItemStrings) {
            terminal.write(s, xCord, yCord);
            
            yCord++;

            if (yCord >= screenHeight - 1) {
                yCord = screenHeight - lines + 1;
                xCord += colWidth;

                if (xCord >= screenWidth)
                    return;
            }
        }
    }

}