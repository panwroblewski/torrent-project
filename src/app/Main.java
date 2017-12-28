package app;

import app.client.Client;
import app.server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Server.start();
            Client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
