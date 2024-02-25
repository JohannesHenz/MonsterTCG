package MTCG.dal.repository;

import MTCG.dal.repository.repoUOWs.CardUOW;
import MTCG.httpserver.server.Response;
import MTCG.models.CardModel;

import java.util.List;

public class CardRepository {
    private CardUOW cardUOW = new CardUOW();

    public List<CardModel> getCards(String username) {
        return cardUOW.getCards(username);
    }

    public List<CardModel> getDeck(String username) {
        return cardUOW.getDeck(username);
    }

    public boolean putDeck(String username, List<String> cardIds) {
        String deleteDeckSql = "DELETE FROM Deck WHERE Owner = ?";
        boolean isDeckDeleted = cardUOW.executeDelete(deleteDeckSql, username);

        if (!isDeckDeleted) {
            return false;
        }

        String insertDeckSql = "INSERT INTO Deck (Owner, Card1, Card2, Card3, Card4) VALUES (?, ?, ?, ?, ?)";
        return cardUOW.executeInsert(insertDeckSql, username, cardIds);
    }
}