package app.api.connectors;

import app.Exceptions.FileDownloadedApiException;
import app.model.DownloadingFile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public interface Connector {
    void sendResponse(Socket s, String payload, boolean shouldClose);

    void sendFileThroughSocket(Socket s, File file, int offset) throws IOException;

    void saveFileFromSocket(Socket s, String targetFolder, String targetFileName) throws IOException;

    void saveFileFromSocket(Socket s, String targetFolder, String targetFileName, DownloadingFile downloadingFile) throws IOException, FileDownloadedApiException;

    void readFromResponse(Socket socket);

    String getResponse(Socket socket, boolean shouldClose);

    boolean ping(Socket socket);
}
