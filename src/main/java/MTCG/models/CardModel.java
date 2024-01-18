package MTCG.models;

import java.util.UUID;


public class CardModel {

    private UUID Id;
    private String Name;
    private double Damage;

    // Getters and Setters

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getDamage() {
        return Damage;
    }

    public void setDamage(double damage) {
        Damage = damage;
    }
}
