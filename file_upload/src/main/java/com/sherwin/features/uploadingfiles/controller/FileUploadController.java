package com.sherwin.features.uploadingfiles.controller;

import com.sherwin.features.uploadingfiles.dto.FileInfo;
import com.sherwin.features.uploadingfiles.exception.StorageFileNotFoundException;
import com.sherwin.features.uploadingfiles.service.StorageService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * List all the uploaded file.
     *
     * @return List of file info object containing the file details.
     */
    @GetMapping("/")
    public ResponseEntity<List<FileInfo>> listUploadedFiles() {
        try {
            final List<FileInfo> fileList = storageService.loadAll();
            return ResponseEntity
                    .ok()
                    .body(fileList);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Get file from the server.
     *
     * @param fileName name of the file.
     * @return file if present.
     */
    @GetMapping("/files/{fileName}")
    @ResponseBody
    public ResponseEntity<String> getFile(@PathVariable(name = "fileName") String fileName) {
        try {
            final File file = storageService.loadFile(fileName);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).toString());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }

    }

    /**
     * Save the file in the server.
     *
     * @param file Multipart file object.
     */
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            storageService.store(file);
            return ResponseEntity
                    .ok()
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Delete all the files from the location.
     *
     * @return List of file path which are deleted.
     */
    @DeleteMapping("/")
    @ResponseBody
    public ResponseEntity<List<String>> deleteAllFiles() {
        try {
            final List<String> deletedFiles = storageService.deleteAll();
            return ResponseEntity
                    .ok()
                    .body(deletedFiles);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Delete a file from the server.
     *
     * @param fileName name of the file to be deleted.
     * @return list of files
     */
    @DeleteMapping("/files/{fileName}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable(name = "fileName") String fileName) {
        try {
            final String deletedFiles = storageService.deleteFile(fileName);
            return ResponseEntity
                    .ok()
                    .body(deletedFiles);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

}