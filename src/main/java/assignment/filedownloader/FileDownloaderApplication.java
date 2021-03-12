package assignment.filedownloader;

import assignment.filedownloader.config.DownloadListConfig;
import assignment.filedownloader.service.downloader.FileDownloaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class FileDownloaderApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloaderApplication.class);

    @Value("${environment}")
    private String environment;

    @Value("${dry-run:true}")
    private boolean dryRun;

    @Autowired
    private DownloadListConfig downloadListConfig;

    @Autowired
    private FileDownloaderService fileDownloaderService;

    /*
     * This method will start downloading process regarding url list in configuration file and dry-run flag
     */
    @PostConstruct
    public void init() {
        LOGGER.info("FileDownloaderApplication is running on {} env", environment);
        if (!dryRun) {
            fileDownloaderService.download(Arrays.asList(downloadListConfig.getUrl()));
        } else {
            //Please checkout dry-run property on application.yml file
            LOGGER.info("Dryrun mode is enabled");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(FileDownloaderApplication.class, args);
    }

}
