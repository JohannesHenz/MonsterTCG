package MTCG.httpserver.server;

public interface Service {
    Response handleRequest(Request request);
}
