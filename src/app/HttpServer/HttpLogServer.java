package app.HttpServer;

import app.common.Logger.Logger;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class HttpLogServer {

    public static int DEFAULT_HTTP_SERVER_PORT_NUMBER = 17000;

    public static void start() throws UnknownHostException, IOException {

        new Thread(() -> {

            HttpServer server = null;
            try {
                server = HttpServer.create(new InetSocketAddress(DEFAULT_HTTP_SERVER_PORT_NUMBER), 0);

                Logger.log("HttpServer", "started at port: " + DEFAULT_HTTP_SERVER_PORT_NUMBER);
                server.createContext("/", new RootHandler());
                server.createContext("/log", new LogHandler());
                server.setExecutor(null);
                server.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }


    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
