package app.model;

import app.api.ApiService;

import java.util.ArrayList;
import java.util.List;

public class RemoteNetworkFilesList {

    private List<RemoteNetworkFile> files;

    public RemoteNetworkFilesList() {}

    public RemoteNetworkFilesList(Host host) {
        refreshList(host);
    }

    public List<RemoteNetworkFile> getFiles() {
        refreshList();
        return this.files;
    }

    public void refreshList(Host host) {
        this.files = parseNetworkFilesListResponse(ApiService.getInstance().getRemoteListOfFiles(host));;
    }

    private void refreshList() {}

    public void add(RemoteNetworkFile file) {
        files.add(file);
    }

    private List<RemoteNetworkFile> parseNetworkFilesListResponse(String response) {
        if (response == null) return null;
        ArrayList<RemoteNetworkFile> files = new ArrayList<>();
        String[] lines = response.split(System.getProperty("line.separator"));
        for (String line : lines) {
            files.add(new RemoteNetworkFile(line));
        }
        return files;
    }
}
