package MTCG.services;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.models.UserModel;

public class UserService implements Service {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {
        Response response;
        response = new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{\"error\": \"UserService not found\"}");
        return response;
    }

    public Response createUser(String username, String password, boolean isAdmin) {
        if (userRepository.userExists(username)) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "{'message': 'User with same username already registered'}"
            );
        } else {
            // Implement your logic to create a new user
            userRepository.save(new UserModel(username, password, isAdmin));
            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{'message': 'User successfully created'}"
            );
        }
    }



}