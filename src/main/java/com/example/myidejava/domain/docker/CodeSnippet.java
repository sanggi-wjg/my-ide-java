package com.example.myidejava.domain.docker;

import com.example.myidejava.domain.common.BaseDateTime;
import com.example.myidejava.domain.member.Member;
import com.example.myidejava.dto.docker.CodeRequest;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "code_snippet")
public class CodeSnippet extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_code_snippet_member_id"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", nullable = false, foreignKey = @ForeignKey(name = "fk_code_snippet_container_id"))
    private Container container;

    @Column(name = "request", nullable = false)
    private String request;

    @Type(JsonType.class)
    @Column(name = "response", columnDefinition = "json")
    private Map<String, String> response;

    @Column(name = "is_success")
    private Boolean isSuccess = Boolean.FALSE;

    public static CodeSnippet create(Container container, CodeRequest codeRequest, Optional<Member> member) {
        CodeSnippetBuilder builder = CodeSnippet.builder()
                .container(container)
                .request(codeRequest.getCode());
        member.ifPresent(builder::member);
        return builder.build();
    }

    public void saveResponse(Map<String, String> response) {
        this.response = response;
        if (response.get("error").isEmpty()) {
            this.isSuccess = Boolean.TRUE;
        }
    }

}
