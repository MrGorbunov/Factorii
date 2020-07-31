package minirpg.inventory;

import java.security.InvalidParameterException;

public class Inventory {

    private int[] itemAmounts;

    public Inventory () {
        itemAmounts = new int[ItemIndex.values().length];
    }



    //
    // Inventory operations
    //

    public void addItem (ItemIndex item) {
        itemAmounts[item.ordinal()]++;
    }

    public void addItemMulti (ItemIndex item, int amount) {
        itemAmounts[item.ordinal()] += amount;
    }

    public void removeItem (ItemIndex item) {
        if (itemAmounts[item.ordinal()] < 1)
            throw new InvalidParameterException("Attempted to remove item with 0 quantity");

        itemAmounts[item.ordinal()]--;

    }

    public void removeItemMulti (ItemIndex item, int amount) {
        if (itemAmounts[item.ordinal()] < amount)
            throw new InvalidParameterException("Attempted to remove item without sufficient quantity");

        itemAmounts[item.ordinal()] -= amount;
    }



    public int getQuantity (ItemIndex item) {
        return itemAmounts[item.ordinal()];
    }

    public int getTotalSize () {
        int total = 0;

        for (int stack : itemAmounts) {
            total += stack;
        }

        return total;
    }

}