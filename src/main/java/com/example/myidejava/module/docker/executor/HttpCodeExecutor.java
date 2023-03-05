package com.example.myidejava.module.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpCodeExecutor extends ContainerCodeExecutor {

    public CodeResponse execute(Container container, CodeRequest codeRequest) {
        if (!container.isTypeHttp()) {
            throw new IllegalStateException("todo : !container.isTypeHttp()");
        }

        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.put("code", codeRequest.getCode());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(container.getHttpUrlAddress(), jsonNodes, JsonNode.class);

        JsonNode body = response.getBody();
        return CodeResponse.builder()
                .output(body.get("output").textValue())
                .error(body.get("error").textValue())
                .build();
    }

}
