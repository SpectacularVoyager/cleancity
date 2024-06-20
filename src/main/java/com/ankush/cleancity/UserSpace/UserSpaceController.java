package com.ankush.cleancity.UserSpace;

import com.ankush.cleancity.Users.AuthUser;
import com.ankush.cleancity.Users.UserController;
import com.ankush.cleancity.Users.UserMapper;
import com.ankush.cleancity.Utils.In.In;
import com.ankush.cleancity.Utils.Utils;
import com.ankush.cleancity.Wastes.Waste;
import com.ankush.cleancity.Wastes.WasteMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("java/api/userspace")
@Slf4j
public class UserSpaceController {
    @Autowired
    JdbcTemplate template;

    UserMapper userMapper = new UserMapper();
    WasteMapper wasteMapper = new WasteMapper();


    public static String getQuery(String where) {
        return String.format("select w.*,GROUP_CONCAT(wt.type) as types from Wastes w right join WasteType wt on w.id = wt.id where " +
                where +
                " group by w.id");
    }

    @PostMapping("complain")
    public ResponseEntity<?> complain(@Valid Waste w) {
        AuthUser user = getUser(template);
        w.setUsername(user.getUsername());
        try {
            w.insert(template);
        } catch (Exception e) {
            log.error("SQL ERROR", e);
            return ResponseEntity.badRequest().body(String.format("COULD NOT ADD %s", w));
        }
        return ResponseEntity.ok(w);
    }

    public AuthUser getUser(JdbcTemplate template) {
        User user = Utils.getUser();
        return template.queryForObject("select * from UserDetails where username=?", userMapper, user.getUsername());
    }

    @GetMapping("complaints")
    public List<Waste> complaints() {
        return template.query(
                getQuery("w.username=?"), wasteMapper,
                Utils.getUser().getUsername());

    }

    @GetMapping("complaints/{id}")
    public Optional<Waste> complaint(@PathVariable long id) {
        return template.query(
                getQuery("w.username=? and w.id=?"), wasteMapper,
                Utils.getUser().getUsername(), id).stream().findFirst();

    }

    @GetMapping("complaintStatus")
    public List<Waste> complaintsStatus(@RequestParam @In(values = {"COMPLETE", "PENDING"}) String status) {
        return template.query(
                getQuery("w.username=? and status=?"), wasteMapper,
                Utils.getUser().getUsername(), status);

    }

    @GetMapping("get")
    public AuthUser get() {
        return getUser(template);
    }


}
