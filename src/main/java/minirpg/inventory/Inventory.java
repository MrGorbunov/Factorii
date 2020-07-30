package minirpg.inventory;

public class Inventory {

    private int[] itemAmounts;

    public Inventory () {
        itemAmounts = new int[ItemIndex.values().length];
    }

    public void addItem (ItemIndex item) {
        itemAmounts[item.ordinal()]++;
    }

    public void addItem (ItemIndex item, int amount) {
        itemAmounts[item.ordinal()] += amount;
    }

    public int getQuantity (ItemIndex item) {
        return itemAmounts[item.ordinal()];
    }

}