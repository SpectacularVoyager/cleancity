package com.ankush.cleancity.CheatController;

import com.ankush.cleancity.Users.AuthUser;
import com.ankush.cleancity.Users.UserMapper;
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
@RequestMapping("java/api/cheats")
@Slf4j
public class CheatController {
    @Autowired
    UserDetailsManager users;
    @Autowired
    JdbcTemplate template;
    UserMapper userMapper = new UserMapper();

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("addRoot")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthUser user) {
        UserDetails details = User.withUsername(user.getUsername()).password(encoder.encode(user.getPassword())).authorities("ROOT").build();
        if (users.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username", "desc", "USER ALREADY EXISTS"));
        }
        users.createUser(details);
        user.insertDetails(template);
        return ResponseEntity.ok("CREATED ROOT");
    }
    @GetMapping
    public String test(){
        return "HEY";
    }

}
