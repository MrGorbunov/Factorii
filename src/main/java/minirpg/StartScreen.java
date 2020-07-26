package minirpg;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class StartScreen implements Screen {
    
    public void displayOutput (AsciiPanel terminal) {
        terminal.write("Mini RPG", 1, 1);
        terminal.write("[Press Enter to Begin]", 22, 10);
    }

    public Screen handleInput (KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER)
            return new PlayScreen();
        else
            return this;
    }

}