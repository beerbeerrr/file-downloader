package assignment.filedownloader.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/*
 * Extended class of FileUtils for using IOUtilsExtended
 */
public class FileUtilsExtended extends FileUtils {

    public static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout, long fileSize) throws IOException {
        URLConnection connection = source.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        copyInputStreamToFile(connection.getInputStream(), destination, fileSize);
    }

    private static void copyInputStreamToFile(InputStream source, File destination, long fileSize) throws IOException {
        InputStream in = source;
        Throwable var3 = null;
        try {
            copyToFile(in, destination, fileSize);
        } catch (Throwable var12) {
            var3 = var12;
            throw var12;
        } finally {
            if (source != null) {
                if (var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var11) {
                        var3.addSuppressed(var11);
                    }
                } else {
                    source.close();
                }
            }
        }
    }

    private static void copyToFile(InputStream source, File destination, long fileSize) throws IOException {
        InputStream in = source;
        Throwable var3 = null;
        try {
            OutputStream out = openOutputStream(destination);
            Throwable var5 = null;
            try {
                IOUtilsExtended.copy(in, out, fileSize);
            } catch (Throwable var28) {
                var5 = var28;
                throw var28;
            } finally {
                if (out != null) {
                    if (var5 != null) {
                        try {
                            out.close();
                        } catch (Throwable var27) {
                            var5.addSuppressed(var27);
                        }
                    } else {
                        out.close();
                    }
                }
            }
        } catch (Throwable var30) {
            var3 = var30;
            throw var30;
        } finally {
            if (source != null) {
                if (var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var26) {
                        var3.addSuppressed(var26);
                    }
                } else {
                    source.close();
                }
            }
        }
    }
}
