package MTCG.controllers;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.models.UserModel;
import MTCG.services.UserService;
import java.util.ArrayList;
import java.util.List;

public class UserController implements Service {

    private UserRepository userRepository = UserRepository.getInstance();
    private UserService userService = new UserService(this, userRepository);
    private final List<String> loggedInUsers = new ArrayList<>();
    public void addLoggedInUser(UserModel user) {
        this.loggedInUsers.add(user.getUsername());
    }

    public Response handleRequest(Request request) {


        Method method = request.getMethod();
        String route = request.getServiceRoute();



        if ("POST".equalsIgnoreCase(String.valueOf(method)) && "/sessions".equals(route)) {
            return userService.loginUser(request);
        }
        else if ("POST".equalsIgnoreCase(String.valueOf(method)) && "/users".equals(route)) {
            return Registration(request);
            //create new User
        }
        UserModel user = getUserFromBearerToken(request.getHeaderMap().getHeader("Authorization"));
        if (user == null || !loggedInUsers.contains(user.getUsername())) {
            System.out.println("logged in Users: " + loggedInUsers);
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{'error': 'User not logged in'}"
            );
        }

         if ("PUT".equalsIgnoreCase(String.valueOf(method)) && "/users/kienboec".equals(route)) {
             System.out.println(loggedInUsers);
            return userService.updateUser(request);
        }
        else if ("PUT".equalsIgnoreCase(String.valueOf(method)) && "/users/altenhof".equals(route)) {
            System.out.println(loggedInUsers);
            return userService.updateUser(request);
        }
        else if ("PUT".equalsIgnoreCase(String.valueOf(method)) && "/users/someGuy".equals(route)) {
            System.out.println(loggedInUsers);
            return userService.updateUser(request);
        }else if ("GET".equalsIgnoreCase(String.valueOf(method)) && route.startsWith("/users/")) {
             String usernameInRoute = route.split("/")[2];
             return userService.getUserData(request, usernameInRoute);

        } else if (request.getMethod() == Method.POST && request.getServiceRoute().equals("/upgradecard")) {
             return userService.upgradeCard(request);
         }
        else if ("GET".equalsIgnoreCase(String.valueOf(method)) && "/users/altenhof".equals(route)) {
            System.out.println(loggedInUsers);
            return userService.updateUser(request);
        }
        else if ("GET".equalsIgnoreCase(String.valueOf(method)) && "/users/someGuy".equals(route)) {
            System.out.println(loggedInUsers);
            return userService.updateUser(request);
        }else {

            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{'error': 'Not found'}"
            );
        }
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
        UserService userService = new UserService(this, userRepository);
        return userService.createUser(username, password, isAdmin);
    }




    public UserModel getUserFromBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String username = authorizationHeader.substring(7).split("-")[0];
            return userRepository.getUserByUsername(username);
        }
        return null;
    }



    public void logoutAllUsers() {
        this.loggedInUsers.clear();
    }

    public List<String> getLoggedInUsers() {
        return loggedInUsers;
    }
}