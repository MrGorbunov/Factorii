package minirpg;

import minirpg.inventory.ItemIndex;
import minirpg.world.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {
    private int screenWidth;
    private int screenHeight;

    public WorldScreen () {
        screenWidth = 80;
        screenHeight = 24;
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_C:
                return new CraftScreen();
        
            default:
                GameState.world.handleNewInput(key.getKeyCode());
        }

        return this;
    } 





    //
    //   Display
    //

    public void displayOutput (AsciiPanel terminal) {
        drawWorld(terminal, -5);
        drawGUI(terminal);
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

    // TODO: Right now, drawGUI assumes that there are 5 lines to work with
    private void drawGUI (AsciiPanel terminal) {
        terminal.write("Wood: " + GameState.inventory.getQuantity(ItemIndex.WOOD), 13, screenHeight - 4);
        terminal.write("Coal: " + GameState.inventory.getQuantity(ItemIndex.ORE_COAL), 13, screenHeight - 3);
    }

}