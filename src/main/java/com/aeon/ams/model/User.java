package com.aeon.ams.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @NotBlank(message = "UserName is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    private  boolean isActive = false;
    private  String activators;
    private List<Roles> roles;

}
