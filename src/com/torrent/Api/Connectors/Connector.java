package com.torrent.Api.Connectors;

import java.net.Socket;

public interface Connector {

    String getListOfFiles();
    void sendResponse(Socket s, String payload);
    void sendFileThroughSocket(Socket s, int fileNumber);
    void readFromResponse(Socket socket);
}
