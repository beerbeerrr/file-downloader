package assignment.filedownloader.service.downloader;

import assignment.filedownloader.domain.FileWrapper;
import assignment.filedownloader.domain.IProtocol;
import assignment.filedownloader.domain.Protocol;
import assignment.filedownloader.utils.IOUtilsExtended;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;

/*
 * This service is using for downloading file through SFTP protocol implemented IProtocol interface
 */
@Service
public class SFTPDownloaderService implements IProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPDownloaderService.class);

    @Value("${protocol.sftp.timeout-ms:10000}")
    private int timeout;

    @Value("${protocol.sftp.ssh}")
    private String ssh;

    private ChannelSftp setupJsch(FileWrapper fileWrapper) throws JSchException {
        JSch jsch = new JSch();
        jsch.setKnownHosts(ssh);
        String[] accessCredential = fileWrapper.getCredential();
        String username = accessCredential[0];
        String password = accessCredential[1];
        Session jschSession = jsch.getSession(username, fileWrapper.getHostName());
        jschSession.setPassword(password);
        jschSession.setTimeout(timeout);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel(Protocol.SFTP.getValue());
    }

    private void logout(ChannelSftp channelSftp) throws JSchException {
        if (channelSftp != null) {
            Session session = channelSftp.getSession();
            channelSftp.disconnect();
            session.disconnect();
        }
    }

    @Override
    public long getFileSize(FileWrapper fileWrapper) throws Exception {
        ChannelSftp channelSftp = null;
        String fileWrapperUrl = fileWrapper.getUrl();
        try {
            channelSftp = setupJsch(fileWrapper);
            channelSftp.connect();
            LOGGER.info("Getting file size to download from {}", fileWrapperUrl);
            return channelSftp.stat(fileWrapper.getFileName()).getSize();
        } catch (Exception exception) {
            LOGGER.info("An error occurred while getting file size from {}", fileWrapperUrl);
            throw new RuntimeException(exception);
        } finally {
            logout(channelSftp);
        }
    }

    @Override
    public void download(FileWrapper fileWrapper) throws Exception {
        ChannelSftp channelSftp = null;
        String fileWrapperUrl = fileWrapper.getUrl();
        long fileSize = fileWrapper.getFileSize();
        try {
            channelSftp = setupJsch(fileWrapper);
            channelSftp.connect();
            LOGGER.info("Start downloading file from {} by using {} protocol (total size {})", fileWrapperUrl, fileWrapper.getProtocol(), fileSize);
            InputStream inputStream = channelSftp.get(fileWrapper.getFileName());
            FileOutputStream outputStream = new FileOutputStream(fileWrapper.getFullPath());
            IOUtilsExtended.copy(inputStream, outputStream, fileSize);
            LOGGER.info("Downloaded {} from {}", fileWrapper.getFileName(), fileWrapperUrl);
        } catch (Exception exception) {
            LOGGER.info("An error occurred while downloading file size from {}", fileWrapperUrl);
            throw new RuntimeException(exception);
        } finally {
            logout(channelSftp);
        }
    }
}
