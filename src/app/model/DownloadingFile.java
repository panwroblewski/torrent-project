package app.model;

import app.common.Env.ConfEntry;
import app.common.Env.Env;
import app.common.Logger.Logger;
import app.common.Utils;

import java.io.File;

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
        return /*!this.isDownloading && */(this.currentSize == this.targetSize);
    }

    public boolean checksum() {
        File file = new File(Env.getInstance().getConf().get(ConfEntry.DOWNLOADS_FOLDER_PATH) + File.separator + this.name);
        Logger.uiInfo("Checksum... ");
        String newMd5 = Utils.Md5(file);
        Logger.uiInfo("Downloaded file: " + newMd5 + ", remote file: " + this.targetMD5);
        return this.targetMD5.equals(newMd5);
    }

}
