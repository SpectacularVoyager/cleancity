package com.ankush.cleancity.Users;

import com.ankush.cleancity.Utils.Utils;
import com.ankush.cleancity.Wastes.Waste;
import com.ankush.cleancity.Wastes.WasteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("java/api/user")
public class UserController {
    @Autowired
    JdbcTemplate template;

    static UserMapper userMapper = new UserMapper();
    WasteMapper wasteMapper = new WasteMapper();

    @GetMapping("get")
    public AuthUser get() {
        return getUser(template);
    }

    @GetMapping("getComplaints")
    public List<Waste> complaints() {
        return template.query("select w.*,GROUP_CONCAT(wt.type) as types from Wastes w right join WasteType wt on w.id = wt.id where w.username=? group by w.id", wasteMapper,
                Utils.getUser().getUsername());
    }
@Deprecated
    public static AuthUser getUser(JdbcTemplate template) {
        User user = Utils.getUser();
        return template.queryForObject("select * from UserDetails where username=?", userMapper, user.getUsername());
    }
}
