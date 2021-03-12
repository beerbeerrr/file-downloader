package assignment.filedownloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/*
 * This configuration class is using for getting downloaded file destination per protocol
 */
@Configuration
@ConfigurationProperties("download-destination")
public class DownloadDestinationConfig {

    private Map<String, String> protocol;

    public Map<String, String> getProtocol() {
        return protocol;
    }

    public void setProtocol(Map<String, String> protocol) {
        this.protocol = protocol;
    }
}
