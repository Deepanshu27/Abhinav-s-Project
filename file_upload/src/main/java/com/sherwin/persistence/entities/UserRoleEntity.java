package com.sherwin.persistence.entities;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_role")
public class UserRoleEntity implements Serializable {

    @EmbeddedId
    private UserRoleId userRoleId;
}
