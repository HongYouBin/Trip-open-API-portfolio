package com.ssafy.i5i.hotelAPI.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "email")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;

    @Column(name="email", length = 255, nullable = false)
    private String email;

    @Column(name="created_time")
    private LocalDateTime createdTime;

    @Column(name="authorized_time")
    private LocalDateTime authorizedTime;

    @Column(name="is_authorized")
    private boolean isAuthorized;

    @Column(name="code")
    private Long code;
}
