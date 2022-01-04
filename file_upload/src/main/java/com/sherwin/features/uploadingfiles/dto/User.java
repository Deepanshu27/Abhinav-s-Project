package com.sherwin.features.uploadingfiles.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String userName;
    private List<String> userRoles;
}
