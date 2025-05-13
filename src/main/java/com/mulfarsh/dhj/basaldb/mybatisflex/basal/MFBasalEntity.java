package com.mulfarsh.dhj.basaldb.mybatisflex.basal;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mulfarsh.dhj.basaldb.core.datetime.CommonDateFormats;
import com.mulfarsh.dhj.basaldb.mybatisflex.external.AutocompleteDataField;
import com.mulfarsh.dhj.basaldb.mybatisflex.external.AutocompleteDataModel;
import com.mulfarsh.dhj.basaldb.mybatisflex.external.AutocompleteField;
import com.mybatisflex.annotation.Id;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.*;

public interface MFBasalEntity extends Serializable {

    @Data
    @Builder
    public static class EntityWithReason {

        public EntityWithReason(MFBasalEntity entity, String reason) {
            this.entity = entity;
            this.reason = reason;
        }

        private MFBasalEntity entity;
        private String reason;
    };

    List<Field> EMPTY_FIELDS = Collections.emptyList();

    default Serializable fetchId() {
        final Field[] fields = ReflectUtil.getFields(this.getClass());
        for (Field field: fields) {
            if (AnnotationUtil.hasAnnotation(field, Id.class)) {
                Object value = ReflectUtil.getFieldValue(this, field);
                if (value != null && value instanceof Serializable) {
                    return (Serializable) value;
                }
            }
        }
        return null;
    }

    default <T extends MFBasalEntity> EntityWithReason configReason(T entity, String reason) {
        return new EntityWithReason(entity, reason);
    }

    default Optional<Object> getUserInfo() { return Optional.empty(); };

    default <T extends MFBasalEntity> Boolean isUpdate(T entity) {
        if (!this.getClass().equals(entity.getClass())) {
            return false;
        }
        final Field[] fieldsDirectly = ReflectUtil.getFieldsDirectly(this.getClass(), false);
        for (Field field : fieldsDirectly) {
            final Object value = ReflectUtil.getFieldValue(this, field);
            final Object reference = ReflectUtil.getFieldValue(entity, field);
            if (!value.equals(reference)) {
                return true;
            }
        }
        return false;
    };

    default <T extends MFBasalEntity> void configUpdate(T entity) {
        if (!isUpdate(entity)) {
            return;
        }
        doConfigUpdate(entity);
    };

    default <T extends MFBasalEntity> void configUpdate(T entity, Boolean needCheck) {
        if (needCheck) {
            if (!isUpdate(entity)) {
                return;
            }
        }
        doConfigUpdate(entity);
    };

    default <T extends MFBasalEntity> void configUpdateWithoutCheck(T entity) {
        doConfigUpdate(entity);
    };

    default void doCreate() {
        final Map<AutocompleteField, List<Field>> autocompleteFieldListMap = parserModel();
        LocalDateTime now = LocalDateTime.now();
        List<Field> creatTimes = autocompleteFieldListMap.getOrDefault(AutocompleteField.CREATE_TIME, EMPTY_FIELDS);
        creatTimes.forEach(field -> setCreateTimeValue(field, now));
        List<Field> updateTimes = autocompleteFieldListMap.getOrDefault(AutocompleteField.UPDATE_TIME, EMPTY_FIELDS);
        updateTimes.forEach(field -> setUpdateTimeValue(field, now));
        List<Field> versions = autocompleteFieldListMap.getOrDefault(AutocompleteField.VERSION, EMPTY_FIELDS);
        versions.forEach(field -> setVersionValue(field, 1));
        getUserInfo().ifPresent(value -> {
            List<Field> creators = autocompleteFieldListMap.getOrDefault(AutocompleteField.CREATOR, EMPTY_FIELDS);
            creators.forEach(field -> setCreatorValue(field, value));
            List<Field> regenerators = autocompleteFieldListMap.getOrDefault(AutocompleteField.REGENERATOR, EMPTY_FIELDS);
            regenerators.forEach(field -> setRegeneratorValue(field, value));
        });
    }

    default <T extends MFBasalEntity> T doCreateAndGet() {
        this.doCreate();
        return (T) this;
    }

    default void doUpdate() {
        this.doUpdate("更新原因信息缺失--ERROR");
    }

