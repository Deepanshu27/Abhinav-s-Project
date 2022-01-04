package com.sherwin.persistence.repositories;

import com.sherwin.persistence.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRoleEntity, String> {

    @Query(value = " SELECT ure"
                 + " FROM   UserRoleEntity ure "
                 + " WHERE  ure.userRoleId.userId = :userId"
    )
    Iterable<UserRoleEntity> findAllByUserId(@Param("userId") @NonNull String userId);

    @Query(value = " SELECT DISTINCT 1"
            + " FROM   UserRoleEntity ure "
            + " WHERE  ure.userRoleId.userId = :userId"
    )
    boolean existsById(@Param("userId") @NonNull final String userId);
}
