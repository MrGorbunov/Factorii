package minirpg;

import minirpg.inventory.CraftingGlobals;
import minirpg.inventory.Inventory;
import minirpg.world.*;

/*
As the name implies, this bad boi stores gamestate.

All screens refer to it, and it is a singleton (everything's static)
*/

public class GameState {

    public static World world;
    public static Player player;

    public static CraftingGlobals craftingGlobals;
    public static TechLevel techLevel;

    private static WorldBuilder worldBuilder;


    public static void initGameState (int worldWidth, int worldHeight) {
        worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        player = new Player();

        techLevel = TechLevel.START;
        craftingGlobals = new CraftingGlobals();
    }

    public static void makeNewWorld () {
        world = worldBuilder.generateDefaultWorld();
    }

}