package minirpg;

import minirpg.inventory.Inventory;

public class Player {

    private int updateTicks;
    private final int TICKS_PER_MOVE;

    private int xCord;
    private int yCord;
    private Inventory playerInventory;

    public int getX () { return xCord; }
    public int getY () { return yCord; }
    public Inventory getInventory () { return playerInventory; }

    public Player (int x, int y) {
        TICKS_PER_MOVE = 3; // Basically speed, but lower = faster
        updateTicks = 0;

        xCord = x;
        yCord = y;
        playerInventory = new Inventory();
    }

    public Player () {
        this(0, 0);
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