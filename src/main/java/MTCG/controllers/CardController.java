package MTCG.controllers;

import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;

public class CardController implements Service {

    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.GET && route.equals("/cards")) {
            return getCards(request);
        }


        return null;
    }

    private Response getCards(Request request) {
        return null;
    }
}
