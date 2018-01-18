package app.api.connectors;

import app.Exceptions.FileDownloadedApiException;
import app.api.ApiRequest;
import app.api.ApiService;
import app.common.Logger.Logger;
import app.model.ClientsList;
import app.model.DownloadingFile;
import app.model.Host;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ConnectorService {

    private static ApiService apiService = ApiService.getInstance();
    private static Connector connector = apiService.getConnector();

    public static void sendFileThroughSocket(Socket socket, File fileToSend, boolean local, String retransmitToIp, String retransmitToPort) {
        Logger.debugInfo(":::ConnectorService::sendFileThroughSocket()");

        try {
            try {
                int dowloadFromByte;
                connector.sendResponse(socket, fileToSend.getName() + "\n" + ApiRequest.REQUEST_END_OF_MESSAGE, false);
                String response = connector.getResponse(socket, false);

                if (!response.contains(ApiRequest.REQUEST_OK_MESSAGE)) {
                    Logger.errorInfo("Something went wrong on server side!");
                    return;
                }
                if (response.contains(ApiRequest.REQUEST_DOWNLOAD_FILE_DOWNLOADED)) {
                    Logger.debugInfo("File downloaded, closing connection.");
                    return;
                }
                dowloadFromByte = ApiRequest.getDownloadFromByteFromResponse(response);

                connector.sendFileThroughSocket(socket, fileToSend, dowloadFromByte);
            } catch (SocketException e) {
                /**
                 * This double try catch is for testing purposes only,
                 * it is fo testing errourous connection and restart after
                 * connection closed. Feature to be implemented.
                 * */
                resendFile(fileToSend, retransmitToIp, Integer.parseInt(retransmitToPort));
            }
        } catch (SocketException e) {
            Logger.errorInfo("Connection closed from: " + socket.getInetAddress() + ":" + socket.getPort());
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resendFile(File fileToSend, String ip, int port) {
        Logger.debugInfo(":::ConnectorService::resendFile(" +fileToSend.getName() + ", " + ip + ", " + port +")");
        try {
            int dowloadFromByte;
            Socket newSocket = new Socket(ip, port);
            ApiRequest apiRequest = ApiRequest.parseCommand("push file=0");
            ApiService.getInstance().sendPayload(newSocket, apiRequest.command, false);
            connector.sendResponse(newSocket, fileToSend.getName() + "\n" + ApiRequest.REQUEST_END_OF_MESSAGE, false);
            String response = connector.getResponse(newSocket, false);
            if (!response.contains(ApiRequest.REQUEST_OK_MESSAGE)) {
                Logger.errorInfo("Something went wrong on server side!");
                return;
            }
            if (response.contains(ApiRequest.REQUEST_DOWNLOAD_FILE_DOWNLOADED)) {
                Logger.debugInfo("File downloaded, closing connection.");
                return;
            }
            dowloadFromByte = ApiRequest.getDownloadFromByteFromResponse(response);

            connector.sendFileThroughSocket(newSocket, fileToSend, dowloadFromByte);
        } catch (IOException e) {
            resendFile(fileToSend, ip, port);
        }
    }

    public static String saveFileFromSocket(Socket socket, String targetFolder, boolean local) {
        Logger.debugInfo(":::ConnectorService::saveFileFromSocket()");
        try {
            Thread.sleep(100); //Todo tutaj czasami jest jakiś wyścig/wywłaszczanie (przy retransmisji nie przechodzi do końca NIE ZAWSZE!)
            String response = connector.getResponse(socket, false);

            DownloadingFile downloadingFile = apiService.getCurrentHostDownloadingNetworkFilesList()
                    .getDownloadingFile(ClientsList.getRemoteNetworkFileByNameFromHost(
                            response.trim().replaceAll("\n", ""),
                            new Host(getIp(socket, local), getPort(socket, local))
                    ));

            notifyFinishDownload(socket, downloadingFile);

            connector.sendResponse(socket, ApiRequest.REQUEST_OK_MESSAGE + "&&" + ApiRequest.REQUEST_DOWNLOAD_FROM_BYTE + downloadingFile.currentSize + "\n" + ApiRequest.REQUEST_END_OF_MESSAGE, false);

            try {
                connector.saveFileFromSocket(socket, targetFolder, String.valueOf(response), downloadingFile);
            } catch (FileDownloadedApiException e) {
                notifyFinishDownload(socket, downloadingFile);
            }

            return ApiRequest.REQUEST_OK_MESSAGE;
        } catch (SocketException e) {
            Logger.errorInfo("Connection closed from: " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (IOException  e) {
            Logger.errorInfo("Cannot pull file from: " + socket.getInetAddress() + ":" + socket.getPort());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "There was a problem with downloading a file. Check if it is available on given host.";
    }

    public static void notifyFinishDownload(Socket socket, DownloadingFile downloadingFile) {
        if (downloadingFile.isDownloaded()) {
            Logger.uiInfo("Client downloaded file: " + downloadingFile.name);
            downloadingFile.isDownloading = false;
            connector.sendResponse(socket, ApiRequest.REQUEST_OK_MESSAGE + "\n" + ApiRequest.REQUEST_DOWNLOAD_FILE_DOWNLOADED + "\n" + ApiRequest.REQUEST_END_OF_MESSAGE, true);
            if (downloadingFile.checksum()) {
                Logger.uiInfo("Checksum for file: " + downloadingFile.name + "ok, file is safe.");
            } else {
                Logger.uiInfo("File " + downloadingFile.name + "is corrupt, checksums do not match!!!");
            }

        }
    }

    public static String getIp(Socket socket, boolean local) {
        return String.valueOf(socket.getInetAddress()).contains("localhost") ||  String.valueOf(socket.getInetAddress()).contains("127.0.0.1") ? "localhost" :
                String.valueOf(socket.getInetAddress()).contains("/") ? String.valueOf(socket.getInetAddress()).split("/")[1] :
                        String.valueOf(socket.getInetAddress());
    }

    public static String getPort(Socket socket, boolean local) {
        return local ? String.valueOf(socket.getLocalPort()) : String.valueOf(socket.getPort());
    }


}
