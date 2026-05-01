package interfaces.types;

import interfaces.ItemInterface;
import models.item.Concession;
import models.item.DailyTicket;
import models.item.Merch;
import models.item.SeasonPass;

public enum ItemType {
    DAILY_TICKET {
        @Override public ItemInterface create(String name, double price) { return new DailyTicket(name, price); }
        //@Override public ArrayList<ItemAddOn> getAddOns() { return new ArrayList<ItemAddOn>(List.of(ItemAddOns.FAST_LANE, ItemAddOns.PARKING, ItemAddOns.MEAL_PLAN, ItemAddOns.VIP)); }
    },

    SEASON_PASS {
        @Override public ItemInterface create(String name, double price) { return new SeasonPass(name, price); }
        //@Override public ArrayList<ItemAddOn> getAddOns() { return new ArrayList<ItemAddOn>(List.of(ItemAddOns.FAST_LANE, ItemAddOns.MEAL_PLAN, ItemAddOns.VIP)); }
    },
    CONCESSION {
        @Override public ItemInterface create(String name, double price) { return new Concession(name, price); }
        //@Override public ArrayList<ItemAddOn> getAddOns() { return new ArrayList<ItemAddOn>(List.of()); }
    },
    MERCH {
        @Override public ItemInterface create(String name, double price) { return new Merch(name, price); }
        //@Override public ArrayList<ItemAddOn> getAddOns() { return new ArrayList<ItemAddOn>(List.of()); }
    };

    public abstract ItemInterface create(String name, double price);
    //public abstract ArrayList<ItemAddOn> getAddOns();
}
