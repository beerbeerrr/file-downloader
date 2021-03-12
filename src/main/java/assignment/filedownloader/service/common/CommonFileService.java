package assignment.filedownloader.service.common;

import assignment.filedownloader.config.FileValidationConfig;
import assignment.filedownloader.domain.FileWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * This service is using for interacting common action with file such as preparing a folder, deleting downloaded file and validating download result
 */
@Service
public class CommonFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFileService.class);

    private final FileValidationConfig fileValidationConfig;

    @Autowired
    public CommonFileService(FileValidationConfig fileValidationConfig) {
        this.fileValidationConfig = fileValidationConfig;
    }

    public void createFolderIfRequired(FileWrapper fileWrapper) {
        if (new File(fileWrapper.getFolder()).mkdirs()) {
            LOGGER.info("Creating new folder for file {} at {}", fileWrapper.getUrl(), fileWrapper.getFullPath());
        }
    }

    public void deleteDownloadedFile(FileWrapper fileWrapper) {
        File downloadedFile = new File(fileWrapper.getFullPath());
        if (downloadedFile.exists() && downloadedFile.delete()) {
            LOGGER.warn("Deleting previous downloaded file of {} on {}", fileWrapper.getUrl(), fileWrapper.getFullPath());
        }
    }

    public boolean validateDownloadResult(FileWrapper fileWrapper) throws IOException {
        try {
            File downloadedFile = new File(fileWrapper.getFullPath());
            String expectedMD5 = fileValidationConfig.getMd5().getOrDefault(downloadedFile.getName(), "");
            if (!expectedMD5.isEmpty()) {
                InputStream targetStream = new FileInputStream(downloadedFile);
                String downloadedMD5 = DigestUtils.md5Hex(targetStream).toLowerCase();
                if (expectedMD5.equals(downloadedMD5)) {
                    LOGGER.info("Downloaded file of {} is valid (MD5: {})", fileWrapper.getUrl(), downloadedMD5);
                    return true;
                } else {
                    LOGGER.error("Downloaded file MD5 is not matched with configuration file");
                    throw new RuntimeException("Downloaded file MD5 is not matched with configuration file");
                }
            } else {
                LOGGER.error("Cannot find expected MD5 on configuration file");
                throw new RuntimeException("Cannot find expected MD5 on configuration file");
            }
        } catch (Exception exception) {
            LOGGER.error("File validation failed for {}", fileWrapper.getUrl());
            throw exception;
        }
    }
}
