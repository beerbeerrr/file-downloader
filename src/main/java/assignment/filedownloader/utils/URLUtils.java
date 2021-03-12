package assignment.filedownloader.utils;

import assignment.filedownloader.domain.Protocol;

import java.net.MalformedURLException;
import java.net.URL;

/*
 * This utility class is using for extracting common information from input URL
 */
public final class URLUtils {

    protected URLUtils() {
        throw new IllegalStateException("This utility class doesn't suppose to be created by a constructor");
    }

    public static Protocol getProtocol(String url) throws MalformedURLException {
        if (url.startsWith(Protocol.SFTP.getValue())) {
            return Protocol.SFTP;
        }
        return Protocol.fromString(new URL(url).getProtocol());
    }

    public static String getHost(String url) throws MalformedURLException {
        url = replaceProtocolToFTPIfSFTP(url);
        return new URL(url).getHost();
    }

    public static String getFileName(String url) throws MalformedURLException {
        url = replaceProtocolToFTPIfSFTP(url);
        return new URL(url).getFile();
    }

    public static String[] getCredential(String url) throws MalformedURLException {
        url = replaceProtocolToFTPIfSFTP(url);
        String userInfo = new URL(url).getUserInfo();
        if (userInfo != null && !userInfo.isEmpty()) {
            return userInfo.split(":");
        }
        return new String[]{"", ""};
    }

    /*
     * This is a quick fix to solve MalformedURLException since java.net.URL doesn't support SFTP protocol parsing
     */
    private static String replaceProtocolToFTPIfSFTP(String url) {
        if (url.startsWith(Protocol.SFTP.getValue())) {
            return url.replace(Protocol.SFTP.getValue(), Protocol.FTP.getValue());
        }
        return url;
    }

}