package MTCG.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.UUID;

public class CardModel {



    @JsonProperty("Id")
    private UUID Id;

    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Damage")
    private double Damage;

    // Getters and Setters
    public CardModel() {}

    public CardModel(UUID Id, String Name, double Damage) {
        this.Id = Id;
        this.Name = Name;
        this.Damage = Damage;
    }

    public CardModel(UUID Id, double Damage) {
        this.Id = Id;
        this.Damage = Damage;
    }

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

    public String getElementType() {
        String elementType;

        if (this.getName().contains("Water")) {
            elementType = "Water";
        } else if (this.getName().contains("Fire")) {
            elementType = "Fire";
        } else {
            elementType = "Regular";

        }
        return elementType;
    }
}