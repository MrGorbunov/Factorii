package minirpg;

import minirpg.world.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class PlayScreen implements Screen {
    private World world;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen () {
        screenWidth = 80;
        screenHeight = 24;

        world = new WorldBuilder(screenWidth + 30, screenHeight + 30)
                    .generateDefaultWorld();
        
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                return new LoseScreen();

            case KeyEvent.VK_ENTER:
                return new WinScreen();
        
            default:
                world.handleNewInput(key.getKeyCode());
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
        Glyph[][] worldSlice = world.getWorldSlice(screenWidth, screenHeight + heightOffset, 0, yOff);

        for (int x=0; x<screenWidth; x++) {
            for (int y=0; y<screenHeight+heightOffset; y++) {
                Glyph glyph = worldSlice[x][y];
                terminal.write(glyph.getChar(), x, y, glyph.getColor());
            }
        }
    }

    // TODO: Right now, drawGUI assumes that there are 5 lines to work with
    private void drawGUI (AsciiPanel terminal) {
        terminal.write("Wood: " + world.getWood(), 13, screenHeight - 4);
        terminal.write("Coal: " + world.getCoal(), 13, screenHeight - 3);
    }

}