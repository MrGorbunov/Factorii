package factorii;

/*
Pressed  = just pressed down (only for 1 frame)
Held     = held down 
Released = just released (only for 1 frame)
Up       = not down
*/

public enum PressState {
    JUST_PRESSED, PRESSED, JUST_RELEASED, RELEASED;

    public boolean isDown () {
        return this == PRESSED ||
               this == JUST_PRESSED;
    }

    public boolean isUp () {
        return this == RELEASED ||
               this == JUST_RELEASED;
    }
}