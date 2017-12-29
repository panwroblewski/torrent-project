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
                connector.sendFileThroughSocket(socket, fileToSend, 0);
            } catch (SocketException e) {
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
            connector.saveFileFromSocket(socket, targetFolder);

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
