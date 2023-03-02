package com.example.myidejava.domain.member;

import com.example.myidejava.domain.common.BaseDateTime;
import com.example.myidejava.dto.auth.RegisterRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "member")
    private List<SocialLogin> socialLoginList = new ArrayList<>();

    public static Member createEmailUser(String email, String username, String password) {
        return Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .isActive(Boolean.TRUE)
                .build();
    }

}
