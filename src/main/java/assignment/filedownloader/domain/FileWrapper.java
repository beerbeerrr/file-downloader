package assignment.filedownloader.domain;

import assignment.filedownloader.utils.URLUtils;

import java.net.MalformedURLException;

/*
 * This class is for using as a wrapper class after get an url from configuration file
 */
public class FileWrapper {

    private String url;
    private Protocol protocol;
    private String hostName;
    private String fileName;
    private String[] credential;
    private String folder;
    private long fileSize;

    public static FileWrapper of(String url) throws MalformedURLException {
        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl(url);
        fileWrapper.setProtocol(URLUtils.getProtocol(url));
        fileWrapper.setHostName(URLUtils.getHost(url));
        fileWrapper.setFileName(URLUtils.getFileName(url));
        fileWrapper.setCredential(URLUtils.getCredential(url));
        return fileWrapper;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getCredential() {
        return credential;
    }

    public void setCredential(String[] credential) {
        this.credential = credential;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFullPath() {
        return this.folder + this.fileName;
    }
}
