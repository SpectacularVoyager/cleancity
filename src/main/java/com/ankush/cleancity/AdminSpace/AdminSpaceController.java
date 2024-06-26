package com.ankush.cleancity.AdminSpace;

import com.ankush.cleancity.MailStuff.MailBean;
import com.ankush.cleancity.Query.CompiledQuery;
import com.ankush.cleancity.Query.ComplaintQuery;
import com.ankush.cleancity.Utils.Utils;
import com.ankush.cleancity.Wastes.EmailedWaste;
import com.ankush.cleancity.Wastes.EmailedWasteMapper;
import com.ankush.cleancity.Wastes.Waste;
import com.ankush.cleancity.Wastes.WasteMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("java/api/adminspace")
@Slf4j
public class AdminSpaceController {
    @Autowired
    JdbcTemplate template;
    WasteMapper wasteMapper = new WasteMapper();
    EmailedWasteMapper emailedWasteMapper = new EmailedWasteMapper();
    @Autowired
    MailBean mails;


    @PostMapping("markInvalid")
    public void markInvalid(@RequestBody InvalidComplaint complaint) {
        template.update("update Wastes set status=?,invalid_complaint_msg=?,resolved_id=?,resolved_time=? where id=?",
                "COMPLETE", complaint.getMsg(), Utils.getUser().getUsername(), new Timestamp(System.currentTimeMillis()), complaint.getId());
        EmailedWaste w = EmailedWaste.get(template, complaint.getId());
        mails.notifyInvalidPerson(w);

    }

    @NoArgsConstructor
    @Getter
    static final class InvalidComplaint {
        private long id;
        private String msg;
    }

    @PostMapping("markComplete")
    public void markComplete(@RequestBody IDWithUrl id) {
        template.update("update Wastes set status=?,solved_image_url=?,resolved_id=?,resolved_time=? where id=?",
                "COMPLETE", id.url, Utils.getUser().getUsername(), new Timestamp(System.currentTimeMillis()), id.getId());
//        EmailedWaste w = template.queryForObject("select u.email,w.*,group_concat(wt.type) as types from Wastes w right join WasteType wt on wt.id=w.id right join UserDetails u on u.username =w.username where w.id=? group by (wt.id)",
//                emailedWasteMapper,
//                id.getId()
//        );
        EmailedWaste w = EmailedWaste.get(template, id.getId());
        mails.notifyComplaint(w);
    }

    @PostMapping("getComplaints")
    public List<Waste> complaints(@RequestBody ComplaintQuery query) {
        CompiledQuery c = query.compile();
        List<Waste> l = template.query(c.getBase(), wasteMapper, c.getValues());
        return l;
    }

    @GetMapping("default")
    public Waste w() {
        return new Waste();
    }

}
