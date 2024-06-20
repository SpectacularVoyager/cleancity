package com.ankush.cleancity.Query;

import com.ankush.cleancity.UserSpace.UserSpaceController;
import com.ankush.cleancity.Utils.In.In;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ComplaintQuery {
    private List<@In(values = {"HIGH", "MEDIUM", "LOW"}) String> severity;
    private List<@In(values = {"COMPLETE", "PENDING"}) String> status;
    private List<@In(values = {"DRY","PLANT","CLOTHES", "WET","CONSTRUCTION","MEDICAL","SANITARY"}) String> wasteType;
    @NotBlank
    private String location;
    private Date time1;
    private Date time2;

    public CompiledQuery compile() {
        List<Object> objects = new ArrayList<>();
        List<String> where = new ArrayList<>();
        addInCond("w.severity", objects, where, severity);
        addInCond("w.status", objects, where, status);
        addInCond("wt.type", objects, where, wasteType);
        addEqualsString("w.location", objects, where, location);
        addDateCond("w.reported",objects,where, time1, time2);
        return new CompiledQuery(UserSpaceController.getQuery(String.join(" AND ", where)), objects.toArray());
    }

    private static void addDateCond(String col, List<Object> objects, List<String> where, Date d1, Date d2) {
        if (d1 == null && d2 == null) return;
        if (d2 == null) {
            where.add(String.format(" where %s > ? ", col));
            objects.add(d1);
        } else if (d1 == null) {
            where.add(String.format(" where %s < ? ", col));
            objects.add(d2);
        } else {
            where.add(String.format(" where %s between ? and ? ", col));
            objects.add(d1);
            objects.add(d2);
        }
    }

    private static void addInCond(String col, List<Object> objects, List<String> where, List<?> vals) {
        if (vals == null) return;
        if (!vals.isEmpty()) {
            where.add(getIn(col, vals));
            objects.addAll(vals);
        }
    }

    private static void addEqualsString(String col, List<Object> objects, List<String> where, String val) {
        if (val == null) return;
        if (!val.isEmpty()) {
            where.add(String.format(" %s = ? ", col));
            objects.add(val);
        }
    }

    private static String getIn(String col, List<?> vals) {
        return String.format("%s in (%s)", col, String.join(",", Collections.nCopies(vals.size(), "?")));
    }
}
