package app.api.connectors;

import app.api.ApiService;
import app.common.Logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ConnectorService {

    private static Connector connector = ApiService.getInstance().getConnector();

    public static void sendFileThroughSocket(Socket socket, File fileToSend) {
        try {
            try {
                connector.sendResponse(socket, fileToSend.getName(), false);
                String response = connector.getResponse(socket);

                if (!response.equals("ok")) {
                    Logger.errorInfo("Something went wrong!!!");
                    return;
                }

                connector.sendFileThroughSocket(socket, fileToSend, 0);
            } catch (SocketException e) {
                /**
                 * This double try catch is for testing purposes only,
                 * it is fo testing errourous connection and restart after
                 * connection closed. Feature to beimplemented.
                 * */
                try {
                    Socket newSocket = new Socket("localhost", 16900);
                    connector.sendFileThroughSocket(newSocket, fileToSend, 5);
                } catch (SocketException e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
            }
        } catch (SocketException e) {
            Logger.errorInfo("Connection closed from: " + socket.getInetAddress() + ":" + socket.getPort());
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saveFileFromSocket(Socket socket, String targetFolder) {
        try {
            String response = connector.getResponse(socket);
            connector.sendResponse(socket, "ok", false);
            connector.saveFileFromSocket(socket, targetFolder, String.valueOf(response));

            return "ok";
        } catch (SocketException e) {
            Logger.errorInfo("Connection closed from: " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (IOException  e) {
            Logger.errorInfo("Cannot pull file from: " + socket.getInetAddress() + ":" + socket.getPort());
            e.printStackTrace();
        }

        return "There was a problem with downloading a file. Check if it is available on given host.";
    }


}
