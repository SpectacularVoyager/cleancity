package com.ankush.cleancity.RootSpace;

import com.ankush.cleancity.AdminSpace.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("java/api/rootspace")
@Slf4j
public class RootSpaceController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("addManager")
    public void addManager(Admin a) {
        throw new UnsupportedOperationException();
    }
}
