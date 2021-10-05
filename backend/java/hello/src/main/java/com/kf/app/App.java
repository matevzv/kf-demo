package com.kf.app;

import java.io.*;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpsServer;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import com.sun.net.httpserver.*;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import javax.net.ssl.SSLContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/*
 * a simple static http server
*/
public class App {

  public static void main(String[] args) throws Exception {
    
    // setup the socket address
    InetSocketAddress address = new InetSocketAddress(443);

    // initialise the HTTPS server
    HttpsServer httpsServer = HttpsServer.create(address, 0);
    SSLContext sslContext = SSLContext.getInstance("TLS");

    // initialise the keystore
    char[] password = "password".toCharArray();
    KeyStore ks = KeyStore.getInstance("PKCS12");
    FileInputStream fis = new FileInputStream("/home/matevz/kf-demo/backend/cert/cert.jks");
    ks.load(fis, password);

    // setup the key manager factory
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, password);

    // setup the trust manager factory
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    // setup the HTTPS context and parameters
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
      @Override
      public void configure(HttpsParameters params) {
        try {
          // initialise the SSL context
          SSLContext c = getSSLContext();
          SSLEngine engine = c.createSSLEngine();
          params.setNeedClientAuth(false);
          params.setCipherSuites(engine.getEnabledCipherSuites());
          params.setProtocols(engine.getEnabledProtocols());

          // Set the SSL parameters
          SSLParameters sslParameters = c.getSupportedSSLParameters();
          params.setSSLParameters(sslParameters);

        } catch (Exception ex) {
          System.out.println("Failed to create HTTPS port");
          System.out.println(ex.getMessage());
        }
      }
    });
    System.out.println("Listening on port 443");
    httpsServer.createContext("/hello", new MyHandler());
    httpsServer.createContext("/", new StaticFileHandler("/home/matevz/kf-demo/frontend/kf/dist/kf/"));
    httpsServer.setExecutor(null);
    httpsServer.start();
}

  static class MyHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
      String[] query = t.getRequestURI().toString().split("/");
      String r = "";
      if (query.length < 3) {
        r = "{\"msg\": \"ni imena\"}";
      } else {
        String q = query[2];
        r = "{\"msg\": \"Hello " + q + "\"}";
      }
      byte [] response = r.getBytes();
      t.sendResponseHeaders(200, response.length);
      OutputStream os = t.getResponseBody();
      os.write(response);
      os.close();
    }
  }
}
