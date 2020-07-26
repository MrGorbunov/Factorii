package minirpg;

import minirpg.world.*;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class PlayScreen implements Screen {
    private World world;
    private int viewX;
    private int viewY;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen () {
        screenWidth = 80;
        screenHeight = 24;

        world = new WorldBuilder(screenWidth + 30, screenHeight + 30)
                    .generateCaveWorld();
        
        viewX = 0;
        viewY = 0;
    }

    public Screen handleInput (KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                return new LoseScreen();

            case KeyEvent.VK_ENTER:
                return new WinScreen();

            case KeyEvent.VK_RIGHT:
                setViewX(viewX + 1);
                break;

            case KeyEvent.VK_LEFT:
                setViewX(viewX - 1);
                break;
            
            case KeyEvent.VK_DOWN:
                setViewY(viewY + 1);
                break;
            
            case KeyEvent.VK_UP:
                setViewY(viewY - 1);
                break;
            
        }
        return this;
    }



    //
    //   Display
    //

    private void setViewX (int newViewX) {
        int maxX = world.width() - screenWidth;

        if (newViewX < 0)
            viewX = 0;
        else if (newViewX > maxX)
            viewX = maxX;
        else
            viewX = newViewX;
    }

    private void setViewY (int newViewY) {
        int maxY = world.height() - screenHeight;

        if (newViewY < 0)
            viewY = 0;
        else if (newViewY > maxY)
            viewY = maxY;
        else
            viewY = newViewY;
    }

    public void displayOutput (AsciiPanel terminal) {
        displayTiles(terminal);

        terminal.write("You are having so much fun!", 1, 1);
        terminal.write("[Press Esc to lose]  [Press enter to win]", 22, 10);
    }

    private void displayTiles (AsciiPanel terminal) {
        for (int x=0; x<screenWidth; x++) {
            for (int y=0; y<screenHeight; y++) {
                int worldX = x + viewX;
                int worldY = y + viewY;
                
                terminal.write(world.glyph(worldX, worldY), x, y, world.color(worldX, worldY));
            }
        }
    }

}