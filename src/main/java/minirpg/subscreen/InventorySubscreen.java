package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.*;
import minirpg.inventory.*;

// TODO: Extend a common interface w/ WorldSubscreen
public class InventorySubscreen {
    
    private int width;
    private int height;
    private int xOff;
    private int yOff;
    private boolean active;

    private int pad;
    private int scrollPosition;
    private ArrayList<String> resources;
    private ArrayList<String> craftedItems;

    
    public InventorySubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;
        this.xOff = xOff;
        this.yOff = yOff;

        pad = 2;
        scrollPosition = 0;
        updateLists();
    }

    public InventorySubscreen (int width, int height) {
        this(width, height, 0, 0);
    }





    //
    // Scrolling logic
    //

    private int totalLines () {
        // 2 lines for titles, 1 line for space
        return 3 + resources.size() + craftedItems.size();
    }

    public void scrollUp () {
        if (scrollPosition <= 0) { return; }

        scrollPosition--;
    }

    public void scrollDown () {
        /*
        totalLines = lines of text to display
        height = height of subscreen
        2*pad = white space at top & bottom

        height - 2*pad = how many lines of text can fit
        totalLines - (height - 2*pad) = how many excess lines don't fit & max scroll position
        */
        if (scrollPosition >= totalLines() - height + 2*pad) { return; }

        scrollPosition++;
    }





    //
    // Drawing Logic
    //

    public void setActive (boolean active) {
        this.active = active;
    }

    public void drawSubscreen (AsciiPanel terminal) {
        updateLists();

        // Setup the lists
        ArrayList<String> inventoryText = new ArrayList<String>();
        ArrayList<Color> inventoryColor = new ArrayList<Color>();
        inventoryText.add("Resources");
        inventoryColor.add(Color.CYAN);

        for (String resource : resources) {
            inventoryText.add(resource);
            inventoryColor.add(Color.WHITE);
        }

        inventoryText.add("");
        inventoryColor.add(Color.WHITE);

        inventoryText.add("Crafted Items");
        inventoryColor.add(Color.CYAN);

        for (String craftedItem : craftedItems) {
            inventoryText.add(craftedItem);
            inventoryColor.add(Color.WHITE);
        }



        int yCord = yOff + pad;
        int xCord = xOff + pad;
        int maxY = height - pad;

        if (scrollPosition != 0 && active) {
            terminal.write(upScrollArrows(), xCord, pad-1);
        }

        for (int i=scrollPosition; i<inventoryText.size(); i++) {
            terminal.write(inventoryText.get(i), xCord, yCord, inventoryColor.get(i));

            yCord++;
            if (yCord == maxY) {
                if (i+1<inventoryText.size() && active)
                    terminal.write(downScrollArrows(), xCord, maxY);
                return;

            } else if (yCord > maxY) {
                throw new Error("yCord exceeded the maximum; should not be possible");
            }
        }

    }

    private String downScrollArrows () {
        StringBuilder res = new StringBuilder();

        for (int i=0; i<width/3; i++) {
            res.append("\\/ ");
        }

        return res.toString();
    }

    private String upScrollArrows () {
        StringBuilder res = new StringBuilder();

        for (int i=0; i<width/3; i++) {
            res.append("/\\ ");
        }

        return res.toString();
    }





    //
    // Inventory Reading & Formatting
    //

    private void updateLists () {
        updateResourceList();
        updateCraftedList();
    }

    private void updateResourceList () {
        Inventory inv = GameState.inventory;
        resources = new ArrayList<String>();

        resources.add("Wood: " + inv.getQuantity(ItemIndex.WOOD));
        resources.add("Stone: " + inv.getQuantity(ItemIndex.STONE));
        resources.add("");
        
        if (GameState.techLevel.ordinal() >= TechLevel.WORKBENCH.ordinal()) {
            resources.add("Coal: " + inv.getQuantity(ItemIndex.ORE_COAL));
            resources.add("Copper Ore: " + inv.getQuantity(ItemIndex.ORE_COPPER));
            resources.add("Iron Ore: " + inv.getQuantity(ItemIndex.ORE_IRON));
        }

        if (GameState.techLevel.ordinal() >= TechLevel.KILN.ordinal()) {
            resources.add(4, "Sand: " + inv.getQuantity(ItemIndex.SAND));
            resources.add("");

            resources.add("Glass: " + inv.getQuantity(ItemIndex.GLASS));
            resources.add("Copper Bars: " + inv.getQuantity(ItemIndex.BAR_COPPER));
        }

        if (GameState.techLevel.ordinal() >= TechLevel.FORGE.ordinal()) {
            resources.add("Iron Bars: " + inv.getQuantity(ItemIndex.BAR_IRON));
            resources.add("Bronze: " + inv.getQuantity(ItemIndex.ALLOY_BRONZE));
            resources.add("Steel: " + inv.getQuantity(ItemIndex.ALLOY_STEEL));
        }
    }

    private void updateCraftedList () {
        ArrayList<ItemIndex> items = getCraftedItems();
        craftedItems = new ArrayList<String> ();
        Inventory inv = GameState.inventory;

        for (ItemIndex item : items) {
            craftedItems.add(item.toString() + " x" + inv.getQuantity(item));
        }
    }

    private ArrayList<ItemIndex> getCraftedItems () {
        ArrayList<ItemIndex> items = new ArrayList<ItemIndex> ();
        Inventory inv = GameState.inventory;

        for (ItemIndex in : ItemIndex.values()) {
            if (!in.isPlacable() ||
                inv.getQuantity(in) == 0)
                    continue;
            
            items.add(in);
        }

        return items;
    }

}