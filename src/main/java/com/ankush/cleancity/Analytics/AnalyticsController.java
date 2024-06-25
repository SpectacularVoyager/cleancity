package com.ankush.cleancity.Analytics;


import com.ankush.cleancity.Query.NamedParameter;
import com.ankush.cleancity.Query.NamedParameterRowMapper;
import com.ankush.cleancity.Utils.In.In;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    class r {
        Timestamp date;
        int resolved, pending;

        public r(Timestamp date) {
            this.date = date;
        }

        public int getTotal() {
            return resolved + pending;
        }
    }

    @GetMapping("res_tot")
    public List<r> total() {
        Date[] max = new Date[1];
        max[0] = new Date(0);
        Map<Date, r> list =
                template.query("select reported,GROUP_CONCAT(status) as status ,GROUP_CONCAT(count) as count from (select date(reported) as reported,status,COUNT(*) as count from Wastes w group by date(reported),status  limit 14) a GROUP BY reported order by reported desc"
                        , (rs, rowNum) -> {
                            r _r = new r(rs.getTimestamp("reported"));
                            max[0] = new Date(Math.max(max[0].getTime(), _r.getDate().getTime()));
                            String[] cols = rs.getString("status").split(",");
                            List<Integer> vals = Arrays.stream(rs.getString("count").split(",")).map(Integer::parseInt).toList();
                            for (int i = 0; i < cols.length; i++) {
                                if (cols[i].equalsIgnoreCase("COMPLETE")) {
                                    _r.setResolved(vals.get(i));
                                }
                                if (cols[i].equalsIgnoreCase("PENDING")) {
                                    _r.setPending(vals.get(i));
                                }
                            }
                            return _r;
                        }
                ).stream().collect(Collectors.toMap(x -> x.date, x -> x));
        if (list.isEmpty()) {
            Date d = new Date();
            List<r> l = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                l.add(new r(new Timestamp(d.getTime()), 0, 0));
            }
            return l.stream().sorted(Comparator.comparingLong(x -> x.date.getTime())).toList();
        }
        Date d = list.get(max[0]).getDate();

        for (int i = 0; i < 14; i++) {
            list.putIfAbsent(d, new r(new Timestamp(d.getTime()), 0, 0));
            d = previousDateString(d);
        }
        return list.values().stream().sorted(Comparator.comparingLong(x -> x.date.getTime())).limit(14).toList();
    }

    private static Date previousDateString(Date myDate) {

        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        // Use the date formatter to produce a formatted date string
        return calendar.getTime();
    }

    @GetMapping("types")
    public Map<String, Float> types() {
        Map<String, Long> map =
                template.query("select type,count(*) as count from WasteType wt group by type", new NamedParameterRowMapper("type", "count", Long.class))
                        .stream().collect(Collectors.toMap(NamedParameter::getKey, x -> (Long) x.getVal()));
        float sum = (float) map.values().stream().reduce(0L, Long::sum);
        Map<String, Float> ret = new HashMap<>();
        if (sum != 0) {
            for (Map.Entry<String, Long> e : map.entrySet()) {
                ret.put(e.getKey(), e.getValue() * 100.0f / sum);
            }
        }
        ret.putIfAbsent("DRY", 0f);
        ret.putIfAbsent("PLANT", 0f);
        ret.putIfAbsent("CONSTRUCTION", 0f);
        ret.putIfAbsent("WET", 0f);
        ret.putIfAbsent("CLOTHES", 0f);
        ret.putIfAbsent("SANITARY", 0f);
        ret.putIfAbsent("MEDICAL", 0f);
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
