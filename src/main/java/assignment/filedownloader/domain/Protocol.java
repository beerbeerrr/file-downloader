package assignment.filedownloader.domain;

/*
 * This enum class is using to define the available supported protocol in this application
 */
public enum Protocol {
    HTTP("http"),
    FTP("ftp"),
    SFTP("sftp");

    private final String value;

    Protocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Protocol fromString(String protocolString) {
        for (Protocol protocol : values()) {
            if (protocol.value.equals(protocolString)) {
                return protocol;
            }
        }
        return null;
    }
}
