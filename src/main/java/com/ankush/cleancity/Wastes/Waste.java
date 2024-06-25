package com.ankush.cleancity.Wastes;

import com.ankush.cleancity.Features.Feature.Feature;
import com.ankush.cleancity.Features.FeatureService;
import com.ankush.cleancity.Utils.In.In;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class Waste {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String username;

    private String location;
    @JsonUnwrapped
    private Coordinates coordinates = new Coordinates(0, 0);
    @In(values = {"HIGH", "MEDIUM", "LOW"})
    private String severity;
    @In(values = {"COMPLETE", "PENDING"})
    private String status;
    private Date reported;
    private List<@In(values = {"DRY", "PLANT", "CLOTHES", "WET", "CONSTRUCTION", "MEDICAL", "SANITARY"}) String> types = new ArrayList<>();

    boolean dustbinNearby;
    boolean dustbinOverflow;
    boolean pmcCleanSite;
    String siteCleanFrequency = "";
    String siteUncleanDuration = "";
    String wasteRecyclable = "";
    String imageURL;
    String resolvedImageURL;
    String geohash;
    String invalidComplaintMessage = "";
    String siteType;
    private String resolved_id;
    private boolean invalidAI;


    public void insert(FeatureService featureService, JdbcTemplate template) {
        this.status = "PENDING";
        this.reported = new Date(System.currentTimeMillis());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template).withTableName("Wastes").usingColumns(
                        "username", "location", "latitude", "longitude", "severity", "status", "reported",
                        "dustbin_nearby", "dustbin_overflow", "pmc_clean_site", "site_clean_frequency", "site_unclean_duration",
                        "description", "complaint_url_image", "solved_image_url", "geohash", "invalid_complaint_msg", "site_type",
                        "resolved_id","invalid_ai"
                )
                .usingGeneratedKeyColumns("id");
        ;
        Map<String, Object> values = new HashMap<>();

        this.location = featureService.getFromCoords("pune", coordinates.getLatitude(), coordinates.getLongitude()).map(Feature::getName).orElse("NOT IN WARD");
//        values.put("id", 10);
        log.info(this.toString());
        values.put("username", username);
        values.put("location", location);
        values.put("latitude", coordinates.getLatitude());
        values.put("longitude", coordinates.getLongitude());
        values.put("severity", severity);
        values.put("status", status);
        values.put("reported", reported);
        values.put("dustbin_nearby", dustbinNearby);
        values.put("dustbin_overflow", dustbinOverflow);
        values.put("pmc_clean_site", pmcCleanSite);
        values.put("site_clean_frequency", siteCleanFrequency);
        values.put("site_unclean_duration", siteUncleanDuration);
        values.put("description", wasteRecyclable);
        values.put("complaint_url_image", imageURL);
        values.put("solved_image_url", resolvedImageURL);
        values.put("geohash", geohash);
        values.put("invalid_complaint_msg", invalidComplaintMessage);
        values.put("site_type", siteType);
        values.put("resolved_id", resolved_id);
        values.put("invalid_ai", invalidAI);

        simpleJdbcInsert.compile();
        id = simpleJdbcInsert.executeAndReturnKey(values).longValue();
        for (String x : types) {
            template.update("insert into WasteType (id,type) values (?,?)", id, x);
        }
    }

//    public void markComplete(JdbcTemplate template) {
//        markComplete(template, this.id);
//    }

    public static void markComplete(JdbcTemplate template, long id, String res_id) {
        template.update("update Wastes set status=?,resolved_id=? where id=?", "COMPLETE", id, res_id);
    }

}
