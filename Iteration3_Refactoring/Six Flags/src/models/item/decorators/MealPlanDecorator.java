package models.item.decorators;

import interfaces.ItemInterface;
import interfaces.types.MealPlanType;

import java.util.ArrayList;
import java.util.List;

public class MealPlanDecorator extends ItemDecorator {

    private final MealPlanType type;

    public MealPlanDecorator(ItemInterface item, MealPlanType type) {
        super(item);
        this.type = type;
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice() + type.getPrice();
    }

    @Override
    public List<String> getBenefits() {
        ArrayList<String> benefits = new ArrayList<>(wrappee.getBenefits());
        benefits.add("Meal Plan: " + type.toString() + " ($" + type.getPrice() + ")");
        return benefits;
    }
}
