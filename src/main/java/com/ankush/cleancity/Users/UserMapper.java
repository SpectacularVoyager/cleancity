package com.ankush.cleancity.Users;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<AuthUser> {
    @Override
    public AuthUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUser user = new AuthUser();
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setUsername(rs.getString("username"));
        return user;
    }
}
