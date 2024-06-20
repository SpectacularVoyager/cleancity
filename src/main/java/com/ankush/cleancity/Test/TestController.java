package com.ankush.cleancity.Test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("java/api/test/")
public class TestController {
    @GetMapping("ping")
    public String ping() {
        return "PONG";
    }
}
