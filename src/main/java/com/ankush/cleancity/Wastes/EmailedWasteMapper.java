package com.ankush.cleancity.Wastes;

import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailedWasteMapper implements RowMapper<EmailedWaste> {
    WasteMapper mapper=new WasteMapper();

    @Override
    public EmailedWaste mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EmailedWaste(mapper.mapRow(rs,rowNum),rs.getString("u.email"));
    }
}
