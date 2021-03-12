package assignment.filedownloader.domain;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileWrapperTest {

    @Test
    void givenHTTPUrlExpectedFileWrapperObject() throws MalformedURLException {
        String url = "http://speedtest.tele2.net/1MB.zip";
        FileWrapper fileWrapper = FileWrapper.of(url);
        assertEquals("http://speedtest.tele2.net/1MB.zip", fileWrapper.getUrl());
        assertSame(Protocol.HTTP, fileWrapper.getProtocol());
        assertEquals("speedtest.tele2.net", fileWrapper.getHostName());
        assertEquals("/1MB.zip", fileWrapper.getFileName());
        assertEquals("", fileWrapper.getCredential()[0]);
        assertEquals("", fileWrapper.getCredential()[1]);
    }

    @Test
    void givenFTPUrlExpectedFileWrapperObject() throws MalformedURLException {
        String url = "ftp://anonymous:test@speedtest.tele2.net/1GB.zip";
        FileWrapper fileWrapper = FileWrapper.of(url);
        assertEquals("ftp://anonymous:test@speedtest.tele2.net/1GB.zip", fileWrapper.getUrl());
        assertSame(Protocol.FTP, fileWrapper.getProtocol());
        assertEquals("speedtest.tele2.net", fileWrapper.getHostName());
        assertEquals("/1GB.zip", fileWrapper.getFileName());
        assertEquals("anonymous", fileWrapper.getCredential()[0]);
        assertEquals("test", fileWrapper.getCredential()[1]);
    }

    @Test
    void givenSFTPUrlExpectedFileWrapperObject() throws MalformedURLException {
        String url = "sftp://demo:password@test.rebex.net/readme.txt";
        FileWrapper fileWrapper = FileWrapper.of(url);
        assertEquals("sftp://demo:password@test.rebex.net/readme.txt", fileWrapper.getUrl());
        assertSame(Protocol.SFTP, fileWrapper.getProtocol());
        assertEquals("test.rebex.net", fileWrapper.getHostName());
        assertEquals("/readme.txt", fileWrapper.getFileName());
        assertEquals("demo", fileWrapper.getCredential()[0]);
        assertEquals("password", fileWrapper.getCredential()[1]);
    }

    @Test
    void givenInvalidUrlExpectedException() {
        String url = "speedtest.tele2.net/1MB.zip";
        Exception exception = assertThrows(MalformedURLException.class, () -> FileWrapper.of(url));
        assertNotNull(exception);
        assertEquals("no protocol: speedtest.tele2.net/1MB.zip", exception.getMessage());
    }
}