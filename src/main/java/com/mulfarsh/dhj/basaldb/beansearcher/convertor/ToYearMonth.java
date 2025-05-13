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

public class ToYearMonth extends Convertors {

    static List<DbType> dbTypes = Arrays.asList(DbType.DATE, DbType.DATETIME, DbType.LONG, DbType.STRING, DbType.TIME, DbType.UNKNOWN);

    public ToYearMonth(BSConvert annotation, FieldMeta fieldMeta, Class<?> valueType) {
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
            return YearMonth.of(localDate.getYear(), localDate.getMonth());
        } else if (getValueType().equals(LocalDateTime.class)) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
        } else if (getValueType().equals(Date.class)) {
            LocalDateTime localDateTime = LocalDateTimeUtil.of((Date) value);
            return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
        } else if (getValueType().equals(Long.class)) {
            LocalDateTime localDateTime = LocalDateTimeUtil.of((Long) value);
            if (localDateTime != null) {
                return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
            }
        } else if (getValueType().equals(String.class)) {
            LocalDateTime localDateTime = SupplementDateTimeUtil.convertToLocalDateTime((String) value);
            if (localDateTime != null) {
                return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
            }
        } else if (getValueType().equals(java.sql.Date.class)) {
            LocalDate localDate = ((java.sql.Date) value).toLocalDate();
            if (localDate != null) {
                return YearMonth.of(localDate.getYear(), localDate.getMonth());
            }
        }
        return null;
    }

}
