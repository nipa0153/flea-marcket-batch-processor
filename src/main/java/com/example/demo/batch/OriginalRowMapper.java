// package com.example.demo.batch;

// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.RowMapper;
// import org.springframework.stereotype.Repository;

// import com.example.demo.domain.Original;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.List;

// public class OriginalRowMapper implements RowMapper<Original> {

// public static final String ID_COLUMN = "id";
// public static final String CATEGORY_NAME_COLUMN = "category_name";

// public Original mapRow(ResultSet rs, int rowNum) throws SQLException {
// Original original = new Original();

// original.setId(rs.getInt(ID_COLUMN));
// original.setCategoryName(rs.getString(CATEGORY_NAME_COLUMN));

// return original;
// }

// JdbcTemplate template = new JdbcTemplate(dataSource);
// List originalList = template.query("SELECT ID, CATEGORY_NAME FROM ORIGINALS",
// new OriginalRowMapper());

// }
