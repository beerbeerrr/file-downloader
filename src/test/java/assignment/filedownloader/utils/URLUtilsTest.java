package assignment.filedownloader.utils;

import assignment.filedownloader.domain.Protocol;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class URLUtilsTest {

    @Test
    void tryToCreateUtilityClassTestExpectedException() {
        Exception exception = assertThrows(IllegalStateException.class, URLUtils::new);
        assertNotNull(exception);
        assertEquals("This utility class doesn't suppose to be created by a constructor", exception.getMessage());
    }

    @Test
    void givenHTTPURLExpectedCorrectURLParser() throws MalformedURLException {
        String url = "http://speedtest.tele2.net/1MB.zip";
        assertSame(Protocol.HTTP, URLUtils.getProtocol(url));
        assertEquals("speedtest.tele2.net", URLUtils.getHost(url));
        assertEquals("/1MB.zip", URLUtils.getFileName(url));
        assertEquals("", URLUtils.getCredential(url)[0]);
        assertEquals("", URLUtils.getCredential(url)[1]);
    }

    @Test
    void givenFTPURLExpectedCorrectURLParser() throws MalformedURLException {
        String url = "ftp://anonymous:test@speedtest.tele2.net/1GB.zip";
        assertSame(Protocol.FTP, URLUtils.getProtocol(url));
        assertEquals("speedtest.tele2.net", URLUtils.getHost(url));
        assertEquals("/1GB.zip", URLUtils.getFileName(url));
        assertEquals("anonymous", URLUtils.getCredential(url)[0]);
        assertEquals("test", URLUtils.getCredential(url)[1]);
    }

    @Test
    void givenSFTPURLExpectedCorrectURLParser() throws MalformedURLException {
        String url = "sftp://demo:password@test.rebex.net/readme.txt";
        assertSame(Protocol.SFTP, URLUtils.getProtocol(url));
        assertEquals("test.rebex.net", URLUtils.getHost(url));
        assertEquals("/readme.txt", URLUtils.getFileName(url));
        assertEquals("demo", URLUtils.getCredential(url)[0]);
        assertEquals("password", URLUtils.getCredential(url)[1]);
    }
}