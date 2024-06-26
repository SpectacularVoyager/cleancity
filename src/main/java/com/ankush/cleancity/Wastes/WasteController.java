package com.ankush.cleancity.Wastes;

import com.ankush.cleancity.Features.FeatureService;
import com.ankush.cleancity.Utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("java/api/waste")
public class WasteController {
    @Autowired
    JdbcTemplate template;
    @Autowired
    FeatureService service;

    @PostMapping("add")
    public Waste addWaste(@Valid @RequestBody Waste w) {
        w.setUsername("ankush");
        w.insert(service, template);
        return w;
    }

    @GetMapping("complete/{id}")
    public void markComplete(@PathVariable long id) {
        Waste.markComplete(template, id, Utils.getUser().getUsername());
    }

    @GetMapping("test")
    public String test() {
        System.out.println(service);
        return "TEST";
    }

}
