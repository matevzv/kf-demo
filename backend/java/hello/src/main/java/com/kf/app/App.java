package com.kf.app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
*/
public class App {

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/hello_java", new MyHandler());
    server.setExecutor(null);
    server.start();
  }

  static class MyHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        String[] query = t.getRequestURI().toString().split("/");
        String r = "";
        if (query.length < 3) {
            r = "{\"msg\": \"ni imena\"}";
        } else {
            String q = query[2];
            r = "{\"msg\": \"Hello: " + q + "\"}";
        }
        byte [] response = r.getBytes();
        t.sendResponseHeaders(200, response.length);
        OutputStream os = t.getResponseBody();
        os.write(response);
        os.close();
    }
  }
}
