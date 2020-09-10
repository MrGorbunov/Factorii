package factorii;

import factorii.factory.Factory;
import factorii.factory.FactoryItemTubeSteel;
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

    public static InputBuffer inputBuffer;

    public static int windowHeight;
    public static int windowWidth;

    public static void setWindowDimensions (int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public static void initNormalGame (int worldWidth, int worldHeight) {
        WorldBuilder worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        factory = new Factory(worldWidth, worldHeight);
        player = new Player();
        world = worldBuilder.generatePocketWorld();

        techLevel = TechLevel.START;
        craftingGlobals = new CraftingGlobals();
    }

    /**
     * Like a normal game, but with resources to avoid collection
     */
    public static void initDevGame (int worldWidth, int worldHeight) {
        initNormalGame(worldWidth, worldHeight);

        player.getInventory().addItemMulti(ItemIndex.WOOD, 100);
        player.getInventory().addItemMulti(ItemIndex.STONE, 100);
        player.getInventory().addItemMulti(ItemIndex.SAND, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COAL, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_IRON, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_IRON, 100);
    }

    /**
     * Blank world w/ lots of factory components.
     */
    public static void initBenchmarkGame (int worldWidth, int worldHeight) {
        WorldBuilder worldBuilder = new WorldBuilder(worldWidth, worldHeight);
        factory = new Factory(worldWidth, worldHeight);
        player = new Player();
        world = worldBuilder.generateDirtWorld();

        techLevel = TechLevel.START;
        craftingGlobals = new CraftingGlobals();

        player.getInventory().addItemMulti(ItemIndex.WOOD, 100);
        player.getInventory().addItemMulti(ItemIndex.STONE, 100);
        player.getInventory().addItemMulti(ItemIndex.SAND, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COAL, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.ORE_IRON, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_COPPER, 100);
        player.getInventory().addItemMulti(ItemIndex.BAR_IRON, 100);


        //
        // Create basic mining setup
        Tile[] resources = new Tile[] {Tile.STONE, Tile.ORE_COAL, Tile.SAND, Tile.ORE_COPPER, Tile.ORE_IRON};
        int y;

        for (y=5; y<=9; y++) {
            world.setTile(resources[y-5], 5, y);
            world.placeCraftedTile(Tile.MINING_DRILL, 5, y);
            world.placeCraftedTile(Tile.AUTO_MINING_UPGRADE, 5, y);
        }

        for (y=5; y<=9; y++) {
            world.placeCraftedTile(Tile.ITEM_TUBE_GLASS, 6, y);
            world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 7, y);
        }


        //
        // Transport it through tube & sort it again
        for (int x=8; x<13; x++) {
            world.placeCraftedTile(Tile.ITEM_TUBE_STONE, x, 7);
        }

        for (y=5; y<=9; y++) {
            world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 13, y);
            world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 14, y);
            world.placeCraftedTile(Tile.ITEM_TUBE_STONE, 15, y);
            world.placeCraftedTile(Tile.ITEM_TUBE_STEEL, 16, y);
            ((FactoryItemTubeSteel) factory.getFacDataAt(16, y)).setFilterMode(true);
            ((FactoryItemTubeSteel) factory.getFacDataAt(16, y)).getFilterInventory().addItem(Tile.tileToItem(resources[y-5]));
            world.placeCraftedTile(Tile.CHEST, 17, y);
        }
    }

    public static void setInputBuffer (InputBuffer inputBuffer) {
        GameState.inputBuffer = inputBuffer;
    }

}