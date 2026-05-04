package interfaces.types;

public enum ItemAddOns {
    FAST_LANE("Fast Lane", 125),
    VIP("VIP Experience", 350),
    PARKING("Parking", 39),
    MEAL_PLAN("Meal Plan", 0);

    private final String displayName;
    private final double price;

    ItemAddOns(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }
}
