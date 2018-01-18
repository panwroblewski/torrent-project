package app.common.Logger;

import app.common.Env.Env;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Logger {
    final static File folder = new File(".");
    public final static File APPLICATION_LOG = new File(folder.getAbsolutePath() + File.separator + "applicationLog.txt");
    static FileOutputStream fs = getLoggerFileOutputStream();

    public static void debugInfo(String message) {
        if (Env.isDEBUG) {
            String msg = "               DEBUG: " + message;
            System.out.println(msg);
            writeToLogs(msg);
        }
    }

    public static void errorInfo(String message) {
        if (Env.isDEBUG) {
            String msg = "                      Exception: " + message;
            System.out.println(msg);
            writeToLogs(msg);
        }
    }

    public static void log(String location, String message) {
        if (Env.isMORE_LOGS) {
            String msg = "___" + location + ": " + message;
            System.out.println(msg);
            writeToLogs(msg);
        }
    }

    public static void info(String message) {
        System.out.println(message);
        writeToLogs(message);
    }

    public static void uiInfo(String message) {
        System.out.println(message);
        writeToLogs(message);
    }

    public static void uiInfoSameLine(String message) {
        System.out.print(message + "\r");
        writeToLogs(message);
    }

    private static FileOutputStream getLoggerFileOutputStream() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(APPLICATION_LOG, true);
            fileOutputStream.write(("\n--\n--\n_____________NEW SESSION STARTED   " + new Date() + "____________\n--\n--\n").getBytes());
            return fileOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeToLogs(String message) {
        try {
            fs.write((message + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
