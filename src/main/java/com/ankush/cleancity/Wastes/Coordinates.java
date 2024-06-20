package com.ankush.cleancity.Wastes;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Coordinates {
    @NotBlank
    private float latitude;
    @NotBlank
    private float longitude;
}
