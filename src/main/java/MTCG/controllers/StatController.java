package MTCG.controllers;

import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;

public class StatController implements Service {
    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.GET && route.equals("/stats")) {
            return getStats(request);
        }
        if (method == Method.GET && route.equals("/scoreboard")) {
            return getScoreboard(request);
        }
        return null;
    }

    private Response getStats(Request request) {
        return null;
    }

    private Response getScoreboard(Request request) {
        return null;
    }
}
