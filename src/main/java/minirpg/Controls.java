package minirpg;

import java.awt.event.KeyEvent;

/*
Controls Breakdown
---
Movement
- One keycode for all 4 dirs
- Also used for scrolling & grid select

UI Cycle
- Used to cycle between subscreens

Action
- Harvest resources
- Craft items
- Transfer to chest

Interact
- Opens up menus
- Goes to crafting screens

*/

// TODO: Start using this you bumbo

public class Controls {
    public static final int DIR_UP = KeyEvent.VK_UP;
    public static final int DIR_DOWN = KeyEvent.VK_DOWN;
    public static final int DIR_LEFT = KeyEvent.VK_LEFT;
    public static final int DIR_RIGHT = KeyEvent.VK_RIGHT;
    
    public static final int UI_CYCLE = KeyEvent.VK_Z;
    public static final int ACTION = KeyEvent.VK_SPACE;
    public static final int INTERACT = KeyEvent.VK_C;

    // Private constructor ensures static use
    private Controls () {}
}