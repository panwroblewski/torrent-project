package app.api.connectors;

import java.io.File;
import java.net.Socket;

public interface Connector {
    void sendResponse(Socket s, String payload, boolean shouldClose);

    void sendFileThroughSocket(Socket s, File file);

    void readFromResponse(Socket socket);

    String getResponse(Socket socket);

    boolean ping(Socket socket);
}
