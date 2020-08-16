package minirpg.screen;

import asciiPanel.AsciiPanel;
import minirpg.Controls;
import minirpg.GameState;
import minirpg.InputBuffer;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class InputTestScreen implements Screen {
    
    private int screenWidth = 79;
    private int screenHeight = 24;

    public InputTestScreen () { }

    public void displayOutput (AsciiPanel terminal) {
        int xCord = 2;
        int yCord = 2;

        InputBuffer buffer = GameState.inputBuffer;

            // if        (buffer.pressState(Controls.INTERACT) == PressState.PRESSED) {

            // } else if (buffer.pressState(Controls.UI_CYCLE) == PressState.PRESSED) {

            // } else if (buffer.pressState(KeyEvent.VK_C) == PressState.PRESSED) {

            // } else if (buffer.pressState(Controls.DIR_UP) == PressState.HELD) {

            // }
        terminal.write("Action: " + buffer.pressState(Controls.ACTION), xCord, yCord);
        yCord++;

        terminal.write("Interact: " + buffer.pressState(Controls.INTERACT), xCord, yCord);
        yCord++;

        terminal.write("UI Cycle: " + buffer.pressState(Controls.UI_CYCLE), xCord, yCord);
        yCord++;
        yCord++;

        terminal.write("Dir Up: " + buffer.pressState(Controls.DIR_UP), xCord, yCord);
        yCord++;

        terminal.write("Dir Down: " + buffer.pressState(Controls.DIR_DOWN), xCord, yCord);
        yCord++;

        terminal.write("Dir Left: " + buffer.pressState(Controls.DIR_LEFT), xCord, yCord);
        yCord++;

        terminal.write("Dir Right: " + buffer.pressState(Controls.DIR_RIGHT), xCord, yCord);
        yCord++;
    }

    public Screen handleInput (KeyEvent key) {
        return this;
    }
}