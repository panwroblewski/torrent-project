package com.torrent;

import com.torrent.Client.Client;
import com.torrent.Server.Server;

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
