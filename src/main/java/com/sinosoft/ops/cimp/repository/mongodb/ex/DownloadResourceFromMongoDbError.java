package com.sinosoft.ops.cimp.repository.mongodb.ex;

public class DownloadResourceFromMongoDbError extends Exception {
    private static final long serialVersionUID = 6651915663214807266L;

    public DownloadResourceFromMongoDbError() {
        super();
    }

    public DownloadResourceFromMongoDbError(String message) {
        super(message);
    }

    public DownloadResourceFromMongoDbError(String message, Throwable e) {
        super(message, e);
    }
}
