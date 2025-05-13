package com.mulfarsh.dhj.basaldb.mybatisflex.basal;

import com.mybatisflex.core.service.IService;

import java.util.Collection;
import java.util.List;

public interface MFBasalBO<IBE extends MFBasalEntity> extends IService<IBE> {

    boolean checkIdExist(IBE entity);

    boolean saveAfterCreate(IBE entity);

    boolean saveBatchAfterCreate(Collection<IBE> entities);

    boolean updateByIdAtferRenewal(IBE entity);

    boolean updateByIdAtferRenewal(IBE entity, String reason);

    boolean updateBatchAtferRenewal(Collection<IBE> entities);

    boolean updateBatchAtferRenewal(List<MFBasalEntity.EntityWithReason> entities);

    boolean removeByIdAfterRenewal(IBE entity);

    boolean removeByIdAfterRenewal(IBE entity, String reason);

    boolean removeByIdsAfterRenewal(Collection<IBE> entities);

    boolean removeByIdsAfterRenewal(List<MFBasalEntity.EntityWithReason> entities);

}
