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

    // private final int FRAMES_PER_FPS_READOUT;
    // private int current_frames;
    // private long processing_since_last_readout;

    public ApplicationMain () {
        super();

        // For benchmarking
        // FRAMES_PER_FPS_READOUT = 50;
        // current_frames = 0;
        // processing_since_last_readout = 0;


        int screenWidth = 120;
        int screenHeight = 40;
        GameState.setWindowDimensions(screenWidth, screenHeight);

        terminal = new AsciiPanel(screenWidth, screenHeight);
        add(terminal);
        pack();

        // screen = new StartScreen();
        screen = new StartScreen();
        InputBuffer inputBuffer = new InputBuffer();

        GameState.initNormalGame(170, 140);
        GameState.setInputBuffer(inputBuffer);

        addKeyListener(inputBuffer);
        repaint();
    }

    public void mainLoop () {
        final int FPS = 30;
        final long frameTimeMillis = 1000 / FPS;
        long previousTime;

        while (true) {
            // Benchmarking
            // current_frames++;
            // if (current_frames >= FRAMES_PER_FPS_READOUT) {
            //     double processing_fps = (double) FRAMES_PER_FPS_READOUT / processing_since_last_readout * 1000;
            //     System.out.println(("Max FPS: " + processing_fps).substring(0, 14));

            //     current_frames = 0;
            //     processing_since_last_readout = 0;
            // }


            // WARNING: Order is pretty important here
            //      be careful what you mess with
            previousTime = System.currentTimeMillis();

            // Update
            screen = screen.update();
            GameState.inputBuffer.update();

            // Draw
            terminal.clear();
            screen.displayOutput(terminal);
            super.repaint();

            // Wait
            long currentTime = System.currentTimeMillis();
            long executionTime = currentTime - previousTime;

            // processing_since_last_readout += executionTime;

            try {
                Thread.sleep(frameTimeMillis - executionTime);
            } catch (InterruptedException e) { }
              catch (IllegalArgumentException e) { } // System.out.println("WARNING: Frame took longer than frameTimeMillis; overloaded"); }
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
