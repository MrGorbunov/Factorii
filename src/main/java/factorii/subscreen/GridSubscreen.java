package factorii.subscreen;

import java.util.ArrayList;
import java.awt.Color;
import asciiPanel.AsciiPanel;

public class GridSubscreen {
    
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
    protected int cols;
    protected int rows;
    protected int xPos; // Selection position
    protected int yPos;

    private int pad;
    protected int scrollPos;
    protected int maxScroll;
    protected ArrayList<String> displayStrings;

    private int markedOptionIndex;
    private String title;
    
    public GridSubscreen (int width, int height, int xOff, int yOff) {
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
        title = "";

        markedOptionIndex = -1;
    }

    public GridSubscreen (int width, int height) {
        this(width, height, 0, 0);
    }

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

    public void refresh () {
        // Lmao sike nigga
    }

    public void setOptions (ArrayList<String> options) {
        displayStrings = options;
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

        if (scrollPos > maxScroll) { scrollPos = maxScroll; }
    }

    /**
     * Used to check if the current selection is hovering over a slot which does not contain any item
     */
    protected boolean hoveringOverNothing () {
        int index = (scrollPos + yPos) * cols + xPos;

        return index >= displayStrings.size();
    }





    //
    // Marking Logic
    //
    // Adds a little star next to the marked option.
    //

    /**
     * Sets a new option to be the marked option.
     * If the index is out of bounds (< -1 or >= # of options)
     * then nothing happens.
     * @param optionIndex int the index of the option
     */
    public void setMarkedOption (int optionIndex) {
        if (optionIndex < 0 ||
            optionIndex >= displayStrings.size())
                return;
        
        markedOptionIndex = optionIndex;
    }

    /**
     * Sets the marked option to be the current selection
     */
    public void setMarkedOptionToSelection () {
        markedOptionIndex = (scrollPos + yPos) * cols + xPos;
    }

    public void turnOffMarkedOption () {
        markedOptionIndex = -1;
    }





    //
    // Display
    //

    public void drawSubscreen (AsciiPanel terminal) {
        drawGrid(terminal);
        drawArrows(terminal);
    }

    private void drawGrid (AsciiPanel terminal) {
        int yCord = yOff + pad;
        int xCord = xOff + pad;

        if (title.length() != 0) {
            terminal.write(title, xOff+pad, yCord, Color.CYAN);
            yCord++;
        }

        if (displayStrings.size() == 0) {
            Color bgColor = active ? Color.GRAY : Color.BLACK;
            String noItemText = "No Items";
            int middleX = width / 2 + xOff - noItemText.length() / 2;
            terminal.write(noItemText, middleX, yCord, Color.LIGHT_GRAY, bgColor);
            return;
        }

        for (int y=0; y<rows; y++) { 
            for (int x=0; x<cols; x++) {
                int index = (y + scrollPos) * cols + x;
                if (index >= displayStrings.size())
                    return;

                boolean highlightBG = x == xPos && y == yPos && active;
                Color bgColor = highlightBG ? Color.GRAY : Color.BLACK;
                
                String displayString = displayStrings.get(index);
                if (index == markedOptionIndex) {
                    displayString += " *";
                    System.out.println(displayString);

                }

                terminal.write(displayString, xCord, yCord, Color.LIGHT_GRAY, bgColor);

                xCord += colWidth;
            }

            yCord++;
            xCord = xOff + pad;
        }
    }

    private void drawArrows (AsciiPanel terminal) {
        if (active == false) return;

        // Top arrows
        if (scrollPos > 0 && maxScroll != 0) {
            terminal.write(upScrollArrows(), xOff+pad, yOff + pad - 1);
        }

        // Bottom arrows
        if (scrollPos < maxScroll && maxScroll != 0) {
            terminal.write(downScrollArrows(), xOff+pad, yOff + height - pad);
        }

    }

    private String downScrollArrows () {
        StringBuilder res = new StringBuilder();

        for (int i=0; i<(width - 2*pad)/3; i++) {
            res.append("\\/ ");
        }

        return res.toString();
    }

    private String upScrollArrows () {
        StringBuilder res = new StringBuilder();

        for (int i=0; i<(width - 2*pad)/3; i++) {
            res.append("/\\ ");
        }

        return res.toString();
    }





    //
    // Getting Info
    //

    public int getSelectedIndex () {
        return (scrollPos + yPos) * cols + xPos;
    }

    /**
     * Will return the selected option's string.
     * If options list empty, returns null
     */
    public String getSelectedOption () {
        if (displayStrings == null ||
            displayStrings.size() == 0)
                return null;

        int index = (scrollPos + yPos) * cols + xPos;
        return displayStrings.get(index);
    }

}

