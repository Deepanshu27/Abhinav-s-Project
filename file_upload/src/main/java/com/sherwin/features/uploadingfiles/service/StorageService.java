package com.sherwin.features.uploadingfiles.service;

import com.sherwin.features.uploadingfiles.dto.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {

    void store(MultipartFile file) throws IOException;

    List<FileInfo> loadAll() throws IOException;

    File loadFile(String filename) throws NoSuchFileException;

    List<String> deleteAll() throws IOException;

    String deleteFile(String filename) throws FileSystemException;

}
