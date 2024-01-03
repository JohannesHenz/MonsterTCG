package src.main.java.MTCG.httpserver.server;

public interface Service {
    Response handleRequest(Request request);
}
