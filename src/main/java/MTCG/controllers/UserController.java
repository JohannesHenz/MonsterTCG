package MTCG.controllers;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.services.UserService;

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
        String requestBody = request.getBody();
        String username = requestBody.split("\"Username\":")[1].split(",")[0].replaceAll("\"", "").trim();
        String password = requestBody.split("\"Password\":")[1].split("}")[0].replaceAll("\"", "").trim();
        boolean isAdmin = false;

        if (username.equals("admin") && password.equals("istrator")) {
            isAdmin = true;
        }

        // Call the createUser method in the UserService class
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        return userService.createUser(username, password, isAdmin);
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
