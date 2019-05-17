package com.sinosoft.ops.cimp.config;


import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
// 读取配置文件的，通过Environment读取
@PropertySource("classpath:application.properties")
public class MongoDBConfig  extends  AbstractMongoConfiguration{


    @Autowired
    Environment env;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception{
        return new GridFsTemplate(secondaryFactory(),mappingMongoConverter());
    }

    @Bean
    public MongoDbFactory secondaryFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(),
                getDatabaseName());
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(env.getProperty("spring.data.mongodb.host"),env.getProperty("spring.data.mongodb.port",Integer.class));
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("spring.data.mongodb.database");
    }
}
