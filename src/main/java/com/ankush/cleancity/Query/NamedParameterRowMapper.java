package com.ankush.cleancity.Query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@Getter
public class NamedParameterRowMapper implements RowMapper<NamedParameter<?>> {
    private String key;
    private String value;
    private Class<?> clazz;

    @Override
    public NamedParameter<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NamedParameter<>(rs.getString(key), rs.getObject(value,clazz));
    }
}
