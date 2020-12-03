package com.aeon.ams.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private boolean status;
    private int httpCode;
    @JsonProperty("error")
    private String message;
    @JsonProperty("message")
    private List<String> errors;

    public ApiError(boolean status, final int httpCode, final String message, final String error) {
        super();
        this.status = status;
        this.httpCode = httpCode;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
