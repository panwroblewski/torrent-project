package com.torrent.Api;

public enum Api {
    SHOW("show"),
    ADD("add"),
    FILE("file"),
    EXIT("exit");

    private String method;

    Api(String method) {
        this.method = method;
    }

    public String toString() {
        return method;
    }

    public static boolean isApiMethod(String key) {
        String keyMethod = key.split(" ")[0];
        for (Api apiMethod : Api.values()) {
            if (keyMethod.equals(apiMethod.toString())) {
                return true;
            }
        }
        return false;
    }
}
