package assignment.filedownloader;

import assignment.filedownloader.config.DownloadDestinationConfig;
import assignment.filedownloader.config.DownloadListConfig;
import assignment.filedownloader.domain.Protocol;
import assignment.filedownloader.service.downloader.FTPDownloaderService;
import assignment.filedownloader.service.downloader.HTTPDownloaderService;
import assignment.filedownloader.service.downloader.SFTPDownloaderService;
import assignment.filedownloader.service.mapper.ProtocolMapperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * Purpose of this test is to check up if application is loading configuration file correctly
 * In this test, the application will running in dryrun mode
 */
@SpringBootTest
@ActiveProfiles("system-checkup")
class FileDownloaderSystemCheckupTest {

    @Autowired
    private DownloadDestinationConfig downloadDestinationConfig;

    @Autowired
    private DownloadListConfig downloadListConfig;

    @Autowired
    private ProtocolMapperService protocolMapperService;

    @Autowired
    private HTTPDownloaderService httpDownloaderService;

    @Autowired
    private FTPDownloaderService ftpDownloaderService;

    @Autowired
    private SFTPDownloaderService sftpDownloaderService;

    @Test
    void downloadDestinationConfigTest() {
        Map<String, String> downloadDestinationByProtocolMap = downloadDestinationConfig.getProtocol();
        assertEquals("downloaded-test/http", downloadDestinationByProtocolMap.get(Protocol.HTTP.getValue()));
        assertEquals("downloaded-test/ftp", downloadDestinationByProtocolMap.get(Protocol.FTP.getValue()));
        assertEquals("downloaded-test/sftp", downloadDestinationByProtocolMap.get(Protocol.SFTP.getValue()));
    }

    @Test
    void downloadListConfigTest() {
        String[] expectedListOnEnvFile = new String[]{"http://speedtest.tele2.net/1MB.zip", "ftp://anonymous:test@speedtest.tele2.net/10MB.zip", "sftp://demo:password@test.rebex.net/readme.txt"};
        assertArrayEquals(expectedListOnEnvFile, downloadListConfig.getUrl());
    }

    @Test
    void protocolMapperService() {
        assertEquals(httpDownloaderService, protocolMapperService.getProtocol(Protocol.HTTP));
        assertEquals(ftpDownloaderService, protocolMapperService.getProtocol(Protocol.FTP));
        assertEquals(sftpDownloaderService, protocolMapperService.getProtocol(Protocol.SFTP));
        Protocol unsupportedProtocol = Protocol.fromString("mailto");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> protocolMapperService.getProtocol(unsupportedProtocol));
        assertNotNull(exception);
        assertEquals("Unsupported protocol", exception.getMessage());
    }
}
