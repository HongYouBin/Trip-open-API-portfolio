package com.ssafy.i5i.hotelAPI.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name="token", nullable = false)
    private String token;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

}


