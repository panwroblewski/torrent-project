package com.torrent.Client;

import com.torrent.Api.Api;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class Client {

    private static int DEFAULT_CLIENT_PORT_NUMBER = 16900;

    public static void start() throws UnknownHostException, IOException {

        new Thread(() -> {
            try {
                Scanner sc = new Scanner(System.in);

                while (true) {

                    System.out.println("Wybierz komendę:");
                    String command = sc.nextLine();

                    if (Api.isApiMethod(command)) {
                        Socket socket = new Socket("localhost", DEFAULT_CLIENT_PORT_NUMBER);
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        out.write(command);
                        out.newLine();
                        out.flush();



                        if (command.equals(Api.SHOW.toString())) {
                            readFromResponse(socket);
                        } else if (command.equals(Api.ADD.toString()) || command.startsWith(Api.FILE.toString())) {

                            if (command.startsWith(Api.FILE.toString())) {

                                InputStream in = socket.getInputStream();
                                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int byteToBeRead = -1;


                                final File folder = new File("./Downloads");
                                File newFile = new File(folder.getAbsolutePath() + File.separator + new Date().getTime());
                                FileOutputStream fs = new FileOutputStream(newFile);
                                while ((byteToBeRead = in.read()) != -1) {
                                    //System.out.println(byteToBeRead);
                                    fs.write(byteToBeRead);
                                }

                                fs.flush();
                                fs.close();
                            }

//                            socket.close();

                        } else if (command.equals(Api.EXIT.toString())) {
                            System.out.println(":::CLIENT::close");
                            break;
                        }

                        Thread.sleep(200);
                    }

                }

                sc.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void readFromResponse(Socket socket) {
        try {
            System.out.println(":::CLIENT::readFromResponse()");
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

    public static void main(String[] args) {

        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
