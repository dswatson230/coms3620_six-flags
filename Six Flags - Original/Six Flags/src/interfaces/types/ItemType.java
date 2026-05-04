package interfaces.types;

import interfaces.ItemInterface;
import models.item.Concession;
import models.item.DailyTicket;
import models.item.Merch;
import models.item.SeasonPass;

public enum ItemType {
    DAILY_TICKET {
        @Override public ItemInterface create(String name, double price) { return new DailyTicket(name, price); }
    },

    SEASON_PASS {
        @Override public ItemInterface create(String name, double price) { return new SeasonPass(name, price); }
    },
    CONCESSION {
        @Override public ItemInterface create(String name, double price) { return new Concession(name, price); }
    },
    MERCH {
        @Override public ItemInterface create(String name, double price) { return new Merch(name, price); }
    };

    public abstract ItemInterface create(String name, double price);
}
