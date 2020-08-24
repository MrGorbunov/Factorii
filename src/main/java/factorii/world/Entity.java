package factorii.world;

public class Entity {
    protected int x;
    protected int y;
    private Tile tile;

    public Entity (int x, int y, Tile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Tile getTile() { return tile; }
}