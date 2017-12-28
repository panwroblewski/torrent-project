package app.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentHostDownloadingNetworkFilesList {

    private List<DownloadingFile> files;

    public CurrentHostDownloadingNetworkFilesList() {
        this.files = new ArrayList<>();
    }

    public List<DownloadingFile> getFiles() {
        return this.files;
    }

    public void add(DownloadingFile file) {
        files.add(file);
    }
}
