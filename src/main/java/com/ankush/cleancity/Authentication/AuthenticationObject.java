package com.ankush.cleancity.Authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthenticationObject {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;
}
