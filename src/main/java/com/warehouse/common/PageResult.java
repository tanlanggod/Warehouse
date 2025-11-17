package com.warehouse.common;

import lombok.Data;
import java.util.List;

/**
 * 分页结果包装对象，记录总条数与当前页数据。
 */
@Data
public class PageResult<T> {
    private Long total;
    private List<T> records;

    public PageResult(Long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
}

