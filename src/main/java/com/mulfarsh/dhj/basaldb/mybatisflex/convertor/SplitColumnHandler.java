package com.mulfarsh.dhj.basaldb.mybatisflex.convertor;

import lombok.Getter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SplitColumnHandler extends BaseTypeHandler<List<String>> {

    @Getter
    private  String seq;

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSeq() {
        return seq;
    }

    public static class BackslashSplitHandler extends SplitColumnHandler {

        public BackslashSplitHandler() {
            setSeq("/");
        }
    }

    public static class CommaSplitHander extends SplitColumnHandler {
        public CommaSplitHander() {
            setSeq(",");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null) {
            ps.setString(i, String.join(seq, parameter));
        } else {
            ps.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return splitByComma(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return splitByComma(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return splitByComma(cs.getString(columnIndex));
    }

    private List<String> splitByComma(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return Arrays.asList(string.split(seq));
    }
}
