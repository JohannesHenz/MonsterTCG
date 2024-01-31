package MTCG.controllers;

import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;

public class UserController implements Service {

    @Override
    public Response handleRequest(Request request) {
        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if ("POST".equalsIgnoreCase(String.valueOf(method)) && "/users".equals(route)) {
            return Registration(request);
            //create new User
        } else if ("GET".equalsIgnoreCase(String.valueOf(method)) && "/users".equals(route)) {
            return showUsers(request);
        } else if ("PUT".equalsIgnoreCase(String.valueOf(method)) && "/users".equals(route)) {
            return editUsers(request);
        }

        // If no conditions match, return a 404 response
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{'error': 'Not found'}"
        );
    }

    private Response Registration(Request request) {
        // Registration logic goes here.
        // You can create your Response based on the logic and return it.
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                "{'message': 'Registration successful'}"
        );
    }

    private Response showUsers(Request request){

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                "{'message': 'Showing users'}"
        );
    }

    private Response editUsers(Request request){

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                "{'message': 'Showing users'}"
        );
    }
}
