package factorii.factory;

// NOTE: Order kinda matters. The ordinals dictate search order for 
//       the item tubes, meaning first they'll check UP (ordinal 0),
//       then LEFT (ordinal 1), and so on.

public enum TubeDirection {
    UP    (new int[] {0, -1}), 
    LEFT  (new int[] {-1, 0}), 
    DOWN  (new int[] {0,  1}),
    RIGHT (new int[] {1,  0});

    private final int[] coordinateOffset;

    private TubeDirection (int[] cordOffset) {
        coordinateOffset = cordOffset;
    }

    public int[] getCordinateOffset () { return coordinateOffset; }

    public TubeDirection getOppositeDirection () { 
        switch (this) {
            case UP:    return DOWN;
            case DOWN:  return UP;
            case LEFT:  return RIGHT;
            case RIGHT: return LEFT;
        }

        throw new Error("Bruh moment: tubeDirection.oppositeDirection() failed");
    }

    public int toIndex () {
        return this.ordinal();
    }

    /**
     * Returns a tube direction following this mapping:
     * 0 - UP
     * 1 - RIGHT
     * 2 - DOWN
     * 3 - LEFT
     * 
     * @throws IllegalArgumentException if index is not in [0,3]
     */
    public static TubeDirection getFromIndex (int index) {
        switch (index) {
            case 0: return UP;
            case 1: return LEFT;
            case 2: return DOWN;
            case 3: return RIGHT;
        }

        throw new IllegalArgumentException();
    }
}