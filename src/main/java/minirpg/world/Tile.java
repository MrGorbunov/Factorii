package minirpg.world;

import asciiPanel.AsciiPanel;

// TODO: Add Z-sorting (every tile holds a z-value)

public enum Tile {
    EMPTY, // EMPTY should always be first so it's defaulted to
    BOUNDS,

    DIRT,
    WATER, 

    TREE,
    STONE,
    SAND,

    ORE_COAL,
    ORE_COPPER,
    ORE_IRON,

    PLAYER
}