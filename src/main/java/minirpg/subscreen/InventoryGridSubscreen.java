package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.*;
import minirpg.inventory.*;

public class InventoryGridSubscreen {
    
    private int width;
    private int height;
    private int xOff;
    private int yOff;
    private boolean active;

    /*
    The selection is a 2D value so there are hella variables
    Scrolling however is still only up & down so only 1 its pos
    */
    private int colWidth;
    private int cols;
    private int rows;
    private int xPos; // Selection position
    private int yPos;

    private int pad;
    private int scrollPos;
    private int maxScroll;
    private ArrayList<ItemIndex> displayItems;
    private ArrayList<String> displayStrings;

    private Inventory inventory;
    private boolean hideResource;
    private String title;
    
    public InventoryGridSubscreen (int width, int height, int xOff, int yOff) {
        this.width = width;
        this.height = height;
        this.xOff = xOff;
        this.yOff = yOff;

        pad = 1;
        xPos = 0; 
        yPos = 0; 
        scrollPos = 0;

        colWidth = 25;
        cols = (width - 2*pad) / colWidth;
        rows = height - 2*pad;
        inventory = GameState.player.getInventory();
        hideResource = true;
        title = "";

        updateLists();
    }

    public InventoryGridSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }

    /**
     * Returns false if there are no entries in the grid
     */
    public boolean setActive (boolean active) {
        if (active == false) {
            this.active = active;
            return true;
        }

        refresh();

        if (displayStrings.size() == 0) {
            this.active = false;
            return false;
        } else {
            this.active = active;
            return true;
        }
    }

    public ItemIndex getSelectedItem () {
        int index = (scrollPos + yPos) * cols + xPos;
        return displayItems.get(index);
    }




    //
    // Configurability
    //

    public void setPad (int pad) {
        this.pad = pad;
    }

    public void setColumns (int columns) {
        cols = columns;
        rows = height - 2*pad;
    }

    public void setIgnoreResources (boolean ignoreResources) {
        hideResource = ignoreResources;
    }

    /**
     * Sets the title to display. If set to an empty string 
     * (length == 1) no title is displayed.
     */
    public void setTitle (String title) {
        this.title = title;

        if (title.length() == 0)
            rows = height - 2*pad;
        else
            rows = height - 2*pad - 1;
    }

    public void setInventory (Inventory inv) {
        inventory = inv;
    }

    public void refresh () {
        updateLists();
    }





    //
    // Scrolling Logic
    //

    public void moveLeft () {
        xPos--;

        if (xPos < 0 || hoveringOverNothing()) { xPos++; }
    }

    public void moveRight () {
        xPos++;

        if (xPos >= cols || hoveringOverNothing()) { xPos--; }
    }

    public void moveUp () {
        yPos--;

        if (yPos < 0) { 
            yPos++; 
            scrollUp();
        }
    }

    public void moveDown () {
        yPos++;

        if (yPos >= rows) { 
            yPos--; 
            scrollDown();
        }

        if (hoveringOverNothing()) { yPos--; }
    }

    private void scrollUp () {
        scrollPos--;

        if (scrollPos < 0) { scrollPos = 0; }
    }

    private void scrollDown () {
        scrollPos++;

        // Only possible if not @ bottom
        if (scrollPos > maxScroll) { scrollPos = maxScroll; }
    }

    /**
     * Used to check if the current selection is hovering over a slot which does not contain any item
     */
    private boolean hoveringOverNothing () {
        int index = (scrollPos + yPos) * cols + xPos;

        return index >= displayStrings.size();
    }




    //
    // List Updating
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
    }

    public Inventory getInventory () {
        return inventory;
    }





    //
    // Display
    //

    public void drawSubscreen (AsciiPanel terminal) {
        drawItemGrid(terminal);
        drawArrows(terminal);
    }

    private void drawItemGrid (AsciiPanel terminal) {
        int titleYOffset = 0;
        if (title.length() != 0) {
            terminal.write(title, xOff+pad, yOff+pad, Color.CYAN);
            titleYOffset = 1;
        }

        for (int y=0; y<rows; y++) { 
            for (int x=0; x<cols; x++) {
                int index = (y + scrollPos) * cols + x;
                if (index >= displayStrings.size())
                    return;

                boolean highlightBG = x == xPos && y == yPos && active;
                Color bgColor = highlightBG ? Color.GRAY : Color.BLACK;

                int xCord = xOff + pad + x*colWidth;
                int yCord = yOff + pad + y + titleYOffset;
                terminal.write(displayStrings.get(index), xCord, yCord, Color.LIGHT_GRAY, bgColor);
            }
        }
    }

    private void drawArrows (AsciiPanel terminal) {
        if (active == false) return;

        // Top arrows
        if (scrollPos > 0 && maxScroll != 0) {
            terminal.write(upScrollArrows(), xOff, yOff + pad - 1);
        }

        // Bottom arrows
        if (scrollPos < maxScroll && maxScroll != 0) {
            terminal.write(downScrollArrows(), xOff, yOff + height - pad);
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

}