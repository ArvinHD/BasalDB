package com.mulfarsh.dhj.basaldb.mybatisflex.basal;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.TypeUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MFBasalBOImpl<IBM extends MFBasalMapper<IBE>, IBE extends MFBasalEntity> extends ServiceImpl<IBM, IBE> implements MFBasalBO<IBE> {

    private Class<IBE> eClass = (Class<IBE>) TypeUtil.getTypeArgument(this.getClass(), 1);

    @Override
    public boolean checkIdExist(IBE entity) {
        final Serializable serializable = entity.fetchId();
        if (serializable == null) {
            return false;
        }
        final IBE byId = getById(serializable);
        return byId != null;
    }

    @Override
    public boolean saveAfterCreate(IBE entity) {
        if (checkIdExist(entity)) {
            return false;
        }
        entity.doCreate();
        return super.save(entity);
    }

    @Override
    public boolean saveBatchAfterCreate(Collection<IBE> entities) {
        List<IBE> finalEntities = new ArrayList<>();
        entities.forEach(e -> {
            if (checkIdExist(e)) {
                return;
            }
            e.doCreate();
            finalEntities.add(e);
        });
        if (CollUtil.isEmpty(finalEntities)) {
            return false;
        }
        return super.saveBatch(finalEntities);
    }

    @Override
    public boolean updateByIdAtferRenewal(IBE entity) {
        if (checkIdExist(entity)) {
            entity.doUpdate();
            return super.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean updateByIdAtferRenewal(IBE entity, String reason) {
        if (checkIdExist(entity)) {
            entity.doUpdate(reason);
            return super.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean updateBatchAtferRenewal(Collection<IBE> entities) {
        List<IBE> finalEntities = new ArrayList<>();
        entities.forEach(e -> {
            if (checkIdExist(e)) {
                e.doUpdate();
                finalEntities.add(e);
            }
        });
        if (CollUtil.isEmpty(finalEntities)) {
            return false;
        }
        return super.updateBatch(finalEntities);
    }

    @Override
    public boolean updateBatchAtferRenewal(List<MFBasalEntity.EntityWithReason> entities) {
        List<IBE> finalEntities = new ArrayList<>();
        entities.forEach(e -> {
            if (!eClass.isInstance(e.getEntity())) {
                return;
            }
            IBE entity = (IBE) e.getEntity();
            if (checkIdExist(entity)) {
                entity.doUpdate(e.getReason());
                finalEntities.add(entity);
            }
        });
        if (CollUtil.isEmpty(finalEntities)) {
            return false;
        }
        return super.updateBatch(finalEntities);
    }

    @Override
    public boolean removeByIdAfterRenewal(IBE entity) {
        if (checkIdExist(entity)) {
            entity.doLogicDelete();
            return super.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean removeByIdAfterRenewal(IBE entity, String reason) {
        if (checkIdExist(entity)) {
            entity.doLogicDelete(reason);
            return super.updateById(entity);
        }
        return false;
    }

    @Override
    public boolean removeByIdsAfterRenewal(Collection<IBE> entities) {
        List<IBE> finalEntities = new ArrayList<>();
        entities.forEach(e -> {
            if (checkIdExist(e)) {
                e.doLogicDelete();
                finalEntities.add(e);
            }
        });
        if (CollUtil.isEmpty(finalEntities)) {
            return false;
        }
        return super.updateBatch(entities);
    }

    @Override
    public boolean removeByIdsAfterRenewal(List<MFBasalEntity.EntityWithReason> entities) {
        List<IBE> finalEntities = new ArrayList<>();
        entities.forEach(e -> {
            if (!eClass.isInstance(e.getEntity())) {
                return;
            }
            IBE entity = (IBE) e.getEntity();
            if (checkIdExist(entity)) {
                entity.doLogicDelete(e.getReason());
                finalEntities.add(entity);
            }
        });
        if (CollUtil.isEmpty(finalEntities)) {
            return false;
        }
        return super.updateBatch(finalEntities);
    }
}
