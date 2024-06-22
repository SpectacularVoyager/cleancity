package com.ankush.cleancity.Wastes;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class WasteMapper implements RowMapper<Waste> {

    @Override
    public Waste mapRow(ResultSet rs, int rowNum) throws SQLException {
        Waste w = new Waste();
        w.setUsername(rs.getString("w.username"));
        w.setId(rs.getLong("w.id"));
        w.setCoordinates(new Coordinates(rs.getFloat("w.latitude"), rs.getFloat("w.longitude")));
        w.setReported(rs.getDate("w.reported"));
        w.setTypes(List.of(rs.getString("types").split(",")));
        w.setLocation(rs.getString("w.location"));
        w.setStatus(rs.getString("w.status"));
        w.setSeverity(rs.getString("w.severity"));

//        boolean dustbinNearby;
        w.setDustbinNearby(rs.getBoolean("w.dustbin_nearby"));
//        boolean dustbinOverflow;
        w.setDustbinOverflow(rs.getBoolean("w.dustbin_overflow"));
//        boolean pmcCleanSite;
        w.setPmcCleanSite(rs.getBoolean("w.pmc_clean_site"));
//        String siteCleanFrequency = "";
        w.setSiteCleanFrequency(rs.getString("w.site_clean_frequency"));
//        String siteUncleanDuration = "";
        w.setSiteCleanFrequency(rs.getString("w.site_unclean_duration"));
//        String wasteRecyclable = "";
        w.setWasteRecyclable(rs.getString("w.waste_recyclable"));
//        String imageURL;
        w.setImageURL(rs.getString("w.complaint_url_image"));
//        String resolvedImageURL;
        w.setResolvedImageURL(rs.getString("w.solved_image_url"));
//        String geohash;
        w.setGeohash(rs.getString("w.geohash"));
//        String invalidComplaintMessage = "";
        w.setInvalidComplaintMessage(rs.getString("w.invalid_complaint_msg"));
//        String siteType;
        w.setSiteType(rs.getString("w.site_type"));
//        private String resolved_id
        w.setResolved_id(rs.getString("w.resolved_id"));
        return w;
    }
}
