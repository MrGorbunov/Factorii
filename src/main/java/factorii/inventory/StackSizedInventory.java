package factorii.inventory;

/**
 * Like an inventory but capped in stack size.
 * 
 * There is no upper cap to the total # of items, but
 * instead to total # of a specific item.
 * 
 * Example use case are filters, in which there should only
 * ever be at most 1 of an item, but it doesn't matter how many
 * items there are overall.
 */

public class StackSizedInventory extends Inventory {

    private int maxStack;

    public StackSizedInventory (int maxStack) {
        this.maxStack = maxStack;
    }

    /**
     * Will add an item if there is space for it. Otherwise
     * does nothing.
     */
    @Override
    public void addItem (ItemIndex item) {
        if (itemAmounts[item.ordinal()] >= maxStack)
            return;

        itemAmounts[item.ordinal()]++;
    }

    /**
     * Will add multiple items if there is space for it.
     * If there is not enough space will not add ANY items.
     */
    @Override
    public void addItemMulti (ItemIndex item, int amount) {
        if (itemAmounts[item.ordinal()] + amount > maxStack)
            return;

        itemAmounts[item.ordinal()] += amount;
    }

}