    default void doUpdate(String reason) {
        final Map<AutocompleteField, List<Field>> autocompleteFieldListMap = parserModel();
        LocalDateTime now = LocalDateTime.now();
        List<Field> updateTimes = autocompleteFieldListMap.getOrDefault(AutocompleteField.UPDATE_TIME, EMPTY_FIELDS);
        updateTimes.forEach(field -> setUpdateTimeValue(field, now));
        List<Field> versions = autocompleteFieldListMap.getOrDefault(AutocompleteField.VERSION, EMPTY_FIELDS);
        versions.forEach(field -> updateVersion(field));
        getUserInfo().ifPresent(value -> {
            List<Field> regenerators = autocompleteFieldListMap.getOrDefault(AutocompleteField.REGENERATOR, EMPTY_FIELDS);
            regenerators.forEach(field -> setRegeneratorValue(field, value));
        });
        if (StrUtil.isNotEmpty(reason)) {
            List<Field> updateReasons = autocompleteFieldListMap.getOrDefault(AutocompleteField.UPDATE_REASON, EMPTY_FIELDS);
            updateReasons.forEach(field -> setUpdateReasonValue(field, reason));
        }

    }

    default <T extends MFBasalEntity> T  doUpdateAndGet() {
        this.doUpdate();
        return (T) this;
    }

    default <T extends MFBasalEntity> T doUpdateAndGet(String reason) {
        this.doUpdate(reason);
        return (T) this;
    }

    default void doLogicDelete() {
        this.doLogicDelete("删除原因缺失--ERROR");
    }

    default void doLogicDelete(String reason) {
        final Map<AutocompleteField, List<Field>> autocompleteFieldListMap = parserModel();
        LocalDateTime now = LocalDateTime.now();
        List<Field> updateTimes = autocompleteFieldListMap.getOrDefault(AutocompleteField.UPDATE_TIME, EMPTY_FIELDS);
        updateTimes.forEach(field -> setUpdateTimeValue(field, now));
        List<Field> deleteTimes = autocompleteFieldListMap.getOrDefault(AutocompleteField.DELETE_TIME, EMPTY_FIELDS);
        deleteTimes.forEach(field -> setDeleteTimeValue(field, now));
        List<Field> deleteds = autocompleteFieldListMap.getOrDefault(AutocompleteField.DELETED, EMPTY_FIELDS);
        deleteds.forEach(field -> setDeletedValue(field));
        List<Field> versions = autocompleteFieldListMap.getOrDefault(AutocompleteField.VERSION, EMPTY_FIELDS);
        versions.forEach(field -> updateVersion(field));
        getUserInfo().ifPresent(value -> {
            List<Field> regenerators = autocompleteFieldListMap.getOrDefault(AutocompleteField.REGENERATOR, EMPTY_FIELDS);
            regenerators.forEach(field -> setRegeneratorValue(field, value));
            List<Field> deleters = autocompleteFieldListMap.getOrDefault(AutocompleteField.DELETER, EMPTY_FIELDS);
            deleters.forEach(field -> setDeleterValue(field, value));
        });
        if (StrUtil.isNotEmpty(reason)) {
            List<Field> updateReasons = autocompleteFieldListMap.getOrDefault(AutocompleteField.DELETE_REASON, EMPTY_FIELDS);
            updateReasons.forEach(field -> setDeleteReasonValue(field, reason));
        }
    }

    default <T extends MFBasalEntity> T doLogicDeleteAndGet() {
        this.doLogicDelete();
        return (T) this;
    }

    default <T extends MFBasalEntity> T doLogicDeleteAndGet(String reason) {
        this.doLogicDelete(reason);
        return (T) this;
    }

    default void setCreatorValue(Field field, Object value) {
        this.setFieldValue(field, value);
    }

    default void setCreateTimeValue(Field field, LocalDateTime dateTime) {
        this.setDateTimeValue(field, dateTime);
    }

    default void setRegeneratorValue(Field field, Object value) {
        this.setFieldValue(field, value);
    }

    default void setUpdateTimeValue(Field field, LocalDateTime dateTime) {
        this.setDateTimeValue(field, dateTime);
    }

    default void setUpdateReasonValue(Field field, Object value) {
        this.setFieldValue(field, value);
    }

    default void setDeleterValue(Field field, Object value) {
        this.setFieldValue(field, value);
    }

    default void setDeletedValue(Field field) {
        if (field.getType().equals(Integer.class)) {
            this.setFieldValue(field, 1);
        } else if (field.getType().equals(Boolean.class)) {
            this.setFieldValue(field, true);
        } else if (field.getType().equals(String.class)) {
            this.setFieldValue(field, "true");
        }
    }

