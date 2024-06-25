package com.ankush.cleancity.UserSpace;

import com.ankush.cleancity.Features.FeatureService;
import com.ankush.cleancity.PythonServer.PythonBean;
import com.ankush.cleancity.PythonServer.PythonRESTObject;
import com.ankush.cleancity.PythonServer.PythonRESTResponse;
import com.ankush.cleancity.Query.NamedParameter;
import com.ankush.cleancity.Query.NamedParameterRowMapper;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("java/api/userspace")
@Slf4j
public class UserSpaceController {
    @Autowired
    FeatureService featureService;
    @Autowired
    JdbcTemplate template;

    UserMapper userMapper = new UserMapper();
    WasteMapper wasteMapper = new WasteMapper();
    NamedParameterRowMapper issueStatus = new NamedParameterRowMapper("status", "count", Long.class);

    @Autowired
    PythonBean pythonBean;

    public static String getQuery(String where) {
        return String.format("select w.*,GROUP_CONCAT(wt.type) as types from Wastes w right join WasteType wt on w.id = wt.id where " +
                where +
                " group by w.id");
    }

    @PostMapping("complain")
    public ResponseEntity<?> complain(@RequestBody @Valid Waste w) {
        log.info(w.toString());
        AuthUser user = getUser(template);
        w.setUsername(user.getUsername());
        try {
            w.insert(featureService, template);
        } catch (Exception e) {
            log.error("SQL ERROR", e);
            return ResponseEntity.badRequest().body(String.format("COULD NOT ADD %s", w));
        }
        log.info("ADDED {}",w);
        new Thread(() -> {
            PythonRESTResponse res = pythonBean.getPythonResponse(new PythonRESTObject(w.getImageURL()));
            if (res.getDetected().trim().equalsIgnoreCase("clean environment")) {
                w.setInvalidAI(true);
                template.update("update Wastes set invalid_ai=true where id=?", w.getId());
            }
            log.info("WASTE {}", res);
        }).start();
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

    @GetMapping("issueSum")
    public Map<String, Long> getIssues() {
        Map<String, Long> ret = template.query("select status,count(*) as count from Wastes w where username = ? group by status", issueStatus, get().getUsername()).stream().
                collect(Collectors.toMap(
                        NamedParameter::getKey, x -> (Long) x.getVal()
                ));
        ret.putIfAbsent("COMPLETE", 0L);
        ret.putIfAbsent("PENDING", 0L);
        ret.putIfAbsent("TOTAL", ret.values().stream().reduce(0L, Long::sum));
        return ret;
    }


}
