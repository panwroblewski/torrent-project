package app.api.connectors;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public interface Connector {
    void sendResponse(Socket s, String payload, boolean shouldClose);

    void sendFileThroughSocket(Socket s, File file, int offset) throws IOException;

    void saveFileFromSocket(Socket s, String targetFolder) throws IOException;

    void readFromResponse(Socket socket);

    String getResponse(Socket socket);

    boolean ping(Socket socket);
}
