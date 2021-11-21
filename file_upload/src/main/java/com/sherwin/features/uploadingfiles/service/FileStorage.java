package com.sherwin.features.uploadingfiles.service;

import com.jcraft.jsch.*;
import com.sherwin.features.uploadingfiles.dto.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Scope(value = "prototype")
public class FileStorage implements StorageService {

    @Value("${file.location}")
    private String fileLocation;

    @Value("${remote.host.name}")
    private String host;
    @Value("${remote.user.name}")
    String user;
    @Value("${remote.user.password}")
    String password;
    @Value("${remote.file.location}")
    String remoteDir;

    /**
     * Save the file in the file location.
     *
     * @param file multipart file.
     */
    @Override
    public void store(@NonNull final MultipartFile file) throws IOException, JSchException, SftpException {
        /* store to local path of the app server. */
        final String fileName = storeToLocalPath(file);
        /* get the remote server connection. */
        final ChannelSftp sftpSession = getSession();
        /* upload the file to remote host and close the connection. */
        uploadFileToRemoteHost(sftpSession, fileName);
    }

    /**
     * Store the file to the local server path.
     *
     * @param file file to be saved.
     * @return file name which was saved.
     * @throws IOException if exception occurred while saving the file.
     */
    private String storeToLocalPath(@NonNull final MultipartFile file) throws IOException {
        final String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
        final String fileName = date + file.getOriginalFilename();
        final String filePath = fileLocation + File.separator + fileName;
        final File fileDirectory = new File(fileLocation);
        if (!fileDirectory.exists()) {
            fileDirectory.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    /**
     * Get the SFTP session to the remote host.
     *
     * @return SFTP channel to the remote host.
     * @throws JSchException if error occurred while connecting to remote host.
     */
    private ChannelSftp getSession() throws JSchException {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, 22);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();
        return (ChannelSftp) session.openChannel("sftp");
    }

    /**
     * Upload the file to the remote host.
     *
     * @param channelSftp SFTP channel connection.
     * @param fileName    name of the file to be uploaded.
     * @throws SftpException if error occurred while transferring the file to the remote server.
     */
    private void uploadFileToRemoteHost(
            @NonNull final ChannelSftp channelSftp,
            @NonNull final String fileName) throws SftpException, JSchException {
        final String sourcePath = fileLocation + File.separator + fileName;
        final String destinationPath = remoteDir + File.separator + fileName;
        channelSftp.connect();
        channelSftp.put(sourcePath, destinationPath);
        channelSftp.exit();
    }

    /**
     * load all the file at the path.
     *
     * @return Stream of file path present in file location.
     */
    @Override
    public List<FileInfo> loadAll() throws IOException {
        final Path path = Paths.get(fileLocation);
        if (Files.exists(path)) {
            try (Stream<Path> filePathStream = Files.walk(path)) {
                return filePathStream
                        .map(this::getFileInfo)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } else {
            throw new NotDirectoryException("files not found");
        }
    }

    /**
     * Load the file from the path.
     *
     * @param filename name of the file.
     * @return File if present else throw exception.
     * @throws NoSuchFileException if file not found.
     */
    @Override
    public File loadFile(String filename) throws NoSuchFileException {
        final String filePath = fileLocation + File.pathSeparator + filename;
        final File file = new File(filePath);
        if (file.exists()) {
            return file;
        } else {
            throw new NoSuchFileException("File not found.");
        }
    }

    /**
     * Delete all the files present in the path.
     */
    @Override
    public List<String> deleteAll() throws IOException {
        final Path path = Paths.get(fileLocation);
        if (Files.exists(path)) {
            try (final Stream<Path> filePaths = Files.walk(path)) {
                return filePaths
                        .filter(this::deleteFile)
                        .map(fileLocation -> String.valueOf(fileLocation))
                        .collect(Collectors.toList());
            }

        } else {
            throw new NoSuchFileException("No files found in the directory. ");
        }
    }

    /**
     * Delete the file from the location.
     *
     * @param filename name of the file to be deleted.
     * @throws FileSystemException if file is not deleted.
     */
    @Override
    public String deleteFile(@NonNull final String filename) throws FileSystemException {
        final String filePath = fileLocation + File.pathSeparator + filename;
        final File file = new File(filePath);
        if (file.exists() && file.delete()) {
            return filename;
        } else {
            throw new FileSystemException("Not able to delete the file.");
        }
    }

    /**
     * Get the file info from the file path.
     *
     * @param path file path.
     * @return FileInfo object containing the details of the file.
     */
    private FileInfo getFileInfo(@NonNull final Path path) {
        final File file = new File(String.valueOf(path));
        if (file.exists() && file.isFile()) {
            return new FileInfo(
                    file.getName(),
                    new Date(file.lastModified()));
        }
        return null;
    }

    private boolean deleteFile(@NonNull final Path path) {
        final File file = new File(String.valueOf(path));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
