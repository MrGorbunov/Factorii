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

        // Testing tube direction
        for (int x=5; x<=10; x++) {
            for (int y=5; y<=10; y++) {
                world.placeCraftedTile(Tile.ITEM_TUBE_STONE, x, y);
            }
        }

        world.placeCraftedTile(Tile.CHEST, 7, 3);
        world.placeCraftedTile(Tile.ITEM_TUBE_GLASS, 7, 4);
        world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 7, 11);
        world.placeCraftedTile(Tile.CHEST, 7, 12);

        world.placeCraftedTile(Tile.CHEST, 3, 7);
        world.placeCraftedTile(Tile.ITEM_TUBE_GLASS, 4, 7);
        world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 11, 7);
        world.placeCraftedTile(Tile.CHEST, 12, 7);
    }

    public static void setInputBuffer (InputBuffer inputBuffer) {
        GameState.inputBuffer = inputBuffer;
    }

    public static void makeNewWorld () {
        world = worldBuilder.generateDirtWorld();
    }

}