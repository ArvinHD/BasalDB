package com.mulfarsh.dhj.basaldb.beansearcher.convertor;

import cn.zhxu.bs.FieldMeta;
import cn.zhxu.bs.bean.DbType;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.BSConvert;
import com.mulfarsh.dhj.basaldb.beansearcher.convertor.base.Convertors;

import java.util.Arrays;
import java.util.List;

public class NumberToString extends Convertors {

//    static DbType []dbTypes = new DbType[] { DbType.BOOL, DbType.DECIMAL, DbType.BYTE, DbType.DOUBLE, DbType.FLOAT, DbType.INT, DbType.LONG, DbType.SHORT };
    static List<DbType> dbTypes = Arrays.asList(DbType.BOOL, DbType.DECIMAL, DbType.BYTE, DbType.DOUBLE, DbType.FLOAT, DbType.INT, DbType.LONG, DbType.SHORT);

    public Boolean isValidType() {
        DbType type = this.getFieldMeta().getDbType();
        return dbTypes.contains(type);
    };

    public NumberToString(BSConvert annotation, FieldMeta fieldMeta, Class<?> valueType) {
        super(annotation, fieldMeta, valueType);
    }

    @Override
    public Object getValue(FieldMeta fieldMeta, Object value) {
        return String.valueOf(value);
    }
}
