package com.sinosoft.ops.cimp.repository.archive.ex;

import org.springframework.data.mongodb.core.query.Query;

public class CannotFindMongoDbResourceById extends Exception {

  /*** TODO请描述一下这个变量 */
  private static final long serialVersionUID = 2696531844537603793L;

  public CannotFindMongoDbResourceById(Query query) {
    super(String.format("不能根据条件【%s】,查询到MongoDb数据", query));
  }

}
