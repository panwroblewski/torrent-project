package app.model;

public class DownloadingFile {
    public boolean isDownloading;
    public String name;
    public String targetMD5;
    public int currentSize;
    public int targetSize;
    public byte[] bytes;

    public DownloadingFile(String name, String targetMD5, int targetSize) {
        this.isDownloading = true;
        this.name = name;
        this.targetMD5 = targetMD5;
        this.currentSize = 0;
        this.targetSize = targetSize;
        this.bytes = new byte[targetSize];
    }
}
