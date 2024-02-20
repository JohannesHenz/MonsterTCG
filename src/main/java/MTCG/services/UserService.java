package MTCG.services;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.models.UserCredentialsModel;
import MTCG.httpserver.server.Response;

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

    public void createUser(String username, String password) {
       /*
        boolean userExists = userRepository.userExists(username);

        if (userExists) {
            throw new IllegalArgumentException("User already exists");
        }

        UserCredentialsModel newUser = new UserCredentialsModel(username, password);
        userRepository.save(newUser);
    */
    }



}