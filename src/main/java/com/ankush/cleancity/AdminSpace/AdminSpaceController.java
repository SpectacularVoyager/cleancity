package com.ankush.cleancity.AdminSpace;

import com.ankush.cleancity.Query.CompiledQuery;
import com.ankush.cleancity.Query.ComplaintQuery;
import com.ankush.cleancity.Wastes.Waste;
import com.ankush.cleancity.Wastes.WasteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("java/api/adminspace")
@Slf4j
public class AdminSpaceController {
    @Autowired
    JdbcTemplate template;
    WasteMapper wasteMapper = new WasteMapper();


    @GetMapping("markInvalid/{id}")
    public void markInvalid(@PathVariable long id) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("markComplete/{id}")
    public void markComplete(@PathVariable long id) {
        template.update("update wastes set status=? where id=?", "COMPLETE", id);
    }

    @PostMapping("getComplaints")
    public List<Waste> complaints(@RequestBody ComplaintQuery query) {
        CompiledQuery c = query.compile();
        System.out.println(c);
        return template.query(c.getBase(), wasteMapper, c.getValues());
    }

}
