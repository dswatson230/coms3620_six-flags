package models.item;

import interfaces.types.ItemType;

public class Merch extends Item {

    public Merch(String name, double price) {
        super(name, price);
    }

    @Override
    public ItemType getType() {
        return ItemType.MERCH;
    }
}
