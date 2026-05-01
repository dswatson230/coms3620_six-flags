package models.item.decorators;

import interfaces.ItemInterface;

import java.util.ArrayList;
import java.util.List;

public class FastLaneDecorator extends ItemDecorator {

    public FastLaneDecorator(ItemInterface item) {
        super(item);
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice() + 125;
    }

    @Override
    public List<String> getBenefits() {
        ArrayList<String> benefits = new ArrayList<>(wrappee.getBenefits());
        benefits.add("Fast Lane ($125)");
        return benefits;
    }
}
