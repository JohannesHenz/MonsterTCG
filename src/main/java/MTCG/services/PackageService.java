package MTCG.services;

import MTCG.dal.repository.repoUOWs.PackageUOW;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.models.CardModel;
import MTCG.models.PackageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class PackageService {

    private PackageUOW packageUOW = new PackageUOW();

    public Response createPackage(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid'}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        if (!"admin".equals(username)) {
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{'message': 'Provided user is not admin'}");
        }

        String requestBody = request.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<CardModel> cards = new ArrayList<>();

        try {
            cards = mapper.readValue(requestBody, new TypeReference<List<CardModel>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{'message': 'Invalid JSON format'}");
        }

        String packageId = UUID.randomUUID().toString();
        PackageModel packageModel = new PackageModel(packageId, cards);

        boolean isCreated = packageUOW.createPackage(packageModel);
        if (isCreated) {
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{'message': 'Package and cards successfully created'}");
        } else {
            return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{'message': 'At least one card in the packages already exists'}");
        }
    }
}