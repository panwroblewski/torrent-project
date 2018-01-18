package app.model;

public class Host {

    public String ip;
    public String port;
    public boolean isOnline;
    public RemoteNetworkFilesList remoteNetworkFilesList;

    public Host(String ip, String port, boolean isOnline) {
        this.ip = ip;
        this.port = port;
        this.isOnline = isOnline;
        this.remoteNetworkFilesList = new RemoteNetworkFilesList(this);
    }

    public Host(String ip, String port) {
        this.ip = ip;
        this.port = port;
        this.isOnline = true;
        this.remoteNetworkFilesList = null;
    }

    public void lisfAvailableFiles() {
        remoteNetworkFilesList.refreshList(this);

        for (RemoteNetworkFile remoteNetworkFile : remoteNetworkFilesList.getFiles()) {
            System.out.println(remoteNetworkFile.counter + " "
                    + remoteNetworkFile.name + " "
                    + remoteNetworkFile.md5 + " "
                    + remoteNetworkFile.size);
        }
    }

}
