package com.ankush.cleancity.Wastes;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmailedWaste {
    Waste waste;
    String email;
}
