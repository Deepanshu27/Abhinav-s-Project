package com.sherwin.persistence.repositories;

import com.sherwin.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

    @Query(value = " SELECT ure"
                 + " FROM   UserEntity ure "
                 + " WHERE  ure.userRoleId.userId = :userId"
    )
    Iterable<UserEntity> findUserRoleById(@Param("userId") @NonNull String userId);

    @Query(value = " SELECT case when count(1)> 0 then true else false end"
            + " FROM   UserEntity ure "
            + " WHERE  ure.userRoleId.userId = :userId"
    )
    boolean existsById(@Param("userId") @NonNull final String userId);
}
