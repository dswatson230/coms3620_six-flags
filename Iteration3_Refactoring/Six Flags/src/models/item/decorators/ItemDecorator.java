package models.item.decorators;

import interfaces.ItemInterface;

import java.util.List;

public class ItemDecorator implements ItemInterface {
    protected ItemInterface wrappee;

    public ItemDecorator(ItemInterface item) {
        wrappee = item;
    }


    public String getName() { return wrappee.getName(); }
    public double getPrice() { return wrappee.getPrice(); }
    public List<String> getBenefits() { return wrappee.getBenefits(); }

    public String getInfo() { return wrappee.getInfo(); }
}
