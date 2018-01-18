package app.api.connectors;

import app.Exceptions.FileDownloadedApiException;
import app.api.ApiMethod;
import app.api.ApiRequest;
import app.common.Env.Env;
import app.common.Logger.Logger;
import app.model.DownloadingFile;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class ConnectorTcpImpl implements Connector {

    @Override
    public void sendResponse(Socket s, String payload, boolean shouldClose) {
        try {
            Logger.debugInfo(":::Connector::sendResponse()");

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bufferedWriter.write(payload);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            if (shouldClose) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFileThroughSocket(Socket socket, File fileToSend, int skip) throws IOException {
        Logger.debugInfo(":::Connector::sendFileThroughSocket()");


        final int DEFAULT_BUFFER_SIZE = 2;
        InputStream in = Files.newInputStream(fileToSend.toPath());
        OutputStream out = socket.getOutputStream();

        int counter = 0;
        int count;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        in.skip(skip);
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
            if (Env.isTEST_CONNECTION_ERROR) { //FOR TESTING ERROR IN CONNECTION
                if (counter++ == 3) out.close();
            }
        }

        out.close();
    }

    @Override
    public void saveFileFromSocket(Socket socket, String targetFolder, String targetFileName) throws IOException {
        Logger.debugInfo(":::Connector::saveFileFromSocket()");
        InputStream in = socket.getInputStream();
        int byteToBeRead = -1;

        final File folder = new File(targetFolder);
        File newFile = new File(folder.getAbsolutePath() + File.separator + targetFileName.trim().replaceAll("\n", ""));
        FileOutputStream fs = new FileOutputStream(newFile);
        while ((byteToBeRead = in.read()) != -1) {
            fs.write(byteToBeRead);
        }

        fs.flush();
        fs.close();
    }

    @Override
    public void saveFileFromSocket(Socket socket, String targetFolder, String targetFileName, DownloadingFile downloadingFile) throws IOException, FileDownloadedApiException {
        Logger.debugInfo(":::Connector::saveFileFromSocket()");
        InputStream in = socket.getInputStream();
        int byteToBeRead = -1;

        final File folder = new File(targetFolder);
        File newFile = new File(folder.getAbsolutePath() + File.separator + targetFileName.trim().replaceAll("\n", ""));
        FileOutputStream fs = new FileOutputStream(newFile, !(downloadingFile.currentSize == 0));
        while ((byteToBeRead = in.read()) != -1) {
            fs.write(byteToBeRead);
            downloadingFile.currentSize++;
            Logger.uiInfo("Downloading file " + targetFileName.trim() + ", current size: " + downloadingFile.currentSize);
            if (downloadingFile.isDownloaded()) {
                fs.flush();
                fs.close();
                throw new FileDownloadedApiException("File Downloaded");
            }
        }

        fs.flush();
        fs.close();
    }

    @Override
    public void readFromResponse(Socket socket) {
        try {
            Logger.debugInfo(":::Connector::readFromResponse()");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getResponse(Socket socket, boolean shouldClose) {
        try {
            Logger.debugInfo(":::Connector::getResponse()");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            String response = "";
//            while ((line = reader.readLine()) != null) {
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                if (line.equals(ApiRequest.REQUEST_END_OF_MESSAGE)) break;

//                System.out.println(line);
                response += (line + System.getProperty("line.separator"));
            }
            if (shouldClose) {
                reader.close();
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean ping(Socket socket) {
        Logger.debugInfo(":::Connector::ping()");

        sendResponse(socket, ApiMethod.PING.toString(), false);
        String response = getResponse(socket, true);
        if (response.contains(ApiRequest.REQUEST_OK_MESSAGE)) {
            return true;
        }
        return false;
    }
}
