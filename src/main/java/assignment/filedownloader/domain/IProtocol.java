package assignment.filedownloader.domain;

/*
 * This interface is for being a base class to be implemented by supported protocol
 */
public interface IProtocol {

    long getFileSize(FileWrapper fileWrapper) throws Exception;

    void download(FileWrapper fileWrapper) throws Exception;

}
