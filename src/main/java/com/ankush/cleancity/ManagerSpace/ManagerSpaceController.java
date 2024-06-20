package com.ankush.cleancity.ManagerSpace;

import com.ankush.cleancity.AdminSpace.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("java/api/managerspace")
@Slf4j
public class ManagerSpaceController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("addAdmin")
    public void addAdmin(Admin a) {
        throw new UnsupportedOperationException();
    }

}
