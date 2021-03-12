package assignment.filedownloader.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/*
 * Extended class of IOUtils for showing download progress in percents
 */
public class IOUtilsExtended extends IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtilsExtended.class);

    public static int copy(InputStream input, OutputStream output, long fileSize) throws IOException {
        long count = copyLarge(input, output, fileSize);
        return count > 2147483647L ? -1 : (int)count;
    }

    private static long copyLarge(InputStream input, OutputStream output, long fileSize) throws IOException {
        return copy(input, output, 4096, fileSize);
    }

    private static long copy(InputStream input, OutputStream output, int bufferSize, long fileSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize], fileSize);
    }

    private static long copyLarge(InputStream input, OutputStream output, byte[] buffer, long fileSize) throws IOException {
        Set<Long> passedPercent = new HashSet<>();
        long count;
        int n;
        for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
            long currentPercent = count * 100/fileSize;
            if(currentPercent > 0 && currentPercent % 5 == 0 && passedPercent.add(currentPercent)){
                LOGGER.info("Downloading completed {}%", currentPercent);
            }
        }
        return count;
    }

}
