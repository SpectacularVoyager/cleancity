package com.ankush.cleancity.ManagerSpace;

import com.ankush.cleancity.Users.AuthUser;
import com.ankush.cleancity.Users.UserMapper;
import com.ankush.cleancity.Utils.Utils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("java/api/managerspace")
@Slf4j

public class ManagerSpace {

    @Autowired
    UserDetailsManager users;
    @Autowired
    JdbcTemplate template;
    UserMapper userMapper = new UserMapper();

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("get")
    public AuthUser get() {
        return getWorker(template);
    }

    @PostMapping("addAdmin")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthUser user) {
        UserDetails details = User.withUsername(user.getUsername()).password(encoder.encode(user.getPassword())).authorities("ADMIN").build();
        if (users.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username", "desc", "USER ALREADY EXISTS"));
        }
        users.createUser(details);
        user.insertDetails(template);
        return ResponseEntity.ok("CREATED ADMIN");
    }

    public AuthUser getWorker(JdbcTemplate template) {
        User user = Utils.getUser();
        return template.queryForObject("select * from UserDetails where username=?", userMapper, user.getUsername());
    }


}
