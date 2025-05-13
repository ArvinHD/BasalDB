package com.mulfarsh.dhj.basaldb.mybatisflex.convertor;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.YearMonth;

public class YearMonthDateHandler extends BaseTypeHandler<YearMonth> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, YearMonth parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, Date.valueOf(parameter.atEndOfMonth()));
    }

    @Override
    public YearMonth getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Date sqlDate = rs.getDate(columnName);
        if (sqlDate != null) {
            return YearMonth.from(sqlDate.toLocalDate());
        }
        return null;
    }

    @Override
    public YearMonth getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Date sqlDate = rs.getDate(columnIndex);
        if (sqlDate != null) {
            return YearMonth.from(sqlDate.toLocalDate());
        }
        return null;
    }

    @Override
    public YearMonth getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Date sqlDate = cs.getDate(columnIndex);
        if (sqlDate != null) {
            return YearMonth.from(sqlDate.toLocalDate());
        }
        return null;
    }

}
