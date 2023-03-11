package com.example.myidejava.domain.member;

import com.example.myidejava.domain.common.BaseDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "social_login")
public class SocialLogin extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

    @Column(name = "access_token", length = 1500, nullable = false)
    private String accessToken;

    @Column(name = "access_token_issued_at", nullable = false)
    private LocalDateTime accessTokenIssuedAt;

    @Column(name = "access_token_expired_at", nullable = false)
    private LocalDateTime accessTokenExpiredAt;

    @Column(name = "refresh_token", length = 1500)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_social_login_member_id"))
    private Member member;

    public static SocialLogin create(Member member, String accessToken){
        return SocialLogin.builder()
                .socialType(SocialType.EMAIL)
                .uniqueId(UUID.randomUUID().toString())
                .member(member)
                .accessToken(accessToken)
                .accessTokenIssuedAt(LocalDateTime.now())
                .accessTokenExpiredAt(LocalDateTime.now().plusDays(1))
                .build();
    }

}
