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
        w.setCoordinates(new Coordinates(rs.getLong("w.latitude"), rs.getLong("w.longitude")));
        w.setReported(rs.getDate("w.reported"));
        w.setTypes(List.of(rs.getString("types").split(",")));
        w.setLocation(rs.getString("w.location"));
        w.setStatus(rs.getString("w.status"));
        w.setSeverity(rs.getString("w.severity"));
        return w;
    }
}
