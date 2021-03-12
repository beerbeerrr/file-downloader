package assignment.filedownloader.service.common;

import assignment.filedownloader.config.FileValidationConfig;
import assignment.filedownloader.domain.FileWrapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonFileServiceTest {

    private FileValidationConfig fileValidationConfig;
    private CommonFileService commonFileService;

    @BeforeEach
    void setup() throws IOException {
        Map<String, String> mockupFileValidationMap = new HashMap<>();
        mockupFileValidationMap.put("test-file.txt", "cb4594a8e124ae2ea1cf9f58a5068ee7");
        mockupFileValidationMap.put("test-file-with-additional-content.txt", "cb4594a8e124ae2ea1cf9f58a5068ee7");
        fileValidationConfig = new FileValidationConfig();
        fileValidationConfig.setMd5(mockupFileValidationMap);
        commonFileService = new CommonFileService(fileValidationConfig);
        FileUtils.deleteDirectory(new File("test-folder"));
    }

    @AfterEach
    void teardown() throws IOException {
        FileUtils.deleteDirectory(new File("test-folder"));
    }

    @Test
    void createFolderIfRequiredExpectedNewFolderToBeCreated() {
        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl("test-case");
        fileWrapper.setFileName("/test-file.txt");
        fileWrapper.setFolder("test-folder");
        commonFileService.createFolderIfRequired(fileWrapper);

        File expectedFile = new File("test-folder");
        assertTrue(expectedFile.exists());
        assertTrue(expectedFile.isDirectory());
    }

    @Test
    void deleteDownloadedFileTestExpectedCreatedFileShouldToBeDeleted() throws IOException {
        //Create test case file
        new File("test-folder").mkdirs();
        new File("test-folder/test-file.txt").createNewFile();

        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl("test-case");
        fileWrapper.setFileName("/test-file.txt");
        fileWrapper.setFolder("test-folder");
        commonFileService.deleteDownloadedFile(fileWrapper);

        File expectedFile = new File("test-folder/test.file.txt");
        assertFalse(expectedFile.exists());
    }

    @Test
    void validateDownloadResultTestExpectedSuccess() throws IOException {
        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl("test-case");
        fileWrapper.setFolder("src/test/resources/test-data");
        fileWrapper.setFileName("/test-file.txt");
        assertTrue(commonFileService.validateDownloadResult(fileWrapper));
    }

    @Test
    void validateDownloadResultTestExpectedFailedOnMD5Checking() throws IOException {
        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl("test-case-with-additional-content");
        fileWrapper.setFolder("src/test/resources/test-data");
        fileWrapper.setFileName("/test-file-with-additional-content.txt");
        Exception exception = assertThrows(Exception.class, () -> commonFileService.validateDownloadResult(fileWrapper));
        assertNotNull(exception);
        assertEquals("Downloaded file MD5 is not matched with configuration file", exception.getMessage());
    }

    @Test
    void validateDownloadResultTestExpectedFailed() {
        FileWrapper fileWrapper = new FileWrapper();
        fileWrapper.setUrl("test-case-does-not-exist-file");
        fileWrapper.setFolder("src/test/resources/test-data");
        fileWrapper.setFileName("/test-file-does-not-exist.txt");
        Exception exception = assertThrows(Exception.class, () -> commonFileService.validateDownloadResult(fileWrapper));
        assertNotNull(exception);
    }
}