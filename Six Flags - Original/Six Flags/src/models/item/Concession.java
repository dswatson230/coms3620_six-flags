package models.item;

import interfaces.types.ItemType;

public class Concession extends Item {

    public Concession(String name, double price) {
        super(name, price);
    }

    @Override
    public ItemType getType() {
        return ItemType.CONCESSION;
    }
}
