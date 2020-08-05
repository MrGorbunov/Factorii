package minirpg.subscreen;

import java.util.ArrayList;
import java.awt.Color;

import asciiPanel.AsciiPanel;
import minirpg.*;
import minirpg.inventory.*;

// TODO: A lot of duplicate code w/ InventorySubscreen : maybe the Screens should be responsible for feeding String[] arrays;
// TODO: Extend a common interface w/ WorldSubscreen
public class InventoryGridSubscreen {
    
    private int width;
    private int height;
    private int xOff;
    private int yOff;
    private boolean active;

    /*
    The selection is a 2D value so there are hella variables
    Scrolling however is still only up / down so 1 var
    */
    private int colWidth;
    private int cols;
    private int rows;
    private int xPos; // Selection position
    private int yPos;

    private int pad;
    private int scrollPos;
    private int maxScroll;
    private String[] displayStrings;

    
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

        updateLists();
        maxScroll = (displayStrings.length + 2) / cols - 3;
    }

    public InventoryGridSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }

    public void setActive (boolean active) {
        this.active = active;
    }

    public ItemIndex getSelectedItem () {
        int index = (scrollPos + yPos) * cols + xPos;
        return allCraftedItems().get(index);
    }

    public void refresh () {
        updateLists();
        maxScroll = (displayStrings.length + 2) / cols - 3;
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

        return index >= displayStrings.length;
    }




    //
    // List Updating
    //

    private void updateLists () {
        Inventory inv = GameState.inventory;
        ArrayList<ItemIndex> craftedItems = allCraftedItems();
        displayStrings = new String[craftedItems.size()];

        for (int i=0; i<craftedItems.size(); i++) {
            displayStrings[i] = craftedItems.get(i) + " x" + inv.getQuantity(craftedItems.get(i));
        }
    }

    private ArrayList<ItemIndex> allCraftedItems () {
        Inventory inv = GameState.inventory;
        ArrayList<ItemIndex> craftedItems = new ArrayList<ItemIndex> ();

        for (ItemIndex item : ItemIndex.values()) {
            if (item.isPlacable() && inv.getQuantity(item) > 0)  {
                craftedItems.add(item);
            }
        }

        return craftedItems;
    }





    //
    // Display
    //

    public void drawSubscreen (AsciiPanel terminal) {
        drawArrows(terminal);
        drawItemGrid(terminal);
    }

    private void drawItemGrid (AsciiPanel terminal) {
        for (int y=0; y<rows; y++) { for (int x=0; x<cols; x++) {
                int index = (y + scrollPos) * cols + x;
                if (index >= displayStrings.length)
                    return;

                boolean highlightBG = x == xPos && y == yPos && active;
                Color bgColor = highlightBG ? Color.GRAY : Color.BLACK;

                int xCord = xOff + pad + x*colWidth;
                int yCord = yOff + pad + y;
                terminal.write(displayStrings[index], xCord, yCord, Color.LIGHT_GRAY, bgColor);
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