package factorii.subscreen;

import java.util.ArrayList;

import factorii.inventory.*;

public class InventoryGridSubscreen extends GridSubscreen {
    
    private ArrayList<ItemIndex> displayItems;

    private Inventory inventory;
    private boolean hideResource;

    public InventoryGridSubscreen (int width, int height, int xOff, int yOff, Inventory inventory) {
        super(width, height, xOff, yOff);
        this.inventory = inventory;
        hideResource = true;

        updateLists();
    }

    public InventoryGridSubscreen (int width, int height, Inventory inventory) {
        this(width, height, 0, 0, inventory);
    }

    /**
     * Returns the selected item. If no item is selected (ex: 
     * empty inventory) it will return null.
     */
    public ItemIndex getSelectedItem () {
        int index = (scrollPos + yPos) * cols + xPos;
        if (index >= displayItems.size() || index < 0)
            return null;

        return displayItems.get(index);
    }

    public Inventory getInventory () {
        return inventory;
    }





    //
    // Configurability
    //

    public void setIgnoreResources (boolean ignoreResources) {
        hideResource = ignoreResources;
    }





    //
    // Logic
    //

    private void updateLists () {
        displayItems = new ArrayList<ItemIndex>();
        displayStrings = new ArrayList<String>();

        for (ItemIndex item : ItemIndex.values()) {
            if (inventory.getQuantity(item) == 0 ||
                (item.isResource() && hideResource) ||
                item.isEquipable())
                    continue;
            
            displayItems.add(item);
            int quantity = inventory.getQuantity(item);
            displayStrings.add(item.toString() + " x" + quantity);
        }

        maxScroll = (int) Math.ceil((double) displayStrings.size() / cols) - rows;
        maxScroll = Math.max(maxScroll, 0);

        // Avoids out-of bounds
        if (scrollPos > maxScroll)
            scrollPos = maxScroll;
        if (hoveringOverNothing()) {
            moveLeft();
            if (hoveringOverNothing())
                moveUp();
        }
    }

    @Override
    public void refresh () {
        updateLists();
    }



}