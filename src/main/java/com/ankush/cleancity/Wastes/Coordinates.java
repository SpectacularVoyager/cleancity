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
    private long latitude;
    @NotBlank
    private long longitude;
}
