package app.api;

import app.api.connectors.Connector;
import app.api.connectors.ConnectorService;
import app.api.connectors.ConnectorTcpImpl;
import app.common.Env.ConfEntry;
import app.common.Env.Env;
import app.common.Logger.Logger;
import app.model.CurrentHostDownloadingNetworkFilesList;
import app.model.CurrentHostNetworkFilesList;
import app.model.Host;
import app.model.NetworkFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ApiService {

    private static ApiService instance;
    private static Connector connector;
    private CurrentHostNetworkFilesList currentHostFilesList;
    private CurrentHostDownloadingNetworkFilesList currentHostDownloadingNetworkFilesList;

    private ApiService() {
        if (Env.isTCP) {
            connector = new ConnectorTcpImpl();
        } else {
            Logger.uiInfo("Protocol not yet implemented: " + Env.getInstance().getConf().get(ConfEntry.CONNECTOR_PROTOCOL));
        }
        currentHostFilesList = new CurrentHostNetworkFilesList();
        currentHostDownloadingNetworkFilesList = new CurrentHostDownloadingNetworkFilesList();
    }

    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public Connector getConnector() {
        return connector;
    }

    public CurrentHostDownloadingNetworkFilesList getCurrentHostDownloadingNetworkFilesList() {
        return currentHostDownloadingNetworkFilesList;
    }

    public static boolean ping(Host host) {
        Logger.debugInfo(":::ApiService::ping(), host: " + host.ip + ":" + host.port);
        try {
            Socket socket = new Socket(host.ip, Integer.parseInt(host.port));

            return connector.ping(socket);
        } catch (IOException e) {
            System.out.println(host.port);
            return false;
        }
    }

    public String getListOfFiles() {
        Logger.debugInfo(":::ApiService::getListOfFiles()");
        int counter = 0;
        StringBuilder result = new StringBuilder();
        List<NetworkFile> files = currentHostFilesList.getLocalFiles();
        if (files == null) {
            return "There are no files available";
        }
        for (NetworkFile file : files) {
            result.append(counter++ + " " + file.name + " " + file.md5 + " " + file.size + System.getProperty("line.separator"));
        }
        return result.toString();
    }

    public String getRemoteListOfFiles(Host host) {
        Logger.debugInfo(":::ApiService::getRemoteListOfFiles()");
        StringBuilder result = new StringBuilder();
        try {
            Socket socket = establishConnection(host);
            connector.sendResponse(socket, ApiMethod.LIST.toString(), false);
            return connector.getResponse(socket);
        } catch (IOException e) {
            Logger.errorInfo("Cannot get list of files from host: " + host.ip + ":" + host.port);
            return null;
        }
    }

    public String push(Socket socket, int fileNumber) {
        Logger.debugInfo(":::ApiService::push()");
        try {
            sendFile(socket, fileNumber);
            return "ok";
        } catch (FileNotFoundException e) {
            Logger.info(e.getMessage());
        }
        return "There was a problem with downloading a file. Check if it is available on given host.";
    }

    public void sendFile(Socket socket, int fileNumber) throws FileNotFoundException {
        Logger.debugInfo(":::ApiService::sendFile()");
        try {
            NetworkFile fileToSend = currentHostFilesList.getByIndex(fileNumber);
            if (fileToSend != null) {
                ConnectorService.sendFileThroughSocket(socket, fileToSend.file);
                return;
            }
            throw new FileNotFoundException("Cannot locate file with index: " + fileNumber);
        } catch (IOException e) {
            Logger.errorInfo(e.getMessage() + " " + "Cannot push file to host: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
    }

    public void saveFile(Socket socket, String targetFolder) {
        Logger.debugInfo(":::ApiService::saveFileFromSocket()");

        ConnectorService.saveFileFromSocket(socket, targetFolder);
    }

    public Socket sendPayload(Socket socket, String payload, boolean shouldClose) throws IOException {
        Logger.debugInfo(":::ApiService::sendPayload()");
        connector.sendResponse(socket, payload, shouldClose);
        return socket;
    }

    public void readFromResponse(Socket socket) throws IOException {
        Logger.debugInfo(":::ApiService::readFromResponse()");
        connector.readFromResponse(socket);
    }

    public String getResponse(Socket socket) throws IOException{
        Logger.debugInfo(":::ApiService::getResponse()");
        return connector.getResponse(socket);
    }

    public static Socket establishConnection(Host host) throws IOException {
        return new Socket(host.ip, Integer.parseInt(host.port));
    }

//    public void handleDownloadingFile(String command) {
//        currentHostDownloadingNetworkFilesList.handleDownloadingFile();
//    }

}
