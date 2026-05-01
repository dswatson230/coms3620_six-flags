package models.item.decorators;

import interfaces.types.ItemAddOns;

public class AddOnSelection {
    private final ItemAddOns addOn;
    private final Object config; // MealPlanType or null

    public AddOnSelection(ItemAddOns addOn, Object config) {
        this.addOn = addOn;
        this.config = config;
    }

    public ItemAddOns getAddOn() { return addOn; }
    public Object getConfig() { return config; }
}
