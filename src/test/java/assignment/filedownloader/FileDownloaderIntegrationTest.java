package assignment.filedownloader;

import assignment.filedownloader.service.downloader.FileDownloaderService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*
 * Purpose of this test is to download the real file from HTTP, FTP, SFTP server to another test directory and validate if downloaded file exists
 * In this test, the application will running in dryrun mode
 */
@SpringBootTest
@ActiveProfiles("integration-test")
class FileDownloaderIntegrationTest {

    @Autowired
    private FileDownloaderService fileDownloaderService;

    @BeforeEach
    void setup() throws IOException {
        FileUtils.deleteDirectory(new File("downloaded-test"));
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File("downloaded-test"));
    }

    @Test
    void fileDownloaderHTTPProtocolTestExpectedFileExists() {
        fileDownloaderService.download(Arrays.asList("http://speedtest.tele2.net/1MB.zip"));
        assertTrue(new File("downloaded-test/http/1MB.zip").exists());
        assertTrue(new File("downloaded-test/http/1MB.zip").isFile());
    }

    @Test
    void fileDownloaderHTTPProtocolTestExpectedFileNotExists() {
        fileDownloaderService.download(Arrays.asList("http://speedtest.tele2.net/0MB.zip"));
        assertFalse(new File("downloaded-test/http/0MB.zip").exists());
        assertFalse(new File("downloaded-test/http/0MB.zip").isFile());
    }

    @Test
    void fileDownloaderFTPProtocolTestExpectedFileExists() {
        fileDownloaderService.download(Arrays.asList("ftp://anonymous:test@speedtest.tele2.net/1MB.zip"));
        assertTrue(new File("downloaded-test/ftp/1MB.zip").exists());
        assertTrue(new File("downloaded-test/ftp/1MB.zip").isFile());
    }

    @Test
    void fileDownloaderFTPProtocolTestExpectedFileNotExists() {
        fileDownloaderService.download(Arrays.asList("ftp://anonymous:test@speedtest.tele2.net/0MB.zip"));
        assertFalse(new File("downloaded-test/ftp/0MB.zip").exists());
        assertFalse(new File("downloaded-test/ftp/0MB.zip").isFile());
    }

    @Test
    void fileDownloaderSFTPProtocolTestExpectedFileExists() {
        fileDownloaderService.download(Arrays.asList("sftp://demo:password@test.rebex.net/readme.txt"));
        assertTrue(new File("downloaded-test/sftp/readme.txt").exists());
        assertTrue(new File("downloaded-test/sftp/readme.txt").isFile());
    }

    @Test
    void fileDownloaderSFTPProtocolTestExpectedFileNotExists() {
        fileDownloaderService.download(Arrays.asList("sftp://demo:password@test.rebex.net/readme2.txt"));
        assertFalse(new File("downloaded-test/sftp/readme2.txt").exists());
        assertFalse(new File("downloaded-test/sftp/readme2.txt").isFile());
    }

    @Test
    void fileDownloaderUnsupportedProtocolTest() {
        fileDownloaderService.download(Arrays.asList("mailto://speedtest.tele2.net/1MB.zip"));
        assertFalse(new File("downloaded-test/mailto/1MB.zip").exists());
        assertFalse(new File("downloaded-test/mailto/1MB.zip").isFile());
    }

    @Test
    void fileDownloaderInvalidUrlTest() {
        fileDownloaderService.download(Arrays.asList("speedtest.tele2.net/1MB.zip"));
        assertFalse(new File("downloaded-test/1MB.zip").exists());
        assertFalse(new File("downloaded-test/1MB.zip").isFile());
    }
}
