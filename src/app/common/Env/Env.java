package app.common.Env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Env {

    private static Env ourInstance;
    private Map<ConfEntry, String> conf;

    private Env(File file) {
        conf = new HashMap<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splitLine = line.split("=");
                    ConfEntry confEntry = ConfEntry.fromString(splitLine[0].trim().replaceAll("[\";]", ""));
                    if (confEntry == null) continue;
                    conf.put(confEntry, splitLine[1].trim().replaceAll("[\";]", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Env getInstance() {
        if (ourInstance == null) {
            System.out.println(new File("./").getAbsolutePath());
            ourInstance = new Env(new File("./src/app/conf/application.conf"));
        }
        return ourInstance;
    }

    public Map<ConfEntry, String> getConf() {
        return conf;
    }
}
