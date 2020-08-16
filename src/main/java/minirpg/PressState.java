package minirpg;

/*
Pressed  = just pressed down (only for 1 frame)
Held     = held down 
Released = just released (only for 1 frame)
Up       = not down
*/

public enum PressState {
    PRESSED, HELD, RELEASED, UP
}