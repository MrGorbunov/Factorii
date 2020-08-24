package factorii;

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
    
    // Like with inventory, the enum's ordinal = the array index
    PressState[] buffer;

    public InputBuffer () {
        buffer = new PressState[Controls.values().length];

        for (int i=0; i<buffer.length; i++) {
            buffer[i] = PressState.RELEASED;
        }
    }



    //
    // Getting info
    //

    public PressState pressState (Controls control) {
        return buffer[control.ordinal()];
    }

    /**
     * Returns an int, either -1, 0, or 1
     * -1 = left
     * 0 = neutral
     * 1 = right
     */
    public int xInput () {
        int input = 0;
        if (buffer[Controls.DIR_LEFT.ordinal()] == PressState.JUST_PRESSED ||
            buffer[Controls.DIR_LEFT.ordinal()] == PressState.PRESSED)
                input--;

        if (buffer[Controls.DIR_RIGHT.ordinal()] == PressState.JUST_PRESSED ||
            buffer[Controls.DIR_RIGHT.ordinal()] == PressState.PRESSED)
                input++;

        return input;
    }

    /**
     * Returns an int, either -1, 0, or 1
     * -1 = up
     * 0 = neutral
     * 1 = down
     */
    public int yInput () {
        int input = 0;
        if (buffer[Controls.DIR_UP.ordinal()] == PressState.JUST_PRESSED ||
            buffer[Controls.DIR_UP.ordinal()] == PressState.PRESSED)
                input--;

        if (buffer[Controls.DIR_DOWN.ordinal()] == PressState.JUST_PRESSED ||
            buffer[Controls.DIR_DOWN.ordinal()] == PressState.PRESSED)
                input++;

        return input;
    }




    //
    // Buffer logic
    //

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
            if (buffer[i] == PressState.JUST_PRESSED) {
                buffer[i] = PressState.PRESSED;

            } else if (buffer[i] == PressState.JUST_RELEASED) {
                buffer[i] = PressState.RELEASED;

            }
        }
    }

    public void keyPressed (KeyEvent e) { 
        for (Controls control : Controls.values()) {
            if (control.getKeyCode() == e.getKeyCode()) {
                if (buffer[control.ordinal()] == PressState.PRESSED)
                    continue;

                buffer[control.ordinal()] = PressState.JUST_PRESSED;
                return;
            }
        }
    }

    public void keyReleased (KeyEvent e) { 
        for (Controls control : Controls.values()) {
            if (control.getKeyCode() == e.getKeyCode()) {
                buffer[control.ordinal()] = PressState.JUST_RELEASED;
                return;
            }
        }
    }
    
    // We don't need you
    public void keyTyped (KeyEvent e) { }


}