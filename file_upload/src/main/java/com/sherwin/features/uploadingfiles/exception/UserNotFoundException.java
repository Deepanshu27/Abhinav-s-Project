package com.sherwin.features.uploadingfiles.exception;

import org.springframework.lang.NonNull;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(@NonNull final String message) {
        super(message);
    }
}
