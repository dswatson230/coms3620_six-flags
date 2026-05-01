package interfaces.types;

public enum MealPlanType {
    SINGLE_MEAL {
        @Override public double getPrice() { return 25; }
        @Override public String toString() { return "Single Meal"; }
    },
    ALL_DAY {
        @Override public double getPrice() { return 35; }
        @Override public String toString() { return "All Day Dining"; }
    },
    DAILY_DRINKS {
        @Override public double getPrice() { return 29; }
        @Override public String toString() { return "All Day Drinks"; }
    },
    ALL_SEASON {
        @Override public double getPrice() { return 145; }
        @Override public String toString() { return "All Season Meals"; }
    },
    SEASON_DRINKS {
        @Override public double getPrice() { return 39; }
        @Override public String toString() { return "All Season Drinks"; }
    };

    public abstract double getPrice();
}
