package assignment.filedownloader.service.mapper;

import assignment.filedownloader.domain.Protocol;
import assignment.filedownloader.service.downloader.FTPDownloaderService;
import assignment.filedownloader.service.downloader.HTTPDownloaderService;
import assignment.filedownloader.service.downloader.SFTPDownloaderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtocolMapperServiceTest {

    private final HTTPDownloaderService httpDownloaderService = new HTTPDownloaderService();
    private final FTPDownloaderService ftpDownloaderService = new FTPDownloaderService();
    private final SFTPDownloaderService sftpDownloaderService = new SFTPDownloaderService();
    private final ProtocolMapperService protocolMapperService = new ProtocolMapperService(httpDownloaderService, ftpDownloaderService, sftpDownloaderService);

    @Test
    void givenHTTPProtocolExpectedHTTPDownloaderService() {
        assertEquals(protocolMapperService.getProtocol(Protocol.HTTP), httpDownloaderService);
    }

    @Test
    void givenFTPProtocolExpectedFTPDownloaderService() {
        assertEquals(protocolMapperService.getProtocol(Protocol.FTP), ftpDownloaderService);
    }

    @Test
    void givenSFTPProtocolExpectedSFTPDownloaderService() {
        assertEquals(protocolMapperService.getProtocol(Protocol.SFTP), sftpDownloaderService);
    }

    @Test
    void givenUnsupportedProtocolExpectedException() {
        Protocol unsupportedProtocol = Protocol.fromString("mailto");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> protocolMapperService.getProtocol(unsupportedProtocol));
        assertNotNull(exception);
        assertEquals("Unsupported protocol", exception.getMessage());
    }
}