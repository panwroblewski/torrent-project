package com.torrent.Server;

import com.torrent.Api.Api;
import com.torrent.Common.NetworkFilesList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.List;

public class Server {

    private static int DEFAULT_SERVER_PORT_NUMBER = 16900;

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() throws UnknownHostException, IOException {

        new Thread(() -> {
            ServerSocket ss;
            try {
                ss = new ServerSocket(DEFAULT_SERVER_PORT_NUMBER);
                int counter = 0;

                while (true) {
                    Socket s = ss.accept();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
                    String line = null;

                    while (!s.isClosed() && (line = in.readLine()) != null) {
                        if (line.equals(Api.ADD.toString())) {
                            counter++;
                            System.out.println("SERVER: " + line + " counter=" + counter);
                        } else if (line.equals(Api.SHOW.toString())) {

                            sendResponse(s, getListOfFiles());

                        } else if (line.startsWith(Api.FILE.toString())) {
                            int fileNumber = Integer.parseInt(line.split(" ")[1]);
//                            in.close();
                            sendFileThroughSocket(s, fileNumber);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static String getListOfFiles() {
        int counter = 0;
        StringBuilder result = new StringBuilder();
        List<File> files = NetworkFilesList.getInstance().getFiles();
        if (files == null) {
            return "There are no files available";
        }
        for (File file : files) {
            result.append(counter++ + " " + file.getName() +"\n");
        }
        return result.toString();
    }

    private static void sendResponse(Socket s, String payload) {
        try {
            System.out.println(":::SERVER::sendResponse()");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            bufferedWriter.write(payload);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendFileThroughSocket(Socket s, int fileNumber) {
        try {
            System.out.println(":::SERVER::sendFileThroughSocket()");
            final File folder = new File("./Assets/TestFiles");
            File fileToSend = folder.listFiles()[fileNumber];

            InputStream in = Files.newInputStream(fileToSend.toPath());
            OutputStream out = s.getOutputStream();

            int count;
            byte[] buffer = new byte[8192];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            out.close();
        } catch (Exception exc) {
            System.out.println(exc);
        }
    }
}
