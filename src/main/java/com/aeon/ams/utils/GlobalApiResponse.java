package com.aeon.ams.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Scope("prototype")
public class GlobalApiResponse implements Serializable {
    private boolean status;
    private String message;

    private Object data;
    private Object errors;

    public void setResponse(String message, boolean status, Object data){
        this.message = message;
        this.status = status;
        this.data = data;
    }

}
