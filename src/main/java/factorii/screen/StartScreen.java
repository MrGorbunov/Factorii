package factorii.screen;

import asciiPanel.AsciiPanel;
import factorii.Controls;
import factorii.GameState;
import factorii.PressState;

public class StartScreen implements Screen {
    
    public void displayOutput (AsciiPanel terminal) {
        terminal.write("Mini RPG", 1, 1);
        terminal.write("[Press Spaces to Begin]", 22, 10);

    }

    public Screen update () {
        if (GameState.inputBuffer.pressState(Controls.ACTION) == PressState.JUST_PRESSED) 
            return new WorldScreen();
        else
            return this;
    }

}