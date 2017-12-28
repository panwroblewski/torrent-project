package app.common.Logger;

import app.common.Env.ConfEntry;
import app.common.Env.Env;

public class Logger {

    private static final boolean DEBUG = "true".equalsIgnoreCase(Env.getInstance().getConf().get(ConfEntry.DEBUG));

    public static void debugInfo(String message) {
        if (DEBUG) {
            System.out.println("               DEBUG: " + message);
        }
    }

    public static void errorInfo(String message) {
        if (DEBUG) {
            System.out.println("                      Exception: " + message);
        }
    }

    public static void log(String location, String message) {
        System.out.println("___" + location + ": " + message);
    }

    public static void info(String message) {
        System.out.println(message);
    }

    public static void uiInfo(String message) {
        System.out.println(message);
    }
}
