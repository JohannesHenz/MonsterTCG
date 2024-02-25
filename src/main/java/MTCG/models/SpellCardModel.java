package MTCG.models;

import MTCG.models.enums.CardNames;
import MTCG.models.enums.CardTypes;
import MTCG.models.enums.ElementTypes;

import java.util.UUID;

public class SpellCardModel extends CardModel {
    private ElementTypes elementType;
    private CardNames cardName = CardNames.REGULARSPELL; // default value
    private CardTypes cardType = CardTypes.SPELL; // default value

    public SpellCardModel(UUID id, double damage, ElementTypes elementType) {
        super(id, damage);
        this.elementType = elementType;
    }



    public CardNames getCardName() {
        return cardName;
    }

    public CardTypes getCardType() {
        return cardType;
    }
}