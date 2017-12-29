package app.server;

import app.api.ApiMethod;
import app.api.ApiRequest;
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

                while (true) {
                    Socket socket = ss.accept();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String line = null;

                    while (!socket.isClosed() && (line = in.readLine()) != null) {
                        Logger.log("SERVER-" + socket.getInetAddress() + ":" + socket.getLocalPort(), "received request: " + line);
                        ApiRequest apiRequest = ApiRequest.parseCommand(line);

                        if (apiRequest.type.equals(ApiMethod.PUSH)) {
                            apiService.saveFile(socket, Env.getInstance().getConf().get(ConfEntry.DOWNLOADS_FOLDER_PATH));
                        } else if (apiRequest.type.equals(ApiMethod.LIST)) {
                            apiService.sendPayload(socket, apiService.getListOfFiles(), true);
                        } else if (apiRequest.type.equals(ApiMethod.PULL)) {
                            apiService.sendFile(socket, Integer.parseInt(apiRequest.fileNumber));
                            socket.close();
                        } else if (apiRequest.type.equals(ApiMethod.PING)) {
                            apiService.sendPayload(socket, "ok", true);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static int pickPort() {
        for (Host host : ClientsList.getHosts().values()) {
            int port = Integer.parseInt(host.port);
            if (Utils.isPortAvailable(port)) {
                return port;
            }
        }
        Logger.uiInfo("There are no free ports available to start server.");
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
