package com.sherwin.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_role")
public class UserEntity implements Serializable {

    @EmbeddedId
    private UserRoleId userRoleId;

    @Column(name = "password")
    private String password;
}