    default void setDeleteTimeValue(Field field, LocalDateTime dateTime) {
        this.setDateTimeValue(field, dateTime);
    }

    default void setDeleteReasonValue(Field field, Object value) {
        this.setFieldValue(field, value);
    }

    default void setVersionValue(Field field, Number ver) {
        Object value = ReflectUtil.getFieldValue(this, field);
        final Class<?> type = field.getType();
        if (Number.class.isAssignableFrom(type)) {
            value = ver;
        } else if (type.equals(String.class)) {
            value = String.valueOf(ver);
        }
        this.setFieldValue(field, value);
    }

    default void updateVersion(Field field) {
        this.updateVersionStep(field, 1);
    }

    default void updateVersionStep(Field field, Number step) {
        final Class<?> type = field.getType();
        if (Number.class.isAssignableFrom(type)) {
            Number fieldValue = (Number) ReflectUtil.getFieldValue(this, field);
            Number ver = fieldValue == null ? 0 : fieldValue;;
            final BigDecimal add = NumberUtil.add(ver, step);
            setFieldValue(field, add);
        } else if (type.equals(String.class)) {
            String fieldValue = (String)  ReflectUtil.getFieldValue(this, field);
            BigDecimal bigDecimal = fieldValue == null ? new BigDecimal("0") : NumberUtil.toBigDecimal(fieldValue);
            final BigDecimal add = NumberUtil.add(bigDecimal, step);
            setFieldValue(field, add);
        }
    }

    default Map<AutocompleteField, List<Field>> parserModel() {
        final Class<? extends MFBasalEntity> tClass = this.getClass();
        final AutocompleteDataModel aDataModel = tClass.getAnnotation(AutocompleteDataModel.class);
        if (aDataModel == null) {
            return null;
        }
        if (!aDataModel.turnOn()) {
            return null;
        }
        Map<AutocompleteField, List<Field>> result = new HashMap<>();
        Field []fields = ReflectUtil.getFields(tClass);
        for (Field field: fields) {
            if (AnnotationUtil.hasAnnotation(field, AutocompleteDataField.class)) {
                AutocompleteDataField dataField = AnnotationUtil.getAnnotation(field, AutocompleteDataField.class);
                if (!dataField.turnOn() || dataField.fieldType() == AutocompleteField.IGNORE) {
                    continue;
                }
                final List<Field> orDefault = result.getOrDefault(dataField.fieldType(), new ArrayList<>());
                orDefault.add(field);
                result.put(dataField.fieldType(), orDefault);
            }
        }
        if (CollUtil.isEmpty(result)) {
            return null;
        }
        return result;
    }

    default void setFieldValue(Field field, Object value) {
        final Class<?> type = field.getType();
        if (type.isInstance(value)) {
            ReflectUtil.setFieldValue(this, field, value);
        }
    }

    default void setDateTimeValue(Field field, LocalDateTime dateTime) {
        final Class<?> type = field.getType();
        if (type.equals(LocalDateTime.class)) {
            ReflectUtil.setFieldValue(this, field, dateTime);
            return;
        }

        if (type.equals(String.class)) {
            final String format = LocalDateTimeUtil.format(dateTime, CommonDateFormats.Pattern.YYYY_MM_DD_HH_MM_SS_PATTERN);
            ReflectUtil.setFieldValue(this, field, format);
            return;
        }

        if (type.equals(LocalDate.class)) {
            final LocalDate localDate = dateTime.toLocalDate();
            ReflectUtil.setFieldValue(this, field, localDate);
            return;
        }

        if (type.equals(YearMonth.class)) {
            final YearMonth yearMonth = YearMonth.of(dateTime.getYear(), dateTime.getMonth().getValue());
            ReflectUtil.setFieldValue(this, field, yearMonth);
            return;
        }

        if (type.equals(Long.class)) {
            final long epochSecond = dateTime.toInstant(ZoneOffset.of("+08:00")).getEpochSecond();
            ReflectUtil.setFieldValue(this, field, epochSecond);
        }
    }

    default <T extends MFBasalEntity> void doConfigUpdate(T entity) {
        final Field[] fieldsDirectly = ReflectUtil.getFieldsDirectly(this.getClass(), false);
        for (Field field : fieldsDirectly) {
            final Object value = ReflectUtil.getFieldValue(this, field);
            final Object reference = ReflectUtil.getFieldValue(entity, field);
            if (value.equals(reference)) {
                continue;
            }
            ReflectUtil.setFieldValue(this, field, reference);
        }
    };
}
