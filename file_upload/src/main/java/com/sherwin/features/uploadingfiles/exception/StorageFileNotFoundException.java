package com.sherwin.features.uploadingfiles.exception;

import org.springframework.lang.NonNull;

public class StorageFileNotFoundException extends Exception {

    public StorageFileNotFoundException() {
        super();
    }

    public StorageFileNotFoundException(@NonNull final String message) {
        super(message);
    }
}
