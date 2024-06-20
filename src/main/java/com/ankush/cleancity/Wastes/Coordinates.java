package com.ankush.cleancity.Wastes;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Coordinates {
    @JsonProperty("longitude")
    @NotBlank
    private float latitude;
    @JsonProperty("latitude")
    @NotBlank
    private float longitude;
}
