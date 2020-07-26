package minirpg;

import java.awt.event.KeyEvent;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class WinScreen implements Screen {
    
    public void displayOutput (AsciiPanel terminal) {
        terminal.write("Bruh you won", 1, 1);
        terminal.write("[Press Enter to Restart]", 22, 10);
    }

    public Screen handleInput (KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER)
            return new PlayScreen();
        else
            return this;
    }

}