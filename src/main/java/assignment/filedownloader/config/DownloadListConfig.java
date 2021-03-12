package assignment.filedownloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
 * This configuration class is using for getting url list to be downloaded by application
 */
@Configuration
@ConfigurationProperties("download-list")
public class DownloadListConfig {

    private String[] url;

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }
}
