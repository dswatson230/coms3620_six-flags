package models.item.decorators;

import interfaces.ItemInterface;
import java.util.ArrayList;
import java.util.List;

public class ParkingDecorator extends ItemDecorator {

    public ParkingDecorator(ItemInterface item) {
        super(item);
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice() + 39;
    }


    @Override
    public List<String> getBenefits() {
        ArrayList<String> benefits = new ArrayList<>(wrappee.getBenefits());
        benefits.add("Parking ($39)");
        return benefits;
    }
}
