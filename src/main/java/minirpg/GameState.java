package minirpg;

import minirpg.factory.Factory;
import minirpg.inventory.CraftingGlobals;
import minirpg.inventory.Inventory;
import minirpg.inventory.ItemIndex;
import minirpg.world.*;

/*
As the name implies, this bad boi stores gamestate.

All screens refer to it, and it is a singleton (everything's static)
*/

public class GameState {

    public static World world;
    public static Factory factory;
    public static Player player;

    public static CraftingGlobals craftingGlobals;
    public static TechLevel techLevel;

    private static WorldBuilder worldBuilder;


    public static void initGameState (int worldWidth, int worldHeight) {
        worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        factory = new Factory(worldWidth, worldHeight);
        player = new Player();

        techLevel = TechLevel.START;
        craftingGlobals = new CraftingGlobals();

        // For easier testing
        player.getInventory().addItemMulti(ItemIndex.WOOD, 100);
        player.getInventory().addItemMulti(ItemIndex.STONE, 100);
        player.getInventory().addItemMulti(ItemIndex.SAND, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COAL, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_IRON, 100);
    }

    public static void makeNewWorld () {
        world = worldBuilder.generateDefaultWorld();
    }

}