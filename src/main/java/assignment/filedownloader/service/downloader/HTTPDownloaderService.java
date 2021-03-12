package assignment.filedownloader.service.downloader;

import assignment.filedownloader.domain.FileWrapper;
import assignment.filedownloader.domain.IProtocol;
import assignment.filedownloader.utils.FileUtilsExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * This service is using for downloading file through HTTP protocol implemented IProtocol interface
 */
@Service
public class HTTPDownloaderService implements IProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPDownloaderService.class);

    @Value("${protocol.http.timeout-ms:10000}")
    private int timeout;

    @Override
    public long getFileSize(FileWrapper fileWrapper) throws Exception {
        String fileWrapperUrl = fileWrapper.getUrl();
        LOGGER.info("Getting file size to download from {}", fileWrapperUrl);
        URL url = new URL(fileWrapperUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        int httpResponseCode = httpConnection.getResponseCode();
        if (httpResponseCode >= 300) {
            LOGGER.info("An error occurred while getting file size from {}", fileWrapperUrl);
            throw new RuntimeException("Received HTTP " + httpResponseCode + " response while getting file size of " + fileWrapperUrl);
        }
        return httpConnection.getContentLengthLong();
    }

    @Override
    public void download(FileWrapper fileWrapper) {
        String fileWrapperUrl = fileWrapper.getUrl();
        long fileSize = fileWrapper.getFileSize();
        LOGGER.info("Start downloading file from {} by using {} protocol (total size {})", fileWrapperUrl, fileWrapper.getProtocol(), fileSize);
        try {
            FileUtilsExtended.copyURLToFile(new URL(fileWrapperUrl), new File(fileWrapper.getFullPath()), timeout, timeout, fileSize);
            LOGGER.info("Downloaded {} from {}", fileWrapper.getFileName(), fileWrapper);
        } catch (Exception exception) {
            LOGGER.info("An error occurred while downloading file size from {}", fileWrapperUrl);
            throw new RuntimeException(exception);
        }
    }
}
