package MTCG.models;

import java.util.List;

public class PackageModel {

    private String packageId;
    private List<CardModel> cards;

    public PackageModel(String packageId, List<CardModel> cards) {
        this.packageId = packageId;
        this.cards = cards;
    }

    public String getPackageId() {
        return this.packageId;
    }

    public List<CardModel> getCards() {
        return this.cards;
    }
}