package app.api;

import app.model.Host;

import java.util.Optional;

public enum ApiMethod {
    LIST("list"),
    PUSH("push"),
    PULL("pull"),
    PING("ping"),
    EXIT("exit");

    private String method;

    ApiMethod(String method) {
        this.method = method;
    }

    public String toString() {
        return method;
    }

    public static boolean isApiMethod(String key) {
        String keyMethod = key.split(" ")[0];
        for (ApiMethod apiMethodMethod : ApiMethod.values()) {
            if (keyMethod.equals(apiMethodMethod.toString())) {
                return true;
            }
        }
        return false;
    }

    public static Optional<Host> getTargetHost(String command) {
        if (command.contains("host")) {
            if (!command.endsWith(" ")) command += " "; //* Api behaviour
            String substring = command.substring(command.indexOf("host"), command.length());
            String substringHostOnly = substring.substring(substring.indexOf("="), substring.indexOf(" "));
            String[] split = substringHostOnly.split(":");
            return Optional.of(new Host(split[0].replaceAll("[=;\\-\\s]",""), split[1].replaceAll("[=;\\-\\s]",""), false));
        }
        return Optional.empty();
    }
}
//show host=localhost:16900