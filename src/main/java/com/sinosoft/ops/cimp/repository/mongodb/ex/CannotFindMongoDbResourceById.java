package com.sinosoft.ops.cimp.repository.mongodb.ex;

import org.springframework.data.mongodb.core.query.Query;

public class CannotFindMongoDbResourceById extends Exception {
    private static final long serialVersionUID = 2696531844537603793L;

    public CannotFindMongoDbResourceById(Query query) {
        super(String.format("不能根据条件【%s】,查询到MongoDb数据", query));
    }

    public CannotFindMongoDbResourceById(String message, Throwable e) {
        super(message, e);
    }
}
