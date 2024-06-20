package com.ankush.cleancity.Analytics;


import com.ankush.cleancity.Query.NamedParameter;
import com.ankush.cleancity.Query.NamedParameterRowMapper;
import com.ankush.cleancity.Utils.In.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("java/api/analytics")
@Slf4j
public class AnalyticsController {
    @Autowired
    JdbcTemplate template;
    NamedParameterRowMapper issueStatus = new NamedParameterRowMapper("status", "count", Long.class);

    @GetMapping("uniqueUsers")
    public long getUnique() {
        return template.queryForObject("Select count(*) from authorities where authority=?", Long.class, "USER");
    }

    @GetMapping("issues")
    public Map<String, Long> getIssues() {
        Map<String, Long> ret = template.query("select status,count(*) as count from Wastes w group by status", issueStatus).stream().
                collect(Collectors.toMap(
                        NamedParameter::getKey, x -> (Long) x.getVal()
                ));
        ret.putIfAbsent("COMPLETE", 0L);
        ret.putIfAbsent("PENDING", 0L);
        ret.putIfAbsent("TOTAL", ret.values().stream().reduce(0L, Long::sum));
        return ret;
    }

    @GetMapping("workers")
    public long getWorkers() {
        return template.queryForObject("Select count(*) from authorities where not authority=?", Long.class, "USER");
    }

    @GetMapping("footer")
    public Map<String, Long> getFooter() {
        Map<String, Long> map = getIssues();
        return Map.of("uniqueUsers", getUnique(), "complete", map.get("COMPLETE"), "workers", getWorkers(), "complaints",
                map.get("COMPLETE") + map.get("PENDING")
        );
    }

    @GetMapping("summarise")
    public Map<String, Long> summarize(@RequestParam @In(values = {"severity", "status"}) String col) {
        Map<String, Long> map = summariseField(col);
        map.put("TOTAL", map.values().stream().reduce(0L, Long::sum));
        return map;
    }

    @GetMapping("types")
    public Map<String, Float> types() {
        Map<String, Long> map =
                template.query("select type,count(*) as count from WasteType wt group by type", new NamedParameterRowMapper("type", "count", Long.class))
                        .stream().collect(Collectors.toMap(x -> x.getKey(), x -> (Long) x.getVal()));
        float sum = (float) map.values().stream().reduce(0L, Long::sum);
        Map<String, Float> ret = new HashMap<>();
        for (Map.Entry<String, Long> e : map.entrySet()) {
            ret.put(e.getKey(), e.getValue() * 100.0f / sum);
        }
        return ret;
    }


    private Map<String, Long> summariseField(String f) {
        return template.query(String.format("select %s,count(*) as count from Wastes w group by %s", f, f),
                        new NamedParameterRowMapper(f, "count", Long.class)
                ).stream().
                collect(Collectors.toMap(
                        NamedParameter::getKey, x -> (Long) x.getVal()
                ));
    }

}
