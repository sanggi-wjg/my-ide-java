package com.example.myidejava.core.util.docker.executor;

import com.example.myidejava.domain.docker.Container;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpCodeExecutor implements CodeExecutor {

    public JsonNode execute(Container container, String code) {
        ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
        jsonNodes.put("code", code);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(container.getHttpUrlAddress(), jsonNodes, JsonNode.class);
//        if (response.getStatusCode().equals(200) ){
//        }
        return response.getBody();
    }

}
