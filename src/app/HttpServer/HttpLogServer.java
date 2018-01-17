package app.HttpServer;

import app.api.ApiMethod;
import app.api.ApiRequest;
import app.api.ApiService;
import app.common.Env.ConfEntry;
import app.common.Env.Env;
import app.common.Logger.Logger;
import app.model.ClientsList;
import app.model.Host;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Scanner;

public class HttpServer {

    private static int DEFAULT_CLIENT_PORT_NUMBER = 17000;
    private static ApiService apiService = ApiService.getInstance();

    public static void start() throws UnknownHostException, IOException {

        new Thread(() -> {
            try {
                ClientsList.listAllAvaliable();

                Scanner scanner = new Scanner(System.in);

                while (true) {

                    Logger.uiInfo("Choose command:");

                    String command = scanner.nextLine();

                    if (ApiMethod.isApiMethod(command)) {
                        ApiRequest apiRequest = ApiRequest.parseCommand(command);

                        Optional<Host> host = apiRequest.host != null
                                ? Optional.of(apiRequest.host)
                                : ClientsList.getFirstAvailable();

                        if (host.isPresent()) {
                            Socket socket = ApiService.establishConnection(host.get());
                            Logger.log("_CLIENT:" + socket.getInetAddress() + ":" + socket.getPort(), command + " to host: " + host.get().ip + ":" + host.get().port);
                            apiService.sendPayload(socket, apiRequest.command, false);

                            if (apiRequest.type.equals(ApiMethod.LIST)) {
                                apiService.readFromResponse(socket);
                            } else if (apiRequest.type.equals(ApiMethod.PUSH)) {
                                apiService.push(socket, Integer.parseInt(apiRequest.fileNumber));
                            } else if (apiRequest.type.equals(ApiMethod.PULL)) {
                                apiService.saveFile(socket, Env.getInstance().getConf().get(ConfEntry.DOWNLOADS_FOLDER_PATH), false);
                            } else if (apiRequest.type.equals(ApiMethod.EXIT)) {
                                Logger.log("CLIENT", ApiMethod.EXIT.toString());
                                break;
                            }
//                            socket.close();
                            Thread.sleep(200);
                        } else {
                            Logger.uiInfo("Host is not available");
                        }
                    } else {
                        Logger.uiInfo("Command: " + command + " is unknown");
                    }

                }

                scanner.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }


    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
