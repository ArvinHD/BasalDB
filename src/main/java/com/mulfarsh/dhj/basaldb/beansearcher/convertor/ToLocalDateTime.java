package com.mulfarsh.dhj.basaldb.beansearcher.convertor;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.zhxu.bs.FieldMeta;
import cn.zhxu.bs.bean.DbType;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.BSConvert;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.Convertors;
import com.mulfarsh.dhj.basaldb.core.datetime.SupplementDateTimeUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ToLocalDateTime extends Convertors {

    static List<DbType> dbTypes = Arrays.asList(DbType.DATE, DbType.DATETIME, DbType.LONG, DbType.STRING, DbType.TIME, DbType.UNKNOWN);

    public ToLocalDateTime(BSConvert annotation, FieldMeta fieldMeta, Class<?> valueType) {
        super(annotation, fieldMeta, valueType);
    }

    @Override
    public Boolean isValidType() {
        DbType dbType = this.getFieldMeta().getDbType();
        if (!dbTypes.contains(dbType)) {
            return false;
        }
        if (!Arrays.asList(getAnnotation().allowTypes()).contains(getValueType())) {
            return false;
        }
        final Field field = this.getFieldMeta().getField();
        final Class<?> targetType = field.getType();
        if (!YearMonth.class.equals(targetType)) {
            return false;
        }
        return true;
    }

    @Override
    public Object getValue(FieldMeta fieldMeta, Object value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        if (getValueType().equals(LocalDate.class)) {
            LocalDate localDate = (LocalDate) value;
            return localDate.atStartOfDay();
        } else if (getValueType().equals(LocalDateTime.class)) {
            return value;
        } else if (getValueType().equals(Date.class)) {
            return LocalDateTimeUtil.of((Date) value);
        } else if (getValueType().equals(Long.class)) {
            return LocalDateTimeUtil.of((Long) value);
        } else if (getValueType().equals(String.class)) {
            return SupplementDateTimeUtil.convertToLocalDateTime((String) value);
        }  else if (getValueType().equals(java.sql.Date.class)) {
            return  ((java.sql.Date) value).toLocalDate().atStartOfDay();
        }
        return null;
    }
}
