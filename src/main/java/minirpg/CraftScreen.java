package minirpg;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class CraftScreen implements Screen {
    
    public Screen handleInput (KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_C)
            return new WorldScreen();

        return this;
    }





    //
    //    Display
    //

    public void displayOutput (AsciiPanel terminal) {
        displayInventory(terminal);
        displayCraftingOptions(terminal);
    }

    private void displayInventory (AsciiPanel terminal) {
        
        // Do stuff
    }

    private void displayCraftingOptions (AsciiPanel terminal) {
        // Do more stuff
    }


}