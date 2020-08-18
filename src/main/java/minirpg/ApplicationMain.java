package minirpg;

import java.awt.Color;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import minirpg.inventory.ItemIndex;
import minirpg.screen.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
This handles the main game play loop.

(a/o now) there are no animations or effects that require 
fast (24+ fps) updates. So visual & 'physics' updates happen
at the same time.

Additionally, the game as is is 30 fps, but movement is closer
to 10 fps. This is because the response time is really important.
*/


public class ApplicationMain extends JFrame {
    private static final long serialVersionUID = -5955110992780994159L;

    private Screen screen;
    private AsciiPanel terminal;
    private InputBuffer inputBuffer;

    public ApplicationMain () {
        super();
        terminal = new AsciiPanel();
        add(terminal);
        pack();

        // screen = new StartScreen();
        screen = new StartScreen();
        inputBuffer = new InputBuffer();

        GameState.initGameState(200, 170);
        GameState.makeNewWorld();
        GameState.setInputBuffer(inputBuffer);

        addKeyListener(inputBuffer);
        repaint();
    }

    public void mainLoop () {
        final int FPS = 30;
        final long frameTimeMillis = 1000 / FPS;
        long previousTime;

        while (true) {
            previousTime = System.currentTimeMillis();

            // Update
            screen = screen.update();

            // Draw
            terminal.clear();
            screen.displayOutput(terminal);
            super.repaint();

            // Updating input buffer happens at end
            inputBuffer.update();

            // Wait
            long currentTime = System.currentTimeMillis();
            long executionTime = currentTime - previousTime;
            try {
                Thread.sleep(frameTimeMillis - executionTime);
            } catch (InterruptedException e) { }
              catch (IllegalArgumentException e) { System.out.println("WARNING: Frame took longer than frameTimeMillis; overloaded"); }
        }
    }

    public static void main(String[] args) {
        ApplicationMain main = new ApplicationMain();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);

        main.mainLoop();
    }

}
