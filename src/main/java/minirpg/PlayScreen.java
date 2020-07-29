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
        Glyph[][] slice = world.getWorldSlice(screenWidth, screenHeight);

        for (int x=0; x<screenWidth; x++) {
            for (int y=0; y<screenHeight; y++) {
                Glyph glyph = slice[x][y];
                terminal.write(glyph.getChar(), x, y, glyph.getColor());
            }
        }
    }

}