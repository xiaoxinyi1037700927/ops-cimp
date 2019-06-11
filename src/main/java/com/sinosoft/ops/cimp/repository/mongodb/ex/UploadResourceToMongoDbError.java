package com.sinosoft.ops.cimp.repository.mongodb.ex;

public class UploadResourceToMongoDbError extends Exception {
    private static final long serialVersionUID = 4038932957892886215L;

    public UploadResourceToMongoDbError() {
        super();
    }

    public UploadResourceToMongoDbError(String message) {
        super(message);
    }

    public UploadResourceToMongoDbError(String message, Throwable e) {
        super(message, e);
    }
}
