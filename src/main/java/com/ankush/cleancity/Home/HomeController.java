package com.ankush.cleancity.Home;

import com.ankush.cleancity.Users.AuthUser;
import com.ankush.cleancity.Users.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("java/api/home")
@SessionAttributes("user")
@Slf4j
public class HomeController {
    @Autowired
    JdbcTemplate template;

    @GetMapping("hello")
    public String home() {
        AuthUser user = (UserController.getUser(template));
        log.info(String.valueOf(user));
        return "Hello," + user.getUsername();
    }
}
