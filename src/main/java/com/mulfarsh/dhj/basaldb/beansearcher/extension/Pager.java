package com.mulfarsh.dhj.basaldb.beansearcher.extension;

import cn.hutool.core.collection.CollUtil;
import cn.zhxu.bs.SearchResult;
import cn.zhxu.bs.util.MapBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pager<T> implements Serializable {

    public static final Pager<?> EMPTY = new Pager<>();

    private long page = 1;
    private long size = 0;
    private long totalPage = 1;
    private long totalSize = 0;
    private List<T> records = new ArrayList<>();

    public Pager(List<T> records, Pager pager) {
        this.page = pager.getPage();
        this.size = pager.getSize();
        this.totalPage = pager.getTotalPage();
        this.totalSize = pager.getTotalSize();
        this.records = CollUtil.isNotEmpty(records) ? records : new ArrayList<>();
    }

    public Pager(SearchResult<T> result, MapBuilder.Page page) {
        this(result.getDataList(), page.getPage() + 1, (long) page.getSize(), (long) result.getTotalCount());
    }

    public Pager(List<T> records) {
        this.page = 1;
        this.totalPage = 1;
        if (CollUtil.isNotEmpty(records)) {
            this.size = records.size();
            this.totalSize = records.size();
            this.records = records;
        } else {
            this.size = 0;
            this.totalSize = 0;
            this.records = new ArrayList<>();
        }
    }

    public Pager(List<T> result, Long page, Long size, Long total) {
        this.records = CollUtil.isNotEmpty(result) ? result : new ArrayList<>();
        this.page = page == null ? 1 : page;
        this.size = CollUtil.isNotEmpty(result) ? (long) result.size() : 0;
        this.totalSize = total == null ? 0 : total;
        if (this.totalSize == 0 || size == null || size == 0) {
            this.totalPage = 1;
        } else {
            this.totalPage = (long) Math.ceil((double) total / (double) size);
        }
    }

}
