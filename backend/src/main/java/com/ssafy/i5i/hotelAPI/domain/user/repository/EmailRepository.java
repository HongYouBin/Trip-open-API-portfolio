package com.ssafy.i5i.hotelAPI.domain.user.repository;

import com.ssafy.i5i.hotelAPI.domain.user.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    @Query("SELECT e FROM Email e WHERE e.email = :email and e.isAuthorized = false and e.code =:code")
    Optional<Email> selectUnAuthorizedEmailWithCode(@Param("email") String email, @Param("code") Long code);
    @Query("SELECT e FROM Email e WHERE e.email = :email and e.isAuthorized = true and e.code =:code")
    Optional<Email> selectAuthorizedEmailWithCode(@Param("email") String email, @Param("code") Long code);

    @Modifying
    @Query("update Email e set e.authorizedTime =:authorizedTime where e.email =:email and e.code =:code")
    int setAuthorizedTimeWithCode(@Param("authorizedTime") LocalDateTime authorizedTime, @Param("email") String email,  @Param("code") Long code);

    @Modifying
    @Query("update Email e set e.isAuthorized = true where e.email =:email and e.code =:code")
    int setAuthorizedStatusTrueWithCode(@Param("email") String email, @Param("code") Long code);

    @Modifying
    @Query("delete from Email e where e.email =:email")
    int deleteEmail(@Param("email") String email);
}
