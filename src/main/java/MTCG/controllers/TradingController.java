package MTCG.controllers;

import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;

public class TradingController implements Service {
    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.GET && route.equals("/tradings")) {
            return getTradings(request);
        } else if (method == Method.POST && route.equals("/tradings")) {
            return postTradings(request);
        } else if (method == Method.DELETE && route.equals("/tradings")) {
            return deleteTradings(request);
        }
        return null;
    }

    private Response getTradings(Request request) {
        return null;
    }

    private Response postTradings(Request request) {
        return null;
    }

    private Response deleteTradings(Request request) {
        return null;
    }
}
