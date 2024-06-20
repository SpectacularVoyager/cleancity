package com.ankush.cleancity.Wastes;

import com.ankush.cleancity.Features.FeatureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("java/api/waste")
public class WasteController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("add")
    public Waste addWaste(@Valid @RequestBody Waste w) {
        w.setUsername("ankush");
        w.insert(template);
        return w;
    }

    @GetMapping("complete/{id}")
    public void markComplete(@PathVariable long id) {
        Waste.markComplete(template, id);
    }
    @GetMapping("test")
    public String test(){return "TEST";}

}
