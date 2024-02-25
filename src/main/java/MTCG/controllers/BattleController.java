package MTCG.controllers;

import MTCG.httpserver.http.ContentType;
import MTCG.httpserver.http.HttpStatus;
import MTCG.httpserver.http.Method;
import MTCG.httpserver.server.Request;
import MTCG.httpserver.server.Response;
import MTCG.httpserver.server.Service;
import MTCG.services.BattleService;
import MTCG.utils.BattlePool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BattleController implements Service {
    private final BattlePool battlePool;
    private BattleService battleService;
    private ExecutorService battleExecutor;


    public BattleController(BattlePool battlePool, BattleService battleService) {
        this.battlePool = battlePool;
        this.battleService = battleService;
        this.battleExecutor = Executors.newFixedThreadPool(10);
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST && request.getServiceRoute().equals("/battles")) {
            String authorizationHeader = request.getHeaderMap().getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{'message': 'Access token is missing or invalid.'}");
            }
            if (battlePool.hasTwoPlayers()) {
                String[] players = battlePool.getTwoPlayers();
                return battleService.battle(players[0], players[1]);
            }
            else {
                String username = request.getHeaderMap().getHeader("Authorization").substring(7).split("-")[0];
                battlePool.addPlayer(username);
                return new Response(HttpStatus.OK, ContentType.JSON, "{'message': 'Waiting for another player'}");
            }
        }

            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{'error': 'Battle Not found'}"
            );


    }
}