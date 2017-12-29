package app.model;

public class DownloadingFile {
    public boolean isDownloading;
    public String name;
    public String targetMD5;
    public long currentSize;
    public long targetSize;
    public byte[] bytes;

    public DownloadingFile(String name, String targetMD5, int targetSize) {
        this.isDownloading = true;
        this.name = name;
        this.targetMD5 = targetMD5;
        this.currentSize = 0;
        this.targetSize = targetSize;
        this.bytes = new byte[targetSize];
    }

    public DownloadingFile(RemoteNetworkFile remoteNetworkFile) {
        this.isDownloading = true;
        this.name = remoteNetworkFile.name;
        this.targetMD5 = remoteNetworkFile.md5;
        this.currentSize = 0;
        this.targetSize = remoteNetworkFile.size;
        this.bytes = new byte[(int) targetSize];
    }

    public boolean isDownloaded() {
        return !this.isDownloading && (this.currentSize == this.targetSize);
    }
}
