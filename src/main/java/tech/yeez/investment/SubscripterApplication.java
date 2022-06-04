package tech.yeez.investment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;
import tech.yeez.investment.utils.SpringBeanUtil;

/**
 * @description: Application
 * @author: xiangbin
 * @create: 2022-04-07 16:47
 **/
@Slf4j
@SpringBootApplication
@MapperScan("tech.yeez.investment.mapper")
@EnableFeignClients(basePackages = {"tech.yeez.investment.service.feign"})
public class SubscripterApplication {

    public static void main(String[] args) {
    	log.error("here is SubscripterApplication ");
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SubscripterApplication.class, args);

        SpringBeanUtil.applicationContext = configurableApplicationContext;
    } 
}

