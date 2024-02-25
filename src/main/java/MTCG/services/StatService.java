package MTCG.services;

import MTCG.dal.repository.StatRepository;
import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.models.UserModel;

import java.util.List;
import java.util.stream.Collectors;

public class StatService {

    private StatRepository statRepository;

    public StatService() {
        this.statRepository = new StatRepository();
    }

    public Response getUserStats(Request request) {
        String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\": \"Access token is missing or invalid\"}");
        }

        String username = authorizationHeader.substring(7).split("-")[0];
        UserModel userStats = statRepository.getUserStats(username);
        if (userStats == null) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\": \"User not found\"}");
        }

        String userStatsJson = String.format(
                "{\"Name\": \"%s\", \"Wins\": %d, \"Losses\": %d, \"Elo\": %d}",
                userStats.getUsername(),
                userStats.getWins(),
                userStats.getLosses(),
                userStats.getELO()
        );

        return new Response(HttpStatus.OK, ContentType.JSON, userStatsJson);
    }

    public Response getScoreboard() {
        List<UserModel> scoreboard = statRepository.getScoreboard();
        if (scoreboard == null) {
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\": \"Scoreboard not found\"}");
        }

        String scoreboardJson = scoreboard.stream()
                .map(user -> String.format("{\"Name\": \"%s\", \"Elo\": %d}", user.getUsername(), user.getELO()))
                .collect(Collectors.joining(", ", "[", "]"));

        return new Response
                (HttpStatus.OK,
                ContentType.JSON,
                scoreboardJson);
    }

}