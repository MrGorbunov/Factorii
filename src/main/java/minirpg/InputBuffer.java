package minirpg;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
                        Example Usage
                     -------------------

if        (buffer.pressState(Controls.INTERACT) == PressState.PRESSED) {

} else if (buffer.pressState(Controls.UI_CYCLE) == PressState.PRESSED) {

} else if (buffer.pressState(Controls.ACTION) == PressState.PRESSED) {

} else if (buffer.pressState(Controls.DIR_UP) == PressState.HELD) {

}
*/

public class InputBuffer implements KeyListener {
    
    // TODO: Create inputX() and inputY()

    // Like with inventory, the enum's ordinal = the array index
    PressState[] buffer;

    public InputBuffer () {
        buffer = new PressState[Controls.values().length];

        for (int i=0; i<buffer.length; i++) {
            buffer[i] = PressState.UP;
        }
    }

    public PressState pressState (Controls control) {
        return buffer[control.ordinal()];
    }

    /**
     * IMPORTANT: This should be one of the last things 
     * to update. If it is updated first, then all key-inputs
     * will be treated as held & up (sustained presses) and
     * the one-frame input-states will never be seen.
     * 
     * Will set Pressed to Held and Released to Up
     */
    public void update () {
        for (int i=0; i<buffer.length; i++) {
            if (buffer[i] == PressState.PRESSED) {
                buffer[i] = PressState.HELD;

            } else if (buffer[i] == PressState.RELEASED) {
                buffer[i] = PressState.UP;

            }
        }
    }

    public void keyPressed (KeyEvent e) { 
        for (Controls control : Controls.values()) {
            if (control.getKeyCode() == e.getKeyCode()) {
                if (buffer[control.ordinal()] == PressState.HELD)
                    continue;

                buffer[control.ordinal()] = PressState.PRESSED;
                return;
            }
        }
    }

    public void keyReleased (KeyEvent e) { 
        for (Controls control : Controls.values()) {
            if (control.getKeyCode() == e.getKeyCode()) {
                buffer[control.ordinal()] = PressState.RELEASED;
                return;
            }
        }
    }
    
    // We don't need you
    public void keyTyped (KeyEvent e) { }


}