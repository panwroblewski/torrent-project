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

    public static boolean isTCP;
    public static boolean isDEBUG;
    public static boolean isMORE_LOGS;
    public static boolean isTEST_CONNECTION_ERROR;

    private Env(File file) {
        conf = new HashMap<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splitLine = line.split("=");
                    ConfEntry confEntry = ConfEntry.fromString(splitLine[0].trim().replaceAll("[\";]", ""));
                    if (confEntry == null) continue;
                    String confEntryValue = splitLine[1].trim().replaceAll("[\";]", "");
                    setStaticGlobal(confEntry, confEntryValue);
                    conf.put(confEntry, confEntryValue);
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

    public static void setStaticGlobal(ConfEntry confEntry, String value) {
        if (ConfEntry.DEBUG.equals(confEntry)) {
            isDEBUG = Boolean.valueOf(value);
        } else if (ConfEntry.MORE_LOGS.equals(confEntry)) {
            isMORE_LOGS = Boolean.valueOf(value);
        } else if (ConfEntry.CONNECTOR_PROTOCOL.equals(confEntry)) {
            isTCP = "TCP".equalsIgnoreCase(value);
        } else if (ConfEntry.TEST_CONNECTION_ERROR.equals(confEntry)) {
            isTEST_CONNECTION_ERROR = Boolean.valueOf(value);
        }
    }

    public Map<ConfEntry, String> getConf() {
        return conf;
    }
}
