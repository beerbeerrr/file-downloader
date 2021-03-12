package assignment.filedownloader.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProtocolTest {

    @Test
    void givenHTTPTextExpectedHTTPEnum(){
        assertSame(Protocol.HTTP, Protocol.fromString("http"));
    }

    @Test
    void givenFTPTextExpectedFTPEnum(){
        assertSame(Protocol.FTP, Protocol.fromString("ftp"));
    }

    @Test
    void givenSFTPTextExpectedSFTPEnum(){
        assertSame(Protocol.SFTP, Protocol.fromString("sftp"));
    }

    @Test
    void givenMailtoTextExpectedNull(){
        assertNull(Protocol.fromString("mailto"));
    }
}