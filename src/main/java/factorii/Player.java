package factorii;

import factorii.inventory.Inventory;

public class Player {

    private int updateTicks;
    private final int TICKS_PER_MOVE;

    private int xCord;
    private int yCord;
    private Inventory playerInventory;

    public int getX () { return xCord; }
    public int getY () { return yCord; }
    public Inventory getInventory () { return playerInventory; }

    public Player () {
        TICKS_PER_MOVE = 1; // Basically speed, but lower = faster
        updateTicks = 0;

        xCord = 0;
        yCord = 0;
        playerInventory = new Inventory();
    }

    /**
     * Used to set the players position. 
     * 
     * Is called "setSpawn" because it should ONLY be used
     * at startup to move the player. Otherwise, player movement
     * should be handled by the .update() method.
     */
    public void setSpawn (int x, int y) {
        xCord = x;
        yCord = y;
    }

    public void update () {
        if (updateTicks < TICKS_PER_MOVE) {
            updateTicks++;
            return;
        }

        InputBuffer inputBuffer = GameState.inputBuffer;

        if (inputBuffer.xInput() != 0 || inputBuffer.yInput() != 0) {
            int newX = xCord + inputBuffer.xInput();
            int newY = yCord + inputBuffer.yInput();

            if (GameState.world.canStandAt(newX, yCord))
                xCord = newX;
            if (GameState.world.canStandAt(xCord, newY))
                yCord = newY;

            updateTicks = 0;
        }

    }



}