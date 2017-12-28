package app.model;

import app.common.Env.ConfEntry;
import app.common.Env.Env;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CurrentHostNetworkFilesList extends RemoteNetworkFilesList {


    private List<NetworkFile> files;

    public CurrentHostNetworkFilesList() {
        refreshList();
    }

    public List<NetworkFile> getLocalFiles() {
        refreshList();
        return this.files;
    }

    private void refreshList() {
        File folder = new File(Env.getInstance().getConf().get(ConfEntry.FILES_FOLDER_PATH));
        File[] listOfFiles = folder.listFiles();
        ArrayList<NetworkFile> listedFiles = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listedFiles.add(new NetworkFile(listOfFiles[i]));
//                    System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
//                    System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        this.files = listedFiles;
    }

    public NetworkFile getByIndex(int index) {
        return files.get(index);
    }

}
