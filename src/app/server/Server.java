package app.server;

import app.api.ApiMethod;
import app.api.ApiService;
import app.common.Env.ConfEntry;
import app.common.Env.Env;
import app.common.Logger.Logger;
import app.common.Utils;
import app.model.ClientsList;
import app.model.Host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

    private static int DEFAULT_SERVER_PORT_NUMBER = pickPort();
    private static ApiService apiService = ApiService.getInstance();


    public static void start() throws UnknownHostException, IOException {
        pickPort();

        new Thread(() -> {
            ServerSocket ss;
            try {
                Logger.uiInfo("Server starting on port: " + DEFAULT_SERVER_PORT_NUMBER + "....");
                ss = new ServerSocket(DEFAULT_SERVER_PORT_NUMBER);
                int counter = 0;

                while (true) {
                    Socket socket = ss.accept();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String line = null;


                    while (!socket.isClosed() && (line = in.readLine()) != null) {
                        Logger.log("SERVER-" + socket.getInetAddress() + ":" + socket.getLocalPort(), "received request: " + line);

                        if (line.startsWith(ApiMethod.PUSH.toString())) {
                            apiService.saveFile(socket, Env.getInstance().getConf().get(ConfEntry.DOWNLOADS_FOLDER_PATH));
                        } else if (line.startsWith(ApiMethod.LIST.toString())) {
                            apiService.sendPayload(socket, apiService.getListOfFiles(), true);
                        } else if (line.startsWith(ApiMethod.PULL.toString())) {
                            int fileNumber = Integer.parseInt(line.split(" ")[1]);
                            apiService.sendFile(socket, fileNumber);
                            socket.close();
                        } else if (line.startsWith(ApiMethod.PING.toString())) {
                            apiService.sendPayload(socket, "ok", true);
                        }
                    }

//                    while (!s.isClosed() && (line = in.readLine()) != null) {
//                        Logger.log("SERVER-" + DEFAULT_SERVER_PORT_NUMBER, "received request: " + line);
//
//                        if (line.startsWith(ApiMethod.PUSH.toString())) {
//
//                            //ToDo PUSH IMPLEMENTATION
//                        } else if (line.startsWith(ApiMethod.LIST.toString())) {
//
//                            apiService.getConnector().sendResponse(s, apiService.getListOfFiles(), true);
//
//                        } else if (line.startsWith(ApiMethod.PULL.toString())) {
//                            int fileNumber = Integer.parseInt(line.split(" ")[1]);
////                            in.close();
//                            apiService.getConnector().sendFileThroughSocket(s, apiService.currentHostFilesList.getByIndex(0).file);
//                        } else if (line.startsWith(ApiMethod.PING.toString())) {
//                            apiService.getConnector().sendResponse(s, "ok", true);
//                        }
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//        while (!socket.isClosed() && (line = in.readLine()) != null) {
//            Logger.log("SERVER-" + socket.getInetAddress() + ":" + socket.getPort(), "received request: " + line);
//
//            if (line.startsWith(ApiMethod.PUSH.toString())) {
//                apiService.saveFile(socket, Env.getInstance().getConf().get(ConfEntry.DOWNLOADS_FOLDER_PATH));
//            } else if (line.startsWith(ApiMethod.LIST.toString())) {
//                apiService.sendPayload(socket, apiService.getListOfFiles(), true);
//            } else if (line.startsWith(ApiMethod.PULL.toString())) {
//                int fileNumber = Integer.parseInt(line.split(" ")[1]);
//                apiService.push(socket, fileNumber);
//            } else if (line.startsWith(ApiMethod.PING.toString())) {
//                apiService.sendPayload(socket, "ok", true);
//            }
//        }
    }

    private static int pickPort() {
        for (Host host : ClientsList.getHosts().values()) {
            int port = Integer.parseInt(host.port);
            if (Utils.isPortAvailable(port)) {
                return port;
            }
        }
        System.out.println("There are no free ports available to start server.");
        return 0;
    }

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
