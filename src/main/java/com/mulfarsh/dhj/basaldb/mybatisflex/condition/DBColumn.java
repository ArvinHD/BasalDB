package com.mulfarsh.dhj.basaldb.mybatisflex.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.LambdaGetter;
import com.mybatisflex.core.util.LambdaUtil;
import lombok.Data;

import java.util.List;

@Data
abstract public class DBColumn<T> {

    public static <D extends DBColumn<T>, T> D INSTANCE(LambdaGetter<T> field, Object value) {
        return null;
    }

//    public static DBColumn INSTANCE(QueryColumn column, Object value) {
//        return new DBColumn(column, value);
//    }

    public enum ColumnType {
        LAMBDA,
        COLUMN;
    }

    private String fieldName;
    private LambdaGetter<T> field;
    private Object value;
    private QueryColumn column;
    private ColumnType type;

    public DBColumn(LambdaGetter<T> field) {
        this.field = field;
        this.type = ColumnType.LAMBDA;
        this.fieldName = LambdaUtil.getFieldName(field);
    }

    public DBColumn(LambdaGetter<T> field, Object value) {
        this.field = field;
        this.value = value;
        this.type = ColumnType.LAMBDA;
        this.fieldName = LambdaUtil.getFieldName(field);

    }

    public DBColumn(QueryColumn column, Object value) {
        this.column = column;
        this.value = value;
        this.type = ColumnType.COLUMN;
    }


    abstract public void configCondition(QueryWrapper queryWrapper);

    public static class Equal extends DBColumn {

        public Equal(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.eq(getField(), getValue());
        }
    }

    public static class NotEqual extends DBColumn {

        public NotEqual(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.ne(getField(), getValue());
        }
    }

    public static class In extends DBColumn {

        public In(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.in(getField(), (List)getValue());
        }
    }

    public static class NotIn extends DBColumn {

        public NotIn(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.notIn(getField(), getValue());
        }
    }

    public static class GrantThen extends DBColumn {

        public GrantThen(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.gt(getField(), getValue());
        }
    }

    public static class LessThen extends DBColumn {

        public LessThen(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.lt(getField(), getValue());
        }
    }

    public static class GrantEqual extends DBColumn {

        public GrantEqual(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.ge(getField(), getValue());
        }
    }

    public static class LessEqual extends DBColumn {

        public LessEqual(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.le(getField(), getValue());
        }
    }

    public static class LeftLike extends DBColumn {

        public LeftLike(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.likeLeft(getField(), getValue());
        }
    }

    public static class RigfhtLike extends DBColumn {

        public RigfhtLike(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.likeRight(getField(), getValue());
        }
    }

    public static class Like extends DBColumn {

        public Like(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null) {
                return;
            }
            queryWrapper.like(getField(), getValue());
        }
    }

    public static class Between extends DBColumn {

        public Between(LambdaGetter field, Object value) {
            super(field, value);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null || getValue() == null || !(getValue() instanceof List<?>) || ObjUtil.isEmpty(getValue())) {
                return;
            }
            List<Object> list = (List<Object>) getValue();
            queryWrapper.between(getField(), CollUtil.getFirst(list), CollUtil.getLast(list));
        }
    }

    public static class IsNull extends DBColumn {
        public IsNull(LambdaGetter field) {
            super(field);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null) {
                return;
            }
            queryWrapper.isNull(getField());
        }
    }

    public static class NotNull extends DBColumn {
        public NotNull(LambdaGetter field) {
            super(field);
        }

        @Override
        public void configCondition(QueryWrapper queryWrapper) {
            if (getField() == null) {
                return;
            }
            queryWrapper.isNotNull(getField());
        }
    }
}
