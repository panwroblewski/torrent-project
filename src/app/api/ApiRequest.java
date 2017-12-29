package app.api;

import app.model.Host;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiRequest {

    private static final Pattern API_COMMAND_PATTERN =
            Pattern.compile("(list|push|pull|ping|exit)\\s*(file=\\d+)?\\s*(host=[a-zA-Z0-9]*:[\\d]*)?\\s*(downloadFromByte=\\d+)?\\s*");
    private static final String REQUEST_OPTION_ENTRY_VALUE_SEPARATOR = "=";
    private static final String REQUEST_OPTION_INET_HOST_SEPARATOR = ":";

    public String command;
    public ApiMethod type;
    public Host host;
    public String fileNumber;
    public String downloadFromByte;

    public ApiRequest(String command, ApiMethod type, Host host, String fileNumber, String downloadFromByte) {
        this.command = command;
        this.type = type;
        this.host = host;
        this.fileNumber = fileNumber;
        this.downloadFromByte = downloadFromByte;
    }

    public static ApiRequest parseCommand(String command) {
        Matcher m = API_COMMAND_PATTERN.matcher(command);
        ApiMethod type = null;
        Optional<Host> host = Optional.empty();
        String file = null;
        String downloadFromByte = null;

        while (m.find()) {
            if (m.group(1) != null) {
                type = ApiMethod.fromString(m.group(1));
            }
            if (m.group(2) != null) {
                file = parseFileNumber(m.group(2));
            }
            if (m.group(3) != null) {
                host = getTargetHost(m.group(3));
            }
            if (m.group(4) != null) {
                downloadFromByte = m.group(4);
            }
        }

//        if (type == null) throw new Exception("ApiException: Unknown API method type in: " + command);

        return new ApiRequest(
                command,
                type,
                host.isPresent() ? host.get() : null,
                file,
                downloadFromByte
        );
    }

    public static Optional<Host> getTargetHost(String command) {
        if (command.contains("host")) {
            if (!command.endsWith(" ")) command += " "; //* Api behaviour
            String substring = command.substring(command.indexOf("host"), command.length());
            String substringHostOnly = substring.substring(substring.indexOf(REQUEST_OPTION_ENTRY_VALUE_SEPARATOR), substring.indexOf(" "));
            String[] split = substringHostOnly.split(REQUEST_OPTION_INET_HOST_SEPARATOR);
            return Optional.of(new Host(split[0].replaceAll("[=;\\-\\s]",""), split[1].replaceAll("[=;\\-\\s]",""), false));
        }
        return Optional.empty();
    }

    public static String parseFileNumber(String fileNumberGroup) {
        return fileNumberGroup.split(REQUEST_OPTION_ENTRY_VALUE_SEPARATOR)[1].trim();
    }


}
