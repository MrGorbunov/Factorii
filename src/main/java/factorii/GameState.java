package factorii;

import factorii.factory.Factory;
import factorii.inventory.CraftingGlobals;
import factorii.inventory.Inventory;
import factorii.inventory.ItemIndex;
import factorii.world.*;

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

    // TODO: Get these guys out of here
    private static WorldBuilder worldBuilder;
    public static InputBuffer inputBuffer;

    public static int windowHeight;
    public static int windowWidth;

    public static void setWindowDimensions (int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public static void initGameState (int worldWidth, int worldHeight) {
        worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        factory = new Factory(worldWidth, worldHeight);
        player = new Player();

        techLevel = TechLevel.START;
        craftingGlobals = new CraftingGlobals();

        makeNewWorld();

        // For easier testing
        player.getInventory().addItemMulti(ItemIndex.WOOD, 100);
        player.getInventory().addItemMulti(ItemIndex.STONE, 100);
        player.getInventory().addItemMulti(ItemIndex.SAND, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COAL, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_IRON, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_IRON, 100);
    }

    public static void setInputBuffer (InputBuffer inputBuffer) {
        GameState.inputBuffer = inputBuffer;
    }

    public static void makeNewWorld () {
        world = worldBuilder.generatePocketDirtWorld();
    }

}