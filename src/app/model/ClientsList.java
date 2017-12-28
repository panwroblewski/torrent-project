package app.model;

import app.api.ApiService;
import app.common.Env.ConfEntry;
import app.common.Env.Env;
import app.common.Logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientsList {

    private static Map<Integer, Host> hosts;

    private ClientsList(File file) {
        hosts = new HashMap<>();
        int counter = 0;
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splitLine = line.split(":");
                    hosts.put(counter++, new Host(splitLine[0], splitLine[1], false));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Host> getHosts() {
        if (hosts == null) {
            new ClientsList(new File(Env.getInstance().getConf().get(ConfEntry.HOSTS_CONF_PATH)));
        }
        return hosts;
    }

    public static void listAllAvaliable() {
        Logger.uiInfo("Available hosts: ");
        for (Host host : ClientsList.getHosts().values()) {
            boolean pingResult = ApiService.ping(host);
            host.isOnline = pingResult;
            Logger.uiInfo(host.ip + ":" + host.port + " isOnline: " + pingResult);
            if (host.isOnline) {
                Logger.uiInfo("Has files:");
                host.lisfAvailableFiles();
            }
        }
    }

    public static Optional<Host> getFirstAvailable() {
        return hosts.values().stream().filter(host -> host.isOnline).findFirst();
    }
}
