package MTCG.controllers;

import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;

public class PackageController implements Service {

    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.POST && route.equals("/packages")) {
            return postPackages(request);

        }
        return null;
    }

    private Response postPackages(Request request) {
        return null;
    }
}
