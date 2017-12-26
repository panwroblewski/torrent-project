package com.torrent.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NetworkFilesList {

    private static NetworkFilesList instance = null;
    private List<File> files;

    private NetworkFilesList(List<File> files) {
        this.files = files;
    }

    public static NetworkFilesList getInstance() {
        if (instance == null) {
            File folder = new File("./Assets/TestFiles");
            File[] listOfFiles = folder.listFiles();
            ArrayList<File> listedFiles = new ArrayList<>();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    listedFiles.add(listOfFiles[i]);
//                    System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
//                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }

            instance = new NetworkFilesList(listedFiles);
        }
        return instance;
    }

    public List<File> getFiles() {
        return this.files;
    }
}
