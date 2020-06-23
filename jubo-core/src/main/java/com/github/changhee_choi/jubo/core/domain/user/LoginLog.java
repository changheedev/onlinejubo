package com.github.changhee_choi.jubo.core.domain.user;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Changhee Choi
 * @since 23/06/2020
 */
@Entity
@Table(name = "oj_login_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String loginIp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime loginDate;

    @Column(nullable = false)
    private boolean loginSuccess;

    private String description; //로그인 실패시 사유를 등록하기 위한 컬럼

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public LoginLog(String loginIp, boolean loginSuccess, String description, User user) {
        this.loginIp = loginIp;
        this.loginSuccess = loginSuccess;
        this.description = description;
        this.user = user;
        this.loginDate = LocalDateTime.now();
    }
}
