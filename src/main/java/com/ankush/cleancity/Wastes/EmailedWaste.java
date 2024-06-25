package com.ankush.cleancity.Wastes;

import lombok.*;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmailedWaste {
    Waste waste;
    String email;
    private static final EmailedWasteMapper emailedWasteMapper = new EmailedWasteMapper();

    public static EmailedWaste get(JdbcTemplate template, long id) {
        return template.queryForObject("select u.email,w.*,group_concat(wt.type) as types from Wastes w right join WasteType wt on wt.id=w.id right join UserDetails u on u.username =w.username where w.id=? group by (wt.id)",
                emailedWasteMapper,
                id
        );

    }
}
