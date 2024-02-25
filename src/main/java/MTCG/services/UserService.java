package MTCG.services;

import MTCG.controllers.UserController;
import MTCG.dal.repository.CardRepository;
import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.models.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

public class UserService implements Service {

    private UserRepository userRepository = UserRepository.getInstance();
    private UserController userController;
    private CardRepository cardRepository = new CardRepository();

    public UserService(UserController userController, UserRepository userRepository) {
        this.userController = userController;
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

    public Response loginUser(Request request) {
        String requestBody = request.getBody();
        String username = requestBody.split("\"Username\":")[1].split(",")[0].replaceAll("\"", "").trim();
        String password = requestBody.split("\"Password\":")[1].split("}")[0].replaceAll("\"", "").trim();

        if (userRepository.userExists(username)) {
            UserModel user = userRepository.getUserByUsername(username);
            if (user != null && password.equals(user.getPassword())) {
                userController.addLoggedInUser(user);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{'message': 'User login successful'}"
                );
            } else {

                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{'message': 'Invalid password provided'}"
                );
            }
        } else {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{'message': 'Invalid username provided'}"
            );
        }
    }


    public Response buyPackage(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        UserModel user = userRepository.getUserByUsername(username);
        if (user == null) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        if (user.getCoinAmount() < 5) {
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{'message': 'Not enough money for buying a card package'}");
        }

        boolean isPackageBought = userRepository.buyPackage(user);
        if (!isPackageBought) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{'message': 'No card package available for buying'}");
        }

        return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'Package successfully bought'}");
    }

    public Response upgradeCard(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> body;
        try {
            body = objectMapper.readValue(request.getBody(), new TypeReference<Map<String, String>>(){});
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{'message': 'Invalid JSON format'}");
        }

        String cardId = body.get("CardId");
        if (cardId == null) {
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{'message': 'CardId is missing'}");
        }

        String username = request.getHeaderMap().getHeader("Authorization").substring(7).split("-")[0];
        UserModel user = userRepository.getUserByUsername(username);
        if (user == null) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'User not found'}");
        }

        boolean isCardUpgraded = cardRepository.upgradeCard(username, cardId, 10.0);
        if (!isCardUpgraded) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{'message': 'Card not found or not enough coins'}");
        }

        return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'Card successfully upgraded'}");
    }

    public Response updateUser(Request request) {
        String requestBody = request.getBody();
        String username = request.getHeaderMap().getHeader("Authorization").substring(7).split("-")[0];
        String name = requestBody.split("\"Name\":")[1].split(",")[0].replaceAll("\"", "").trim();
        String bio = requestBody.split("\"Bio\":")[1].split(",")[0].replaceAll("\"", "").trim();
        String image = requestBody.split("\"Image\":")[1].split("}")[0].replaceAll("\"", "").trim();

        boolean isUpdated = userRepository.updateUser(username, name, bio, image);

        if (isUpdated) {
            return new Response(HttpStatus.OK, ContentType.JSON, "User successfully updated.");
        } else {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "User not found.");
        }
    }

    public Response getUserData(Request request, String username) {
        UserModel user = userController.getUserFromBearerToken(request.getHeaderMap().getHeader("Authorization"));
        if (user == null || !userController.getLoggedInUsers().contains(user.getUsername())) {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{'message': 'Access token is missing or invalid'}"
            );
        }

        UserModel userData = userRepository.getUserData(username);
        if (userData == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{'message': 'User not found'}"
            );
        }

        String userDataJson = String.format(
                "{'Name': '%s', 'Bio': '%s', 'Image': '%s'}",
                userData.getName(),
                userData.getBio(),
                userData.getImage()
        );

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                userDataJson
        );
    }



}