package app.common.Logger;

import app.common.Env.Env;

public class Logger {

    public static void debugInfo(String message) {
        if (Env.isDEBUG) {
            System.out.println("               DEBUG: " + message);
        }
    }

    public static void errorInfo(String message) {
        if (Env.isDEBUG) {
            System.out.println("                      Exception: " + message);
        }
    }

    public static void log(String location, String message) {
        if (Env.isMORE_LOGS) {
            System.out.println("___" + location + ": " + message);
        }
    }

    public static void info(String message) {
        System.out.println(message);
    }

    public static void uiInfo(String message) {
        System.out.println(message);
    }
}
