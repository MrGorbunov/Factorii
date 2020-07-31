package minirpg.inventory;

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

    public void addItem (ItemIndex item, int amount) {
        itemAmounts[item.ordinal()] += amount;
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