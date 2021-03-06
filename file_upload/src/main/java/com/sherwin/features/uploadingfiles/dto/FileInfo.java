package com.sherwin.features.uploadingfiles.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class FileInfo {
    private final String fileName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private final Date createdDate;
}
