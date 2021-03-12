package assignment.filedownloader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/*
 * This configuration class is using for getting prepared MD5 to be compared after the file is downloaded
 */
@Configuration
@ConfigurationProperties("file-validation")
public class FileValidationConfig {

    private Map<String, String> md5 = new HashMap<>();

    public Map<String, String> getMd5() {
        return md5;
    }

    public void setMd5(Map<String, String> md5) {
        this.md5 = md5;
    }
}
