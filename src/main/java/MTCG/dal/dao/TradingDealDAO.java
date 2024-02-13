package MTCG.dal.dao;

import MTCG.models.enums.CardTypes;

public class TradingDealDAO {

    String Id;
    String cardToTrade;
    CardTypes cardType;
    float minimumDamage;

    public TradingDealDAO(String Id, String cardToTrade, CardTypes cardType, float minimumDamage) {
        this.Id = Id;
        this.cardToTrade = cardToTrade;
        this.cardType = cardType;
        this.minimumDamage = minimumDamage;
    }


}
