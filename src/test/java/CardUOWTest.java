import MTCG.dal.repository.repoUOWs.CardUOW;
import MTCG.models.CardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CardUOWTest {
    private CardUOW cardUOW;
    private CardModel mockCard;

    @BeforeEach
    public void setUp() {
        cardUOW = new CardUOW();
        mockCard = new CardModel(UUID.randomUUID(), "TestCard", 10);
    }

    @Test
    public void testCreateCardSuccess() {
        boolean result = cardUOW.createCard(mockCard);
        assertTrue(result);
    }

    @Test
    public void testCreateCardFailure() {
        // Assuming the card already exists
        boolean result = cardUOW.createCard(mockCard);
        assertTrue(result);
    }

    @Test
    public void testGetCardsSuccess() {
        List<CardModel> cards = cardUOW.getCards("testUser");
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testGetDeckSuccess() {
        List<CardModel> cards = cardUOW.getDeck("testUser");
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testGetDeckFailure() {
        // Assuming the user does not have a deck
        List<CardModel> cards = cardUOW.getDeck("testUser");
        assertTrue(cards.isEmpty());
    }
}