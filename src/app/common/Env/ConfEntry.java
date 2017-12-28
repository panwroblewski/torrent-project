package app.common.Env;

public enum ConfEntry {
    DEBUG("DEBUG"),
    MORE_LOGS("MORE_LOGS"),
    TEST_CONNECTION_ERROR("TEST_CONNECTION_ERROR"),
    CONNECTOR_PROTOCOL("CONNECTOR_PROTOCOL"),
    DOWNLOADS_FOLDER_PATH("DOWNLOADS_FOLDER_PATH"),
    ASSETS_FOLDER_PATH("ASSETS_FOLDER_PATH"),
    FILES_FOLDER_PATH("FILES_FOLDER_PATH"),
    HOSTS_CONF_PATH("HOSTS_CONF_PATH");

    private String variable;

    ConfEntry(String variable) {
        this.variable = variable;
    }

    public String toString() {
        return variable;
    }

    public static ConfEntry fromString(String text) {
        for (ConfEntry b : ConfEntry.values()) {
            if (b.variable.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
