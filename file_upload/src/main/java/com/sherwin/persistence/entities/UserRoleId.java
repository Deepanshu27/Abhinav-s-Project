package com.sherwin.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_role")
    private String userRole;
}
