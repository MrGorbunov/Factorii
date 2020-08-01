package minirpg.world;

public class PlayerTile extends Entity {

    public PlayerTile (int x, int y) {
        super(x, y, Tile.PLAYER);
    }

    public void setX (int newX) { x = newX; }
    public void setY (int newY) { y = newY; }
}