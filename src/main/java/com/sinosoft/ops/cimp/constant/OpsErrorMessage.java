package com.sinosoft.ops.cimp.constant;

public interface OpsErrorMessage {
    /**
     * 公共模块名
     */
    String MODULE_NAME = "cimp";

    /**
     * 未找到错误编码对应的消息
     */
    String NOT_FOUND_MESSAGE = "未找到此错误编码信息，请到【{0}-message.properties】中进行配置";

    /**
     * 通用错误信息
     */
    String ERROR_MESSAGE = "000000";
    /**
     * [{0}]转换错误
     */
    String CONVERT_ERROR = "000001";
    /**
     * 文件[{0}]读取/写入失败
     */
    String FILE_IO_ERROR = "100001";
    /**
     * 读取的[{0}]文件不存在
     */
    String FILE_NOT_FOUND_ERROR = "100002";
    /**
     * 对文件进行hash失败
     */
    String FILE_HASH_ERROR = "100003";
    /**
     * 缓存引擎{0}初始化失败，对应实现类{1}无法创建
     */
    String CACHE_ERROR_100101 = "100101";

    /**
     * 缓存引擎未初始化
     */
    String CACHE_ERROR_100103 = "100103";
    /**
     * 缓存项目{0}对应的缓存容器{1}未配置
     */
    String CACHE_ERROR_100104 = "100104";
    /**
     * 缓存引擎{0}初始化失败，对应实现类{1}不存在
     */
    String CACHE_ERROR_100105 = "100105";
    /**
     * 缓存项目{0}未在classpath的[cfgCache.xml]配置文件中配置
     */
    String CACHE_ERROR_100106 = "100106";
    /**
     * 从{0}缓存引擎中读取缓存项目{1}失败
     */
    String CACHE_ERROR_100107 = "100107";

    ////////////////////////////////////
    /**
     * [{0}]报文转换错误
     */
    String ERROR_MESSAGE_100201 = "100201";
    /**
     * 100202=初始化实体基本信息失败实体名称为[{0}]
     */
    String ERROR_MESSAGE_100202 = "100202";
    /**
     * 100203=初始化实体组信息失败实体名称为[{0}]
     */
    String ERROR_MESSAGE_100203 = "100203";
    /**
     * 100204=初始化实体属性信息失败实体名称为[{0}]
     */
    String ERROR_MESSAGE_100204 = "100204";
    /**
     * 100205=初始化实体[{0}]属性[{1}]不允许重复
     */
    String ERROR_MESSAGE_100205 = "100205";
    /**
     * 100206=实体[{0}]项目编号[{1}],属性[{2}]，在配置中出现问题
     */
    String ERROR_MESSAGE_100206 = "100206";
    /**
     * 100207=实体[{0}]配置错误，必须存在主键字段
     */
    String ERROR_MESSAGE_100207 = "100207";
    /**
     * 100208=实体[{0}]配置，删除信息集信息[{1}]未配置逻辑删除列
     */
    String ERROR_MESSAGE_100208 = "100208";
}
