package assignment.filedownloader.service.downloader;

import assignment.filedownloader.config.DownloadDestinationConfig;
import assignment.filedownloader.domain.FileWrapper;
import assignment.filedownloader.domain.IProtocol;
import assignment.filedownloader.domain.Protocol;
import assignment.filedownloader.service.common.CommonFileService;
import assignment.filedownloader.service.mapper.ProtocolMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

/*
 * This service is for being a common class which receive an url for file downloading
 */
@Service
public class FileDownloaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloaderService.class);

    @Value("${retry-on-failure-ms:1000}")
    private int retryOnFailureMs;

    @Value("${retry-attempt-max:3}")
    private int retryAttempt;

    @Autowired
    private ProtocolMapperService protocolMapperService;

    @Autowired
    private DownloadDestinationConfig downloadDestinationConfig;

    @Autowired
    private CommonFileService commonFileService;

    public void download(List<String> urlList) {
        for (String url : urlList) {
            LOGGER.info("Incoming URL to download {}", url);
            this.download(url);
            LOGGER.info("--------------------------------");
        }
    }

    public void download(String url) {
        FileWrapper fileWrapper = null;
        try {
            fileWrapper = FileWrapper.of(url);
        } catch (MalformedURLException exception) {
            LOGGER.error("Failed to initiate FileWrapper of {}", url);
            exception.printStackTrace();
        }
        if (fileWrapper != null) {
            int attempt = 1;
            boolean isSuccess = false;
            while (!isSuccess) {
                isSuccess = this.download(fileWrapper);
                if (!isSuccess && attempt < retryAttempt) {
                    LOGGER.warn("Failed to download file from {} on attempt {}", url, attempt);
                    this.waitForNextAttempt(attempt);
                    attempt++;
                } else {
                    break;
                }
            }
            if (isSuccess) {
                LOGGER.info("Finished downloading file from {} on attempt {}", url, attempt);
            } else {
                LOGGER.warn("Exhausted on download file from {} after attempt {}", url, attempt);
            }
        }
    }

    private boolean download(FileWrapper fileWrapper) {
        boolean isSuccess = false;
        Protocol fileWrapperProtocol = fileWrapper.getProtocol();
        try {
            IProtocol protocolDownloader = protocolMapperService.getProtocol(fileWrapperProtocol);
            fileWrapper.setFileSize(protocolDownloader.getFileSize(fileWrapper));
            fileWrapper.setFolder(downloadDestinationConfig.getProtocol().get(fileWrapperProtocol.getValue()));
            this.allocateSpace(fileWrapper);
            protocolDownloader.download(fileWrapper);
            isSuccess = commonFileService.validateDownloadResult(fileWrapper);
        } catch (Exception exception) {
            LOGGER.error("Exception was thrown during the process");
            exception.printStackTrace();
            LOGGER.warn("Cleaning up download file due to error");
            commonFileService.deleteDownloadedFile(fileWrapper);
        }
        return isSuccess;
    }

    public void allocateSpace(FileWrapper fileWrapper) {
        LOGGER.info("Allocating space and remove existing download file if existed");
        commonFileService.createFolderIfRequired(fileWrapper);
        commonFileService.deleteDownloadedFile(fileWrapper);
    }

    /*
     * Simple way to wait for next attempt if the destination server was down
     */
    private void waitForNextAttempt(int attempt) {
        try {
            Thread.sleep((long) attempt * retryOnFailureMs);
        } catch (InterruptedException e) {
            LOGGER.error("An exception occurred while waiting for next retry");
        }
    }
}
