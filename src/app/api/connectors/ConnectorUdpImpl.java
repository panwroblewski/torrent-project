package app.api.connectors;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ConnectorUdpImpl implements Connector {


    @Override
    public void saveFileFromSocket(Socket s, String targetFolder) throws IOException {
        //ToDo
    }

    @Override
    public void sendResponse(Socket s, String payload, boolean shouldClose) {
        //ToDo
//        try {
//            Logger.debugInfo(":::Connector::sendResponse()");
//
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//            bufferedWriter.write(payload);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//            if (shouldClose) {
//                bufferedWriter.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void sendFileThroughSocket(Socket s, File fileToSend, int offset) throws IOException {
        //ToDo
//        try {
//            DatagramSocket datagramSocket = new DatagramSocket(s.getLocalPort(), s.getInetAddress());
//            Logger.debugInfo(":::Connector::sendFileThroughSocket()");
//
//            InputStream in = Files.newInputStream(fileToSend.toPath());
//            OutputStream out = s.getOutputStream();
//
//            int count;
//            byte[] buffer = new byte[8192];
//            while ((count = in.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }
//
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void readFromResponse(Socket socket) {
        //ToDo
//        try {
//            Logger.debugInfo(":::Connector::readFromResponse()");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public String getResponse(Socket socket) {
        //ToDo
//        try {
//            Logger.debugInfo(":::Connector::getResponse()");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String line;
//            String response = "";
//            while ((line = reader.readLine()) != null) {
//               response += (line + System.getProperty("line.separator"));
//            }
//            reader.close();
//            return response;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public boolean ping(Socket socket) {
        //ToDo
//        Logger.debugInfo(":::Connector::ping()");
//
//        sendResponse(socket, ApiMethod.PING.toString(), false);
//        String response = getResponse(socket);
//        if (response.contains("ok")) {
//             return true;
//        }
        return false;
    }
}
