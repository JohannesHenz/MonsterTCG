package MTCG.services;

import MTCG.dal.repository.CardRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.models.CardModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CardService implements Service {
    private CardRepository cardRepository = new CardRepository();

    @Override
    public Response handleRequest(Request request) {

        return new Response(HttpStatus.NOT_IMPLEMENTED, ContentType.JSON, "{'message': 'Not implemented'}");
    }

    public Response getCards(String username) {
        List<CardModel> cards = cardRepository.getCards(username);
        if (cards.isEmpty()) {
            return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{'message': 'The request was fine, but the user doesn't have any cards'}");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String cardsJson;
            try {
                cardsJson = objectMapper.writeValueAsString(cards);
            } catch (Exception e) {
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{'message': 'Error processing the request'}");
            }
            return new Response(HttpStatus.OK, ContentType.JSON, cardsJson);
        }
    }

    public List<CardModel> getDeck(String username) {
        return cardRepository.getDeck(username);
    }

    public Response putDeck(String username, List<String> cardIds) {
        boolean isDeckUpdated = cardRepository.putDeck(username, cardIds);
        if (isDeckUpdated) {
            return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'The deck has been successfully configured'}");
        } else {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{'message': 'An error occurred while configuring the deck'}");
        }
    }
}