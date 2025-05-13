package com.mulfarsh.dhj.basaldb.mybatisflex.condition;

import com.mybatisflex.core.util.LambdaGetter;

public enum DBCondition {

    EQUAL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.Equal(field, value);
        }
    },
    NOT_EQUAL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.NotEqual(field, value);
        }
    },
    IN {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.In(field, value);
        }
    },
    NOT_IN {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.NotIn(field, value);
        }
    },
    GRANT_THEN {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.GrantThen(field, value);
        }
    },
    LESS_THEN {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.LessThen(field, value);
        }
    },
    GRANT_EQUAL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.GrantEqual(field, value);
        }
    },
    LESS_EQUAL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.LessEqual(field, value);
        }
    },
    LEFT_LIKE {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.LeftLike(field, value);
        }
    },
    RIGFHT_LIKE {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.RigfhtLike(field, value);
        }
    },
    LIKE {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.Like(field, value);
        }
    },
    BETWEEN {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.Between(field, value);
        }
    },
    IS_NULL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.IsNull(field);
        }
    },
    NOT_NULL {
        @Override
        public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value) {
            return (D) new DBColumn.NotNull(field);
        }
    };

    abstract public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field, Object value);

    public <D extends DBColumn<T>, T> D generateColumn(LambdaGetter<T> field) {
        return this.generateColumn(field, null);
    }

}
