package factorii.screen;

import asciiPanel.AsciiPanel;
import factorii.Controls;
import factorii.GameState;
import factorii.InputBuffer;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class InputTestScreen implements Screen {
    
    public InputTestScreen () { }

    public void displayOutput (AsciiPanel terminal) {
        InputBuffer buffer = GameState.inputBuffer;
        int xCord = 2;
        int yCord = 2;

        terminal.write("Action: " + buffer.pressState(Controls.ACTION), xCord, yCord);
        yCord++;

        terminal.write("Interact: " + buffer.pressState(Controls.OPEN_SCREEN), xCord, yCord);
        yCord++;

        terminal.write("UI Cycle: " + buffer.pressState(Controls.SWITCH_SUBSCREEN), xCord, yCord);
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
        yCord++;

        terminal.write("Modifier: " + buffer.pressState(Controls.MODIFIER), xCord, yCord);
    }

    public Screen update () {
        return this;
    }

    public Screen handleInput (KeyEvent key) {
        return this;
    }
}