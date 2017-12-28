package app.model;

public class RemoteNetworkFile {
    public int counter;
    public String name;
    public String md5;

    public RemoteNetworkFile(String file) {
        String[] split = file.split(" ");
        this.counter = Integer.parseInt(split[0]);
        this.name = split[1];
        this.md5 = split[2];
    }
}
