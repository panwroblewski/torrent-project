package app.api;

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

    public static ApiMethod fromString(String text) {
        for (ApiMethod method : ApiMethod.values()) {
            if (method.method.equalsIgnoreCase(text)) {
                return method;
            }
        }
        return null;
    }

}
//show host=localhost:16900