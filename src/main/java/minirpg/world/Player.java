package minirpg.world;

public class Player extends Entity {

    public Player (int x, int y) {
        super(x, y, Tile.PLAYER);
    }

    public void setX (int newX) { x = newX; }
    public void setY (int newY) { y = newY; }
}