package MTCG.controllers;

import MTCG.dal.repository.UserRepository;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.services.PackageService;
import MTCG.services.UserService;

public class PackageController implements Service {

    private PackageService packageService = new PackageService();
    private UserRepository userRepository = UserRepository.getInstance();

    private UserController userController = new UserController();
    private UserService userService = new UserService(userController, userRepository);

    @Override
    public Response handleRequest(Request request) {

        Method method = request.getMethod();
        String route = request.getServiceRoute();

        if (method == Method.POST && route.equals("/packages")) {
            return postPackages(request);
        } else if (method == Method.POST && route.equals("/transactions/packages")) {
            return buyPackage(request);
        }

        return null;
    }

    private Response postPackages(Request request) {
        return packageService.createPackage(request);
    }

    private Response buyPackage(Request request) {
        return userService.buyPackage(request);
    }
}