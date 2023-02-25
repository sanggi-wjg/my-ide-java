package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "코드 실행 Response")
public class CodeResponse {

    @Schema(description = "코드 실행 결과", defaultValue = "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\\n")
    private String output;

    @Schema(description = "코드 실행 에러", defaultValue = "Traceback (most recent call last):\\n  File \\\"/app/app.py\\\", line 23, in run_code\\n    File \\\"<string>\\\", line 1\\n    print([i for i in range(10)])1\\n                                 ^\\nSyntaxError: invalid syntax\\n")
    private String error;

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("output", output);
        map.put("error", error);
        return map;
    }

}
