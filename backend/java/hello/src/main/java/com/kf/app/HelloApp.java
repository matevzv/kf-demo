package com.kf.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
 
public class HelloApp {
 
    public static void main(String[] args) throws Exception {

        int tcpPort = 443;
        String pwdPath = System.getProperty("user.dir");
        
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        Server server = new Server(threadPool);
        
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(pwdPath + "/cert/cert.jks");
        sslContextFactory.setKeyStorePassword("password");
        sslContextFactory.setKeyManagerPassword("password");

        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        SecureRequestCustomizer secureRequestCustomizer = new SecureRequestCustomizer();
        httpsConfiguration.addCustomizer(secureRequestCustomizer);

        ServerConnector serverConnector = new ServerConnector(server,
            new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
            new HttpConnectionFactory(httpsConfiguration));
        serverConnector.setPort(tcpPort);

        server.setConnectors(new Connector[] { serverConnector });

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");

        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
        holderPwd.setInitParameter("resourceBase", pwdPath + "/frontend/kf/dist/kf/");

        ctx.addServlet(holderPwd, "/*");
        ctx.addServlet(HelloServlet.class, "/hello");
        ctx.addServlet(HelloServlet2.class, "/hello/*");

        server.setHandler(ctx);
         
        server.start();
        server.join();
    }
     
    public static class HelloServlet extends HttpServlet 
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            String res = "{\"msg\": \"ni imena\"}";
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(res); 
        } 
    }

    public static class HelloServlet2 extends HttpServlet 
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            String[] query = request.getRequestURI().toString().split("/");
            String res = "";
            
            if (query.length < 3) {
                res = "{\"msg\": \"ni imena\"}";
            } else {
                String q = query[2];
                res = "{\"msg\": \"Hello " + q + "\"}";
            }
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(res); 
        } 
    }
 }
