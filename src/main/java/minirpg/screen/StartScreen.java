package minirpg.screen;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import minirpg.Controls;

public class StartScreen implements Screen {
    
    public void displayOutput (AsciiPanel terminal) {
        terminal.write("Mini RPG", 1, 1);
        terminal.write("[Press Spaces to Begin]", 22, 10);

    }

    public Screen handleInput (KeyEvent key) {
        if (key.getKeyCode() == Controls.ACTION)
            return new WorldScreen();
        else
            return this;
    }

}