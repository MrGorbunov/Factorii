package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.*;
import minirpg.inventory.*;

// TODO: Extend a common interface w/ WorldSubscreen
public class PlayerInventorySubscreen {
    
    private int width;
    private int height;
    private int xOff;
    private int yOff;
    private boolean active;

    private int pad;
    private int scrollPosition;
    private int totalLines;
    private ArrayList<String> resources;
    private ArrayList<String> equipment;
    private ArrayList<String> factoryComponents;

    
    public PlayerInventorySubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;
        this.xOff = xOff;
        this.yOff = yOff;

        pad = 2;
        scrollPosition = 0;
        totalLines = 0; // Gets reset once drawn
        updateLists();
    }

    public PlayerInventorySubscreen (int width, int height) {
        this(width, height, 0, 0);
    }





    //
    // Scrolling logic
    //

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
        if (scrollPosition >= totalLines - height + 2*pad) { return; }

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

        //
        // Generate a list for text & one for colors

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

        if (equipment.size() > 0) {
            inventoryText.add("Equipment");
            inventoryColor.add(Color.CYAN);
            for (String equip : equipment) {
                inventoryText.add(equip);
                inventoryColor.add(Color.WHITE);
            }
            inventoryText.add("");
            inventoryColor.add(Color.WHITE);
        }

        if (factoryComponents.size() > 0) {
            inventoryText.add("Factory Components");
            inventoryColor.add(Color.CYAN);
            for (String craftedItem : factoryComponents) {
                inventoryText.add(craftedItem);
                inventoryColor.add(Color.WHITE);
            }
        }

        totalLines = inventoryText.size();


        //
        // Now write the created list to the console

        int yCord = yOff + pad;
        int xCord = xOff + pad;
        int maxY = yOff + height - pad;

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
        updateEquipmentAndFactoryComponentsList();
    }

    private void updateResourceList () {
        Inventory inv = GameState.player.getInventory();
        resources = new ArrayList<String>();

        resources.add("Wood: " + inv.getQuantity(ItemIndex.WOOD));
        resources.add("Stone: " + inv.getQuantity(ItemIndex.STONE));
        
        if (GameState.techLevel.ordinal() >= TechLevel.WORKBENCH.ordinal()) {
            resources.add("");
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

    private void updateEquipmentAndFactoryComponentsList () {
        equipment = new ArrayList<String> ();
        factoryComponents = new ArrayList<String> ();
        Inventory inv = GameState.player.getInventory();

        for (ItemIndex possibleItem : ItemIndex.values()) {
            if (possibleItem.isResource() ||
                inv.getQuantity(possibleItem) == 0)
                    continue;

            if (possibleItem.isPlacable())
                factoryComponents.add(possibleItem.toString() + " x" + inv.getQuantity(possibleItem));
            else if (possibleItem.isEquipable())
                equipment.add(possibleItem.toString());
        }
    }
}