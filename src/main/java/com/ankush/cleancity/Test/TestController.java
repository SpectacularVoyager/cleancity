package com.ankush.cleancity.Test;

import com.ankush.cleancity.PythonServer.PythonBean;
import com.ankush.cleancity.PythonServer.PythonRESTObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("java/api/test/")
@Slf4j
public class TestController {
    private static final String url = "https://firebasestorage.googleapis.com/v0/b/sugam-59122.appspot.com/o/ComplaintsImages%2F1719157078333IMG_4854%20cropped%202.JPG?alt=media&token=37e98996-250d-4127-9105-bcd35e61d6ab";
    @Autowired
    PythonBean python;

    @GetMapping("ping")
    public String ping() {
        return "PONG";
    }

    @GetMapping("python")
    public String python() {
        log.info("LOG");
        try {
            String s= python.getPythonResponse(new PythonRESTObject(url)).getPrediction();
            log.info("INFO {}",s);
            return s;
        }catch (Exception e){
            log.error("ONO",e);
            return null;
        }
    }
}
