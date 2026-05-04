package models.item.decorators;

import interfaces.ItemInterface;
import interfaces.types.ItemAddOns;
import interfaces.types.ItemType;
import interfaces.types.MealPlanType;
import models.item.InventoryItem;

public class AddOnService {

    public boolean supports(ItemAddOns addOn, ItemType type) {
        return switch (addOn) {
            case FAST_LANE, VIP, MEAL_PLAN ->
                    type == ItemType.DAILY_TICKET || type == ItemType.SEASON_PASS;

            case PARKING ->
                    type == ItemType.DAILY_TICKET;
        };
    }

    public InventoryItem apply(ItemAddOns addOn, InventoryItem item, Object config) {
        ItemInterface wrapped = item.getItem();

        switch (addOn) {
            case FAST_LANE -> wrapped = new FastLaneDecorator(wrapped);
            case VIP -> wrapped = new VIPDecorator(wrapped);
            case PARKING -> wrapped = new ParkingDecorator(wrapped);

            case MEAL_PLAN -> {
                MealPlanType type = (MealPlanType) config;
                wrapped = new MealPlanDecorator(wrapped, type);
            }
        }

        return new InventoryItem(
                wrapped,
                item.getType(),
                item.getLocation(),
                item.getQuantity()
        );
    }
}
