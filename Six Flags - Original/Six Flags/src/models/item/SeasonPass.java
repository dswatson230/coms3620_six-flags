package models.item;

import interfaces.types.ItemType;
import interfaces.types.SeasonPassType;

public class SeasonPass extends Item {
    private SeasonPassType type;
    private double discount;

    public SeasonPass(String name, double price) {
        super(name, price);
    }

    @Override
    public ItemType getType() {
        return ItemType.SEASON_PASS;
    }

    public SeasonPassType getSeasonPassType() {
        return type;
    }

    public void setSeasonPassType(SeasonPassType type) {
        this.type = type;
        discount = type.getDiscount();
        setBenefits(type.getBenefits());
    }
}
