package models.item.decorators;

import interfaces.ItemInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VIPDecorator extends ItemDecorator {

    public VIPDecorator(ItemInterface item) {
        super(item);
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice() + 350;
    }

    @Override
    public List<String> getBenefits() {
        ArrayList<String> benefits = new ArrayList<>(wrappee.getBenefits());
        benefits.add("VIP Experience ($350)\n\t\t\t- Guided Tour\n\t\t\t- Reserved Parking\n\t\t\t- Unlimited Drinks\n\t\t\t- Front of Line Access");
        return benefits;
    }
}
