package minirpg;

import minirpg.inventory.Inventory;
import minirpg.world.World;
import minirpg.world.WorldBuilder;

/*
As the name implies, this bad boi stores gamestate.

All screens refer to it, and it is a singleton (everything's static)
*/

public class GameState {

    public static World world;
    public static Inventory inventory;

    public static TechLevel techLevel;

    private static WorldBuilder worldBuilder;


    public static void initGameState (int worldWidth, int worldHeight) {
        worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        inventory = new Inventory();

        techLevel = TechLevel.START;
    }

    public static void makeNewWorld () {
        world = worldBuilder.generateDefaultWorld();
    }

}