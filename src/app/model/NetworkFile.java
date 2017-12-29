package app.model;

import app.common.Utils;

import java.io.File;

public class NetworkFile {
    public String name;
    public File file;
    public String md5;
    public long size;

    public NetworkFile(File file) {
        this.file = file;
        this.name = file.getName();
        this.md5 = Utils.Md5(file);
        this.size = file.length();
    }
}
