package factorii;

import java.awt.Color;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import factorii.inventory.ItemIndex;
import factorii.screen.*;

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

    public ApplicationMain () {
        super();

        int screenWidth = 120;
        int screenHeight = 40;
        GameState.setWindowDimensions(screenWidth, screenHeight);

        terminal = new AsciiPanel(screenWidth, screenHeight);
        add(terminal);
        pack();

        // screen = new StartScreen();
        screen = new StartScreen();
        InputBuffer inputBuffer = new InputBuffer();

        GameState.initGameState(200, 170);
        GameState.setInputBuffer(inputBuffer);

        addKeyListener(inputBuffer);
        repaint();
    }

    public void mainLoop () {
        final int FPS = 30;
        final long frameTimeMillis = 1000 / FPS;
        long previousTime;

        while (true) {
            // WARNING: Order is pretty important here
            //      be careful what you mess with
            previousTime = System.currentTimeMillis();

            screen = screen.update();

            // Draw
            terminal.clear();
            screen.displayOutput(terminal);
            super.repaint();

            GameState.inputBuffer.update();

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
        main.setResizable(false);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);

        main.mainLoop();
    }

}
