package MTCG.models;

import java.util.UUID;

public class TradingDealModel {
    private UUID Id;
    private UUID CardToTrade;
    private String Type;
    private double MinimumDamage;

    // Getters and Setters

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public UUID getCardToTrade() {
        return CardToTrade;
    }

    public void setCardToTrade(UUID cardToTrade) {
        CardToTrade = cardToTrade;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public double getMinimumDamage() {
        return MinimumDamage;
    }

    public void setMinimumDamage(double minimumDamage) {
        MinimumDamage = minimumDamage;
    }
}