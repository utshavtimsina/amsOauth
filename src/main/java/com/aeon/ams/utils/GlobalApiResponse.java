package com.aeon.ams.utils;


import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Getter
@Setter
@Scope("prototype")
public class GlobalApiResponse implements Serializable {
    private boolean status;
    private Object message;
    private Object data;
    private Object errors;

    public void setResponse(String message, boolean status, Object data){
        this.message = message;
        this.status = status;
        this.data = data;
    }

}
