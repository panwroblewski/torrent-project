package app.model;

import java.util.List;

public class LocalDownloadingNetworkFilesList {

    private List<DownloadingFile> files;

    public List<DownloadingFile> getFiles() {
        return this.files;
    }

    public void add(DownloadingFile file) {
        files.add(file);
    }
}
