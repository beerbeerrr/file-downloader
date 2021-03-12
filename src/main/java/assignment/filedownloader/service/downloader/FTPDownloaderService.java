package assignment.filedownloader.service.downloader;

import assignment.filedownloader.domain.FileWrapper;
import assignment.filedownloader.domain.IProtocol;
import assignment.filedownloader.utils.IOUtilsExtended;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * This service is using for downloading file through FTP protocol implemented IProtocol interface
 */
@Service
public class FTPDownloaderService implements IProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPDownloaderService.class);

    @Value("${protocol.ftp.timeout-ms:10000}")
    private int timeout;

    private FTPClient setupFTPClient() {
        LOGGER.info("Initiating FTP client");
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultTimeout(timeout);
        ftpClient.setDataTimeout(timeout);
        ftpClient.setConnectTimeout(timeout);
        ftpClient.setControlKeepAliveTimeout(timeout);
        return ftpClient;
    }

    private void logout(FTPClient ftpClient) throws IOException {
        LOGGER.info("Logging out from FTP client");
        if (ftpClient.logout()) {
            ftpClient.disconnect();
        }
    }

    @Override
    public long getFileSize(FileWrapper fileWrapper) throws Exception {
        FTPClient ftpClient = setupFTPClient();
        String fileWrapperUrl = fileWrapper.getUrl();
        try {
            String[] accessCredential = fileWrapper.getCredential();
            String username = accessCredential[0];
            String password = accessCredential[1];
            ftpClient.connect(fileWrapper.getHostName());
            ftpClient.login(username, password);
            ftpClient.sendCommand("SIZE", fileWrapper.getFileName());
            LOGGER.info("Getting file size to download from {}", fileWrapperUrl);
            String[] ftpReply = ftpClient.getReplyString().split(" ");
            if (!"213".equals(ftpReply[0])) {
                throw new RuntimeException("Received FTP " + ftpReply[0] + " response while getting file size of " + fileWrapperUrl);
            }
            return Long.parseLong(ftpReply[1].trim());
        } catch (Exception exception) {
            LOGGER.info("An error occurred while getting file size from {}", fileWrapperUrl);
            throw new RuntimeException(exception);
        } finally {
            logout(ftpClient);
        }
    }

    @Override
    public void download(FileWrapper fileWrapper) throws Exception {
        FTPClient ftpClient = setupFTPClient();
        String fileWrapperUrl = fileWrapper.getUrl();
        long fileSize = fileWrapper.getFileSize();
        try {
            String[] accessCredential = fileWrapper.getCredential();
            String username = accessCredential[0];
            String password = accessCredential[1];
            ftpClient.connect(fileWrapper.getHostName());
            ftpClient.login(username, password);
            LOGGER.info("Start downloading file from {} by using {} protocol (total size {})", fileWrapperUrl, fileWrapper.getProtocol(), fileSize);
            InputStream inputStream = ftpClient.retrieveFileStream(fileWrapper.getFileName());
            FileOutputStream outputStream = new FileOutputStream(fileWrapper.getFullPath());
            IOUtilsExtended.copy(inputStream, outputStream, fileSize);
            LOGGER.info("Downloaded {} from {}", fileWrapper.getFileName(), fileWrapperUrl);
        } catch (Exception exception) {
            LOGGER.info("An error occurred while downloading file size from {}", fileWrapperUrl);
            throw new RuntimeException(exception);
        } finally {
            logout(ftpClient);
        }
    }
}
