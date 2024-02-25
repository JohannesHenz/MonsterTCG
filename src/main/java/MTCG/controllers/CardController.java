package MTCG.controllers;

import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.services.CardService;
import MTCG.models.CardModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class CardController implements Service {

    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.GET && route.equals("/cards")) {
            return getCards(request);
        }
        else if (method == Method.GET && route.equals("/deck")) {
            return getDeck(request);
        }
        else if (method == Method.PUT && route.equals("/deck")) {
           return putDeck(request);
        }
        else {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{'error': 'Not found'}"
            );
        }
    }

    private Response getCards(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        CardService cardService = new CardService();
        return cardService.getCards(username);
    }

    private Response getDeck(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        CardService cardService = new CardService();
        List<CardModel> cards = cardService.getDeck(username);
        if (cards.isEmpty()) {
            return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{'message': 'The request was fine, but the deck doesn't have any cards'}");
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

    private Response putDeck(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> cardIds;
        try {
            cardIds = objectMapper.readValue(request.getBody(), new TypeReference<List<String>>(){});
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{'message': 'Invalid JSON format'}");
        }

        if (cardIds.size() != 4) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{'message': 'The provided deck did not include the required amount of cards'}");
        }

        CardService cardService = new CardService();
        return cardService.putDeck(username, cardIds);
    }
}
