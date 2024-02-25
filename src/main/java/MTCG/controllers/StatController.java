package MTCG.controllers;

import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.services.StatService;

public class StatController implements Service {

    private StatService statService;

    public StatController() {
        this.statService = new StatService();
    }

    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.GET && route.equals("/stats")) {
            return statService.getUserStats(request);

        }else if (method == Method.GET && route.equals("/scoreboard")) {
        return statService.getScoreboard();

        } else {
            // If no conditions match, return a 404 response
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{'error': 'Stats Not found'}"
            );
        }

    }

}

