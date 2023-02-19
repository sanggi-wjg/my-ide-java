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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", nullable = false)
    private Container container;

    @Column(name = "request")
    private String request;

    @Type(JsonType.class)
    @Column(name = "response", columnDefinition = "json")
    private Map<String, Object> response;


    public static CodeSnippet create(Container container, CodeRequest codeRequest, Optional<Member> member){
        CodeSnippetBuilder builder = CodeSnippet.builder()
                .container(container)
                .request(codeRequest.getCode());
        member.ifPresent(builder::member);
        return builder.build();
    }

    public void saveResponse(Map<String, Object> response){
        this.response = response;
    }

}
