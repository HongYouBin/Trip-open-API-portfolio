package com.ssafy.i5i.hotelAPI.domain.user.repository;


import com.ssafy.i5i.hotelAPI.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.token = :token AND u.isDeleted = false")
    Optional<User> findByToken(@Param("token") String token);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false")
    Optional<User> selectUserById(@Param("id") String id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> selectAllUserById(@Param("id") String id);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false")
    Optional<User> findById(@Param("id") String id);

    @Modifying
    @Query("update User u set u.token = :token where u.id = :id")
    void updateUserToken(@Param("id") String id, @Param("token") String token);

    @Modifying
    @Query("update User u set u.isDeleted = true where u.id = :id and u.isDeleted = false")
    void deleteUser(@Param("id") String id);
}
