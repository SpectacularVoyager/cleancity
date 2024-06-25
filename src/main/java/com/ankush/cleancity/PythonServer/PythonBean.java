package com.ankush.cleancity.PythonServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PythonBean {
    @Autowired
    RestTemplate template;


    public PythonRESTResponse getPythonResponse(PythonRESTObject in) {
        String URL="http://localhost:5000/predict";
        return template.postForObject(URL, in, PythonRESTResponse.class);
    }
}
