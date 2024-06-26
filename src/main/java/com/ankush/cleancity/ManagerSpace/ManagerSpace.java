package com.ankush.cleancity.ManagerSpace;

import com.ankush.cleancity.Users.AuthUser;
import com.ankush.cleancity.Users.UserMapper;
import com.ankush.cleancity.Utils.Utils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        UserDetails details = User.withUsername(user.getUsername()).password(encoder.encode(user.getPassword())).authorities("ADMIN", "USER").build();
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static final class EmployeeLog {
        String emp;
        long val;
    }

    @GetMapping("getWorkerStats")
    public List<EmployeeLog> getstats() {
        String sql = "select u.username,count(w.id) as count from (select u.username from users u right join authorities a on a.username =u.username where a.authority =\"ADMIN\") u left join Wastes w on u.username =w.resolved_id group by u.username";

        return template.query(sql, (rs, rowNum) -> new EmployeeLog(rs.getString("username"), rs.getLong("count")));
    }

    @GetMapping("getManagers")
    public List<AuthUser> getManagers() {
        return template.query("select ud.* from authorities a left join UserDetails ud on ud.username =a.username where a.authority in('MANAGER')", userMapper);
    }

//    select w.resolved_id ,count(w.resolved_id) from Wastes w left join users u on u.username =w.username inner join UserDetails ud on ud.username =u.username
//    where w.resolved_id is not null
//    group by w.resolved_id


}
