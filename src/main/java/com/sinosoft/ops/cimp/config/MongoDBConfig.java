package com.sinosoft.ops.cimp.config;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mongodb.config.MongoConfigurationSupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MongoDBConfig {

    @Value("${mongoClientIp}")
    private String mongoClientIp;
    @Value("${mongoClientPort}")
    private Integer mongoClientPort;
    @Value("${mongoDbName}")
    private String mongoDbName;

    @Bean
    public MongoTemplate mongoTemplate() {

        ServerAddress serverAddress = new ServerAddress(mongoClientIp, mongoClientPort);
        MongoClientOptions options = MongoClientOptions.builder().requiredReplicaSetName("192.168.0.143:27017").build();
        MongoClient mongoClient = new MongoClient(serverAddress, options);
        mongoClient.getMongoClientOptions();
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, mongoDbName);

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(simpleMongoDbFactory);

        MongoMappingContext mappingContext = new MongoMappingContext();

        try {
            mappingContext.setInitialEntitySet(this.getInitialEntitySet());
        } catch (ClassNotFoundException ignored) {
        }
        mappingContext.setFieldNamingStrategy(this.fieldNamingStrategy());
        mappingContext.setSimpleTypeHolder(this.customConversions().getSimpleTypeHolder());

        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setCustomConversions(new MongoCustomConversions(Collections.emptyList()));

        return new MongoTemplate(simpleMongoDbFactory, converter);
    }

    private Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        Set<Class<?>> initialEntitySet = new HashSet<>();
        for (String basePackage : this.getMappingBasePackages()) {
            initialEntitySet.addAll(this.scanForEntities(basePackage));
        }

        return initialEntitySet;
    }

    private Set<Class<?>> scanForEntities(String basePackage) throws ClassNotFoundException {
        if (!StringUtils.hasText(basePackage)) {
            return Collections.emptySet();
        } else {
            Set<Class<?>> initialEntitySet = new HashSet<>();
            if (StringUtils.hasText(basePackage)) {
                ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
                componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
                componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

                for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
                    initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(), MongoConfigurationSupport.class.getClassLoader()));
                }
            }

            return initialEntitySet;
        }
    }

    private boolean abbreviateFieldNames() {
        return false;
    }

    private FieldNamingStrategy fieldNamingStrategy() {
        return this.abbreviateFieldNames() ? new CamelCaseAbbreviatingFieldNamingStrategy() : PropertyNameFieldNamingStrategy.INSTANCE;
    }

    private Collection<String> getMappingBasePackages() {
        Package mappingBasePackage = this.getClass().getPackage();
        return Collections.singleton(mappingBasePackage == null ? null : mappingBasePackage.getName());
    }

    public CustomConversions customConversions() {
        return new MongoCustomConversions(Collections.emptyList());
    }
}
