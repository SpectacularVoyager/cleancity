package com.ankush.cleancity.Test;

import com.ankush.cleancity.Features.Feature.Feature;
import com.ankush.cleancity.Features.FeatureService;
import com.ankush.cleancity.PythonServer.PythonBean;
import com.ankush.cleancity.PythonServer.PythonRESTObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("java/api/test/")
@Slf4j
public class TestController {
    private static final String url = "https://firebasestorage.googleapis.com/v0/b/sugam-59122.appspot.com/o/ComplaintsImages%2F1719157078333IMG_4854%20cropped%202.JPG?alt=media&token=37e98996-250d-4127-9105-bcd35e61d6ab";
    @Autowired
    PythonBean python;
    @Autowired
    FeatureService featureService;

    @GetMapping("ping")
    public String ping() {
        return "PONG";
    }

    @GetMapping("feature/{city}")
    public Optional<String> getFeature(@PathVariable String city, @RequestParam double latitude, @RequestParam double longitude) {
        return featureService.getFromCoords(city, latitude, longitude).map(Feature::getName);
    }

    @GetMapping("python")
    public String python() {
        log.info("LOG");
        try {
            String s = python.getPythonResponse(new PythonRESTObject(url)).getDetected();
            log.info("INFO {}", s);
            return s;
        } catch (Exception e) {
            log.error("ONO", e);
            return null;
        }
    }

    @GetMapping("features")
    public List<String> features() {
        return featureService.getCity("chennai").getFeatures().stream().map(Feature::getName).toList();
    }
}
