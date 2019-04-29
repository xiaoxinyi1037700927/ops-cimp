package com.sinosoft.ops.cimp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(value = {"com.sinosoft.ops.cimp"})
@EnableAutoConfiguration
@EnableSwagger2
@EnableScheduling
public class CimpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CimpApplication.class, args);
    }
}
