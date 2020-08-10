package minirpg;

import minirpg.inventory.Inventory;

public class Player {

    private int x;
    private int y;
    private Inventory playerInventory;

    public int getX () { return x; }
    public int getY () { return y; }
    public Inventory getInventory () { return playerInventory; }

    public Player (int x, int y) {
        this.x = x;
        this.y = y;
        playerInventory = new Inventory();
    }

    public Player () {
        this(0, 0);
    }



    //
    // Movement Logic
    //

    public void moveUp () {
		int newY = y - 1;
        if (GameState.world.canStandOn(x, newY) == false)
            return;
        y = newY;
    }

    public void moveDown () {
		int newY = y + 1;
        if (GameState.world.canStandOn(x, newY) == false)
            return;
        y = newY;
    }

    public void moveLeft () {
		int newX = x - 1;
        if (GameState.world.canStandOn(newX, y) == false)
            return;
        x = newX;
    }

    public void moveRight () {
		int newX = x + 1;
        if (GameState.world.canStandOn(newX, y) == false)
            return;
        x = newX;
    }

}