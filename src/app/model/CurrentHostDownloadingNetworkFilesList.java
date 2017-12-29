package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CurrentHostDownloadingNetworkFilesList {

    private List<DownloadingFile> files;

    public CurrentHostDownloadingNetworkFilesList() {
        this.files = new ArrayList<>();
    }

    public List<DownloadingFile> getFiles() {
        return this.files;
    }

    public DownloadingFile add(DownloadingFile file) {
        files.add(file);
        return file;
    }

    public DownloadingFile handleDownloadingFile(RemoteNetworkFile remoteNetworkFile) {
        if (isDownloading(remoteNetworkFile)) {
            return filterDownloads(isNotDownloadedAndOnListByName(remoteNetworkFile)).get(0);
        } else {
            return add(new DownloadingFile(remoteNetworkFile));
        }
    }

    public boolean isDownloading(RemoteNetworkFile remoteNetworkFile) {
        return filterDownloads(isNotDownloadedAndOnListByName(remoteNetworkFile)).size() > 0;
    }

    private List<DownloadingFile> filterDownloads(Predicate<DownloadingFile> predicate) {
        return files.stream().filter(predicate).collect(Collectors.toList());
    }

    private Predicate<DownloadingFile> isNotDownloadedAndOnListByName(RemoteNetworkFile remoteNetworkFile) {
        return file -> !file.isDownloaded() && file.name.equals(remoteNetworkFile.name);
    }
}
