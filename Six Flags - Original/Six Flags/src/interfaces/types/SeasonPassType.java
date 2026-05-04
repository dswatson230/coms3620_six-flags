package interfaces.types;

import java.util.ArrayList;
import java.util.List;

public enum SeasonPassType {
    SILVER {
        @Override public double getDiscount() { return 0.1; }
        @Override public List<String> getBenefits() { return new ArrayList<>(List.of("Unlimited Visits through Labor Day", "Free Parking", "10% Food/Merch")); }
    },
    GOLD {
        @Override public double getDiscount() { return 0.1; }
        @Override public List<String> getBenefits() { return new ArrayList<>(List.of("Unlimited Visits all Year", "Free Parking", "10% Food/Merch", "One Single-Use Fast Lane per Visit")); }
    },
    PRESTIGE {
        @Override public double getDiscount() { return 0.15; }
        @Override public List<String> getBenefits() { return new ArrayList<>(List.of("Unlimited Visits all Year", "Free Parking", "15% Food/Merch", "One Single-Use Fast Lane per Visit", "Two Free Bring-A-Friend's per Year")); }
    };

    public abstract double getDiscount();
    public abstract List<String> getBenefits();
}
