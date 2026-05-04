package models.item;

import interfaces.types.ItemType;

public class DailyTicket extends Item {

    public DailyTicket(String name, double price) {
        super(name, price);
    }

    @Override
    public ItemType getType() {
        return ItemType.DAILY_TICKET;
    }
}
