package com.ankush.cleancity.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Setter
@Component
@ToString
public class AuthUser {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @Size(min = 8)
    @NotBlank
    private String phone;

    //    @Size(min = 8)
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    public void insertDetails(JdbcTemplate template) {
        template.update("insert into UserDetails (username,email,phone) values (?,?,?)", username, email, phone);
    }
}